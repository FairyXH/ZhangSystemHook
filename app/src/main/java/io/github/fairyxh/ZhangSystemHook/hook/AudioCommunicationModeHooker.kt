package io.github.fairyxh.ZhangSystemHook.hook

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.telephony.TelephonyManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/** 在 system_server 的 AudioService 中：开关开启时禁止所有应用进入通话/通信模式、使用听筒，音频强制到扬声器/媒体音量。 */
object AudioCommunicationModeHooker : YukiBaseHooker() {
    private const val TAG = "AudioBlocker"
    private const val FORCE_NORMAL_DELAY_MS = 600L
    private val forceNormalPending = AtomicBoolean(false)
    private val correctionHandler: Handler by lazy {
        Handler(HandlerThread("AudioModeCorrector").apply { start() }.looper)
    }

    override fun onHook() {
        HookLog.i(TAG, "[Audio] installing AudioService hooks")
        val audioService = runCatching {
            "com.android.server.audio.AudioService".toClass()
        }.getOrElse {
            HookLog.e(TAG, "[Audio] AudioService class unavailable", it)
            return
        }
        hookMethods(audioService, "setMode")
        hookModeCommit(audioService)
        hookOriginalMode(audioService)
        hookCommunicationDevice(audioService)
        hookPreferredDeviceForStrategy(audioService)
        hookSpeakerphone(audioService)
        hookMethods(audioService, "startBluetoothSco")
        hookMethods(audioService, "startBluetoothScoVirtualCall")
        hookMethods(audioService, "stopBluetoothSco")
        hookVolumeSelection(audioService)
        hookPhoneState()
        hookEarpieceDisable()
        HookLog.i(TAG, "[Audio] AudioService hook scan completed")
    }

    private fun hookMethods(clazz: Class<*>, methodName: String) {
        runCatching {
            val candidates = allMethods(clazz).filter { it.name == methodName }.distinctBy(Method::toGenericString)
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[Audio] candidate not found: ${clazz.name}.$methodName")
                return
            }
            candidates.forEach { method: Method ->
                method.isAccessible = true
                method.hook {
                    before {
                        val decision = decide(methodName, instance, args)
                        val normalizeMode = methodName == "setMode" && decision.block
                        HookLog.i(
                            "AudioMode",
                            "$methodName${method.parameterTypes.contentToString()} uid=${decision.uid} " +
                                "pkg=${decision.packageName} args=${args.contentToString()} " +
                                "enabled=${decision.enabled} privileged=${decision.privileged} " +
                                "state=${decision.state} " +
                                "decision=${when {
                                    normalizeMode -> "FORCE_MODE_NORMAL"
                                    decision.block -> "BLOCK"
                                    else -> "ALLOW"
                                }}"
                        )
                        if (normalizeMode) {
                            val modeIndex = firstIntParameterIndex(method)
                            if (modeIndex >= 0) {
                                args[modeIndex] = AudioManager.MODE_NORMAL
                            } else {
                                HookLog.w(TAG, "[Audio] setMode signature has no mode argument; allowing unchanged")
                            }
                        } else if (decision.block) {
                            result = defaultResult(method.returnType)
                        }
                    }
                }
                HookLog.i(TAG, "[Audio] installed $methodName${method.parameterTypes.contentToString()}")
            }
        }.onFailure { HookLog.e(TAG, "[Audio] failed to hook $methodName", it) }
    }

    /**
     * ColorOS 上模式真正提交点是 setOriginalMode -> MSG(36) -> onUpdateAudioMode，
     * AOSP 对应 setModeInt。这里补一层：提交前改写请求模式，提交后兜底强切回普通模式。
     */
    private fun hookModeCommit(clazz: Class<*>) {
        val names = setOf("onUpdateAudioMode", "setModeInt")
        val candidates = runCatching {
            allMethods(clazz).filter { it.name in names }.distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[ModeCommit] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[ModeCommit] candidate not found: onUpdateAudioMode/setModeInt")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        val modeIndex = firstIntParameterIndex(method)
                        val requested = if (modeIndex >= 0) args.getOrNull(modeIndex) as? Int else null
                        if (requested != null && isCallMode(requested) && communicationModeBlocked()) {
                            args[modeIndex] = AudioManager.MODE_NORMAL
                            HookLog.i(
                                "AudioMode",
                                "[ModeCommit] FORCE_MODE_NORMAL ${method.name}${method.parameterTypes.contentToString()} " +
                                    "requested=$requested"
                            )
                        }
                    }
                    after {
                        forceNormalIfNeeded(instance, findContext(instance))
                    }
                }
                HookLog.i(TAG, "[ModeCommit] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[ModeCommit] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    /** 兜底：mMode 已进入通话/通信模式时，强行切回 MODE_NORMAL（不分调用方）。 */
    private fun forceNormalIfNeeded(audioService: Any?, context: Context?) {
        if (!communicationModeBlocked()) return
        val mode = readMode(audioService) ?: return
        if (!isCallMode(mode)) return
        if (!forceNormalPending.compareAndSet(false, true)) return
        HookLog.i(TAG, "[ForceNormal] mode=$mode callState=${readCallState(context)} scheduling setMode(MODE_NORMAL)")
        correctionHandler.postDelayed({
            forceNormalPending.set(false)
            runCatching {
                val method = setModeMethod(audioService) ?: error("setMode not found")
                val binder = readModeOwnerBinder(audioService) ?: Binder()
                val invokeArgs = when (method.parameterTypes.size) {
                    3 -> arrayOf<Any?>(AudioManager.MODE_NORMAL, binder, "ZhangSystemHook")
                    else -> arrayOf<Any?>(AudioManager.MODE_NORMAL, binder)
                }
                method.isAccessible = true
                method.invoke(audioService, *invokeArgs)
                HookLog.i(TAG, "[ForceNormal] invoked ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[ForceNormal] correction failed", it)
            }
        }, FORCE_NORMAL_DELAY_MS)
    }

    private fun isCallMode(mode: Int): Boolean =
        mode == AudioManager.MODE_IN_CALL || mode == AudioManager.MODE_IN_COMMUNICATION ||
            mode == AudioManager.MODE_CALL_SCREENING

    /** ColorOS 中间层 setOriginalMode 的 mode 参数同样改写（覆盖游戏模式消息 114 等直达路径）。 */
    private fun hookOriginalMode(clazz: Class<*>) {
        val candidates = runCatching {
            allMethods(clazz).filter { it.name == "setOriginalMode" }.distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[OriginalMode] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[OriginalMode] candidate not found: setOriginalMode")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        val modeIndex = firstIntParameterIndex(method)
                        val requested = if (modeIndex >= 0) args.getOrNull(modeIndex) as? Int else null
                        if (requested != null && isCallMode(requested) && communicationModeBlocked()) {
                            args[modeIndex] = AudioManager.MODE_NORMAL
                            HookLog.i(
                                "AudioMode",
                                "[OriginalMode] FORCE_MODE_NORMAL ${method.name}${method.parameterTypes.contentToString()} " +
                                    "requested=$requested"
                            )
                        }
                    }
                }
                HookLog.i(TAG, "[OriginalMode] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[OriginalMode] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    /** 应用可通过 setPreferredDeviceForStrategy 把通信策略设备指到听筒，改写为扬声器。 */
    private fun hookPreferredDeviceForStrategy(clazz: Class<*>) {
        val attributesClass = runCatching {
            "android.media.AudioDeviceAttributes".toClass()
        }.getOrElse {
            HookLog.w(TAG, "[Strategy] AudioDeviceAttributes class unavailable; skip")
            return
        }
        val candidates = runCatching {
            allMethods(clazz)
                .filter { method ->
                    method.name == "setPreferredDeviceForStrategy" && method.parameterTypes.isNotEmpty() &&
                        method.parameterTypes.last() == attributesClass
                }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[Strategy] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[Strategy] candidate not found: setPreferredDeviceForStrategy")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        if (communicationModeBlocked()) {
                            val device = args.lastOrNull()
                            val deviceType = device?.let { invokeInt(it, "getType") }
                            if (deviceType == AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                                val speakerAttrs = runCatching {
                                    attributesClass
                                        .getConstructor(Int::class.javaPrimitiveType, String::class.java)
                                        .newInstance(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, "")
                                }.getOrNull()
                                if (speakerAttrs != null) {
                                    args[args.size - 1] = speakerAttrs
                                    HookLog.i(
                                        TAG,
                                        "[Strategy] REWRITE_EARPIECE_TO_SPEAKER ${method.name}${method.parameterTypes.contentToString()} " +
                                            "args=${args.contentToString()}"
                                    )
                                }
                            }
                        }
                    }
                }
                HookLog.i(TAG, "[Strategy] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[Strategy] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    /** setSpeakerphoneOn(false) 强制改写为 true，防止应用/系统把外放切回听筒。 */
    private fun hookSpeakerphone(clazz: Class<*>) {
        val candidates = runCatching {
            allMethods(clazz)
                .filter {
                    it.name == "setSpeakerphoneOn" && it.parameterTypes.size >= 2 &&
                        it.parameterTypes[0] == IBinder::class.java && it.parameterTypes[1] == Boolean::class.javaPrimitiveType
                }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[Speaker] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[Speaker] candidate not found: setSpeakerphoneOn")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        if (communicationModeBlocked() && args.getOrNull(1) == false) {
                            args[1] = true
                            HookLog.i(
                                TAG,
                                "[Speaker] FORCE_SPEAKER_ON ${method.name}${method.parameterTypes.contentToString()} " +
                                    "args=${args.contentToString()}"
                            )
                        }
                    }
                }
                HookLog.i(TAG, "[Speaker] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[Speaker] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    /** AudioSystem.setPhoneState 是 HAL 侧切换通话模式的最终点，作为兜底：返回失败阻止切换。 */
    private fun hookPhoneState() {
        val audioSystem = runCatching {
            "android.media.AudioSystem".toClass()
        }.getOrElse {
            HookLog.e(TAG, "[PhoneState] AudioSystem class unavailable", it)
            return
        }
        audioSystemClass = audioSystem
        val candidates = runCatching {
            allMethods(audioSystem).filter { it.name == "setPhoneState" }.distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[PhoneState] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[PhoneState] candidate not found: AudioSystem.setPhoneState")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        if (communicationModeBlocked()) {
                            val state = firstInt(args)
                            if (state != null && isCallMode(state)) {
                                result = -1
                                HookLog.i(
                                    TAG,
                                    "[PhoneState] BLOCK_HAL_CALL_MODE ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}"
                                )
                            }
                        }
                    }
                }
                HookLog.i(TAG, "[PhoneState] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[PhoneState] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    private fun hookCommunicationDevice(clazz: Class<*>) {
        val candidates = runCatching {
            allMethods(clazz)
                .filter { method ->
                    method.name.startsWith("setCommunicationDevice") &&
                        method.parameterTypes.size == 2 &&
                        IBinder::class.java.isAssignableFrom(method.parameterTypes[0]) &&
                        (method.parameterTypes[1] == Int::class.javaPrimitiveType ||
                            method.parameterTypes[1] == AudioDeviceInfo::class.java)
                }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[CommunicationDevice] candidate discovery failed", it)
            return
        }
        if (candidates.isEmpty()) {
            HookLog.w(
                TAG,
                "[CommunicationDevice] candidate not found: AudioService.setCommunicationDevice(IBinder, int)"
            )
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        rewriteCommunicationDevice(instance, method, args)
                    }
                }
                HookLog.i(
                    TAG,
                    "[CommunicationDevice] installed ${method.name}${method.parameterTypes.contentToString()}"
                )
            }.onFailure {
                HookLog.e(TAG, "[CommunicationDevice] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    private fun rewriteCommunicationDevice(audioService: Any?, method: Method, args: Array<Any?>) {
        if (method.parameterTypes.size >= 2 && method.parameterTypes[1] == AudioDeviceInfo::class.java) {
            rewriteCommunicationDeviceInfo(audioService, args)
            return
        }
        rewriteCommunicationDeviceId(audioService, args)
    }

    private fun rewriteCommunicationDeviceId(audioService: Any?, args: Array<Any?>) {
        val uid = Binder.getCallingUid()
        val context = findContext(audioService)
        val packageName = resolvePackageName(context, uid)
        val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
        val systemApp = isSystemApplication(context, uid)
        val enabled = runCatching {
            ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE)
        }.onFailure {
            HookLog.e(TAG, "[CommunicationDevice] failed to read switch; allowing uid=$uid pkg=$packageName", it)
        }.getOrDefault(false)
        val requestedDeviceId = args.getOrNull(1) as? Int
        if (requestedDeviceId == null) {
            HookLog.w(
                TAG,
                "[CommunicationDevice] ALLOW invalid deviceId uid=$uid pkg=$packageName args=${args.contentToString()}"
            )
            return
        }

        runCatching {
            val audioManager = context?.getSystemService(AudioManager::class.java)
            if (audioManager == null) {
                HookLog.w(
                    TAG,
                    "[CommunicationDevice] ALLOW AudioManager unavailable uid=$uid pkg=$packageName deviceId=$requestedDeviceId"
                )
                return
            }
            val devices = audioManager.availableCommunicationDevices
            devices.forEach { device ->
                HookLog.i(
                    TAG,
                    "[CommunicationDevice] available deviceId=${device.id} type=${device.type} " +
                        "productName=${device.productName} address=${device.address} isSource=${device.isSource}"
                )
            }
            val resolvedDevice = if (requestedDeviceId == 0) {
                audioManager.communicationDevice
            } else {
                devices.firstOrNull { it.id == requestedDeviceId }
            }
            HookLog.i(
                TAG,
                "[CommunicationDevice] request uid=$uid pkg=$packageName privileged=$privileged " +
                    "systemApp=$systemApp enabled=$enabled requestedDeviceId=$requestedDeviceId " +
                    "resolvedDeviceId=${resolvedDevice?.id} resolvedType=${resolvedDevice?.type} " +
                    "address=${resolvedDevice?.address} isSource=${resolvedDevice?.isSource}"
            )
            if (!enabled) {
                HookLog.i(
                    TAG,
                    "[CommunicationDevice] ALLOW switch off uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type}"
                )
                return
            }
            val speaker = devices.firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
            if (speaker == null) {
                HookLog.w(
                    TAG,
                    "[CommunicationDevice] ALLOW speaker unavailable uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type}"
                )
                return
            }
            if (requestedDeviceId != 0 && resolvedDevice?.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                HookLog.i(
                    TAG,
                    "[CommunicationDevice] ALLOW already speaker uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId speakerId=${speaker.id}"
                )
                return
            }
            args[1] = speaker.id
            HookLog.i(
                TAG,
                "[CommunicationDevice] REWRITE_TO_SPEAKER uid=$uid pkg=$packageName " +
                    "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type} " +
                    "speakerId=${speaker.id} speakerType=${speaker.type} " +
                    "speakerAddress=${speaker.address} speakerIsSource=${speaker.isSource}"
            )
        }.onFailure {
            HookLog.e(
                TAG,
                "[CommunicationDevice] ALLOW route inspection failed uid=$uid pkg=$packageName " +
                    "deviceId=$requestedDeviceId",
                it
            )
        }
    }

    /** 参数形状为 (IBinder, AudioDeviceInfo) 的 OEM 变体（如 ColorOS 扩展），同样强制扬声器。 */
    private fun rewriteCommunicationDeviceInfo(audioService: Any?, args: Array<Any?>) {
        val uid = Binder.getCallingUid()
        val context = findContext(audioService)
        val packageName = resolvePackageName(context, uid)
        val enabled = communicationModeBlocked()
        val requested = args.getOrNull(1) as? AudioDeviceInfo
        if (requested == null) {
            HookLog.w(
                TAG,
                "[CommunicationDevice] ALLOW invalid AudioDeviceInfo uid=$uid pkg=$packageName args=${args.contentToString()}"
            )
            return
        }
        if (!enabled || requested.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
            HookLog.i(
                TAG,
                "[CommunicationDevice] ALLOW uid=$uid pkg=$packageName requestedType=${requested.type} enabled=$enabled"
            )
            return
        }
        runCatching {
            val audioManager = context?.getSystemService(AudioManager::class.java)
            val speaker = audioManager?.availableCommunicationDevices
                ?.firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
            if (speaker == null) {
                HookLog.w(
                    TAG,
                    "[CommunicationDevice] ALLOW speaker unavailable uid=$uid pkg=$packageName requestedType=${requested.type}"
                )
                return
            }
            args[1] = speaker
            HookLog.i(
                TAG,
                "[CommunicationDevice] REWRITE_TO_SPEAKER(INFO) uid=$uid pkg=$packageName " +
                    "requestedType=${requested.type} speakerId=${speaker.id} speakerType=${speaker.type}"
            )
        }.onFailure {
            HookLog.e(TAG, "[CommunicationDevice] ALLOW route inspection failed (info) uid=$uid pkg=$packageName", it)
        }
    }

    private fun hookVolumeSelection(clazz: Class<*>) {
        runCatching {
            val candidates = allMethods(clazz)
                .filter { it.name.startsWith("adjustStreamVolume") || it.name.startsWith("adjustSuggestedStreamVolume") }
                .distinctBy(Method::toGenericString)
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[Audio] candidate not found: adjustStreamVolume/adjustSuggestedStreamVolume")
                return
            }
            candidates.forEach { method: Method ->
                method.isAccessible = true
                method.hook {
                    before {
                        val enabled = communicationModeBlocked()
                        val streamIndex = volumeStreamParameterIndex(method, args)
                        if (streamIndex != null) {
                            val uid = Binder.getCallingUid()
                            val context = findContext(instance)
                            val packageName = resolvePackageName(context, uid)
                            val mode = readMode(instance)
                            val requestedStream = args.getOrNull(streamIndex) as? Int
                            val activeStream = invokeInt(
                                instance,
                                "getActiveStreamType",
                                AudioManager.USE_DEFAULT_STREAM_TYPE
                            )
                            val communicationMode = mode != null && isCallMode(mode)
                            val voiceCallSelected = requestedStream == AudioManager.STREAM_VOICE_CALL ||
                                activeStream == AudioManager.STREAM_VOICE_CALL
                            val shouldRewrite = enabled && (communicationMode || voiceCallSelected)
                            HookLog.i(
                                "AudioMode",
                                "${method.name} args=${args.contentToString()} uid=$uid " +
                                    "pkg=$packageName mode=$mode requestedStream=$requestedStream " +
                                    "streamArg=$streamIndex activeStream=$activeStream " +
                                    "decision=${if (shouldRewrite) "REWRITE_TO_MUSIC" else "ALLOW"}"
                            )
                            if (shouldRewrite && requestedStream != AudioManager.STREAM_MUSIC) {
                                args[streamIndex] = AudioManager.STREAM_MUSIC
                            }
                        } else {
                            HookLog.w(
                                "AudioMode",
                                "${method.name}${method.parameterTypes.contentToString()} unknown stream slot; ALLOW"
                            )
                        }
                    }
                }
                HookLog.i(TAG, "[Audio] installed ${method.name}${method.parameterTypes.contentToString()}")
            }
        }.onFailure { HookLog.e(TAG, "[Audio] failed to hook adjustStreamVolume family", it) }
    }

    /**
     * AudioSystem 隐藏常量：听筒输出设备 / 设备状态。
     */
    private const val DEVICE_OUT_EARPIECE = 0x00000001
    private const val DEVICE_OUT_SPEAKER = 0x00000004
    private const val DEVICE_STATE_UNAVAILABLE = 0
    private const val DEVICE_STATE_AVAILABLE = 1
    private const val EARPIECE_DISCONNECT_DELAY_MS = 5000L

    private var audioSystemClass: Class<*>? = null

    /**
     * 从系统层面禁用听筒：任何“连接听筒”请求改写为“断开”，查询听筒状态返回不可用，
     * 并在安装后主动断开一次。应用（如 QQ）无法把通信音频路由到听筒，系统会回落到扬声器。
     */
    private fun hookEarpieceDisable() {
        val audioSystem = runCatching {
            "android.media.AudioSystem".toClass()
        }.getOrElse {
            HookLog.e(TAG, "[Earpiece] AudioSystem class unavailable", it)
            return
        }
        audioSystemClass = audioSystem
        val setters = runCatching {
            allMethods(audioSystem).filter { it.name.startsWith("setDeviceConnectionState") }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[Earpiece] setDeviceConnectionState discovery failed", it)
            emptyList()
        }
        if (setters.isEmpty()) {
            HookLog.w(TAG, "[Earpiece] candidate not found: AudioSystem.setDeviceConnectionState*")
        }
        setters.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        if (communicationModeBlocked()) {
                            val device = firstInt(args)
                            val state = if (method.parameterTypes.size >= 2 &&
                                method.parameterTypes[1] == Int::class.javaPrimitiveType
                            ) {
                                args.getOrNull(1) as? Int
                            } else null
                            if (device == DEVICE_OUT_EARPIECE && state == DEVICE_STATE_AVAILABLE) {
                                args[1] = DEVICE_STATE_UNAVAILABLE
                                HookLog.i(
                                    TAG,
                                    "[Earpiece] REWRITE_EARPIECE_OFF ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}"
                                )
                            }
                        }
                    }
                }
                HookLog.i(TAG, "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[Earpiece] hook failed: ${method.toGenericString()}", it)
            }
        }
        val getters = runCatching {
            allMethods(audioSystem).filter { it.name == "getDeviceConnectionState" }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[Earpiece] getDeviceConnectionState discovery failed", it)
            emptyList()
        }
        if (getters.isEmpty()) {
            HookLog.w(TAG, "[Earpiece] candidate not found: AudioSystem.getDeviceConnectionState")
        }
        getters.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        if (communicationModeBlocked()) {
                            val device = firstInt(args)
                            if (device == DEVICE_OUT_EARPIECE) {
                                result = DEVICE_STATE_UNAVAILABLE
                                HookLog.i(
                                    TAG,
                                    "[Earpiece] REPORT_UNAVAILABLE ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}"
                                )
                            }
                        }
                    }
                }
                HookLog.i(TAG, "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[Earpiece] hook failed: ${method.toGenericString()}", it)
            }
        }
        val deviceGetters = runCatching {
            allMethods(audioSystem)
                .filter { it.name == "getDevicesForAttributes" || it.name == "getOutputDevices" }
                .distinctBy(Method::toGenericString)
        }.getOrElse {
            HookLog.e(TAG, "[Earpiece] getDevicesForAttributes discovery failed", it)
            emptyList()
        }
        if (deviceGetters.isEmpty()) {
            HookLog.w(TAG, "[Earpiece] candidate not found: AudioSystem.getDevicesForAttributes/getOutputDevices")
        }
        deviceGetters.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    after {
                        if (communicationModeBlocked()) {
                            val devices = result as? Int
                            if (devices != null && devices and DEVICE_OUT_EARPIECE != 0) {
                                val filtered = (devices and DEVICE_OUT_EARPIECE.inv()) or DEVICE_OUT_SPEAKER
                                result = filtered
                                HookLog.i(
                                    TAG,
                                    "[Earpiece] FILTER_DEVICE_MASK ${method.name}${method.parameterTypes.contentToString()} " +
                                        "devices=$devices filtered=$filtered"
                                )
                            }
                        }
                    }
                }
                HookLog.i(TAG, "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e(TAG, "[Earpiece] hook failed: ${method.toGenericString()}", it)
            }
        }
        correctionHandler.postDelayed({
            if (communicationModeBlocked()) {
                disconnectEarpiece()
            }
        }, EARPIECE_DISCONNECT_DELAY_MS)
    }

    private fun disconnectEarpiece() {
        val clazz = audioSystemClass ?: return
        val method = runCatching {
            allMethods(clazz)
                .filter {
                    it.name.startsWith("setDeviceConnectionState") &&
                        it.parameterTypes.size >= 3 &&
                        it.parameterTypes[0] == Int::class.javaPrimitiveType &&
                        it.parameterTypes[1] == Int::class.javaPrimitiveType
                }
                .sortedByDescending { it.parameterTypes.size }
                .firstOrNull()
        }.getOrNull() ?: run {
            HookLog.w(TAG, "[Earpiece] setDeviceConnectionState method not found for disconnect")
            return
        }
        runCatching {
            method.isAccessible = true
            val invokeArgs = when (method.parameterTypes.size) {
                4 -> arrayOf<Any?>(DEVICE_OUT_EARPIECE, DEVICE_STATE_UNAVAILABLE, "", "")
                else -> arrayOf<Any?>(DEVICE_OUT_EARPIECE, DEVICE_STATE_UNAVAILABLE, "")
            }
            method.invoke(null, *invokeArgs)
            HookLog.i(TAG, "[Earpiece] disconnected earpiece via ${method.name}")
        }.onFailure {
            HookLog.e(TAG, "[Earpiece] disconnect failed", it)
        }
    }

    private fun allMethods(clazz: Class<*>): List<Method> = buildList {
        var current: Class<*>? = clazz
        while (current != null) {
            addAll(current.declaredMethods.filter { !java.lang.reflect.Modifier.isAbstract(it.modifiers) })
            current = current.superclass
        }
    }

    private fun firstIntParameterIndex(method: Method): Int = method.parameterTypes.indexOfFirst {
        it == Int::class.javaPrimitiveType || it == Int::class.javaObjectType
    }

    /**
     * 定位音量调整方法中的 stream 参数槽。
     * adjustStreamVolume* 族 stream 恒在参数 0；
     * adjustSuggestedStreamVolume* 族 AOSP 顺序为 (direction, suggestedStream, ...)，但华为/荣耀的
     * adjustSuggestedStreamVolumeForUid 是 (suggestedStream, direction, ...)，参数 0 恒为 USE_DEFAULT_STREAM_TYPE，
     * 参数 1 才是方向。这里按运行时值启发式识别，避免把方向参数改写坏导致音量键失效。
     */
    private fun volumeStreamParameterIndex(method: Method, args: Array<Any?>): Int? {
        val parameterTypes = method.parameterTypes
        val intType = Int::class.javaPrimitiveType
        return when {
            method.name.startsWith("adjustStreamVolume") -> {
                if (parameterTypes.isNotEmpty() && parameterTypes[0] == intType) 0 else null
            }
            method.name.startsWith("adjustSuggestedStreamVolume") -> {
                if (parameterTypes.size < 2 || parameterTypes[0] != intType || parameterTypes[1] != intType) {
                    null
                } else {
                    val first = args.getOrNull(0) as? Int
                    val second = args.getOrNull(1) as? Int
                    when {
                        first == AudioManager.USE_DEFAULT_STREAM_TYPE -> 0
                        second == AudioManager.USE_DEFAULT_STREAM_TYPE -> 1
                        else -> 1
                    }
                }
            }
            else -> null
        }
    }

    private fun decide(methodName: String, audioService: Any?, args: Array<Any?>): Decision {
        val uid = Binder.getCallingUid()
        val enabled = runCatching {
            ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE)
        }.getOrDefault(false)
        val context = findContext(audioService)
        val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
        val systemApp = isSystemApplication(context, uid)
        val packageName = resolvePackageName(context, uid)
        val acquisition = when (methodName) {
            "setMode" -> firstInt(args)?.let { isCallMode(it) } ?: false
            "startBluetoothSco", "startBluetoothScoVirtualCall" -> true
            "stopBluetoothSco" -> false
            else -> false
        }
        val block = enabled && acquisition
        return Decision(uid, packageName, enabled, privileged, block, describeState(audioService, context))
    }

    private fun firstInt(args: Array<Any?>): Int? = args.firstOrNull { it is Int } as? Int

    private fun communicationModeBlocked(): Boolean = runCatching {
        ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE)
    }.getOrDefault(false)

    @Suppress("DEPRECATION")
    private fun readCallState(context: Context?): Int? = runCatching {
        val telephony = context?.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return null
        telephony.callState
    }.getOrNull()

    private fun readMode(service: Any?): Int? {
        invokeInt(service, "getMode")?.let { return it }
        return readIntField(service, "mMode")
    }

    /** 读取当前通话模式持有者的 Binder（ColorOS: SetModeDeathHandler.getBinder()）。 */
    private fun readModeOwnerBinder(service: Any?): IBinder? = runCatching {
        val field = generateSequence(service?.javaClass) { it.superclass }
            .flatMap { it.declaredFields.asSequence() }
            .firstOrNull { it.name == "mSetModeDeathHandlers" }
            ?: return null
        field.isAccessible = true
        val handlers = field.get(service) as? List<*> ?: return null
        for (handler in handlers) {
            if (handler == null) continue
            val mode = invokeInt(handler, "getMode") ?: continue
            if (!isCallMode(mode)) continue
            return invokeObject(handler, "getBinder") as? IBinder
        }
        null
    }.getOrNull()

    private fun invokeObject(service: Any?, name: String): Any? = runCatching {
        val method = generateSequence(service?.javaClass) { it.superclass }
            .flatMap { it.declaredMethods.asSequence() }
            .firstOrNull { it.name == name && it.parameterTypes.isEmpty() }
            ?: return null
        method.isAccessible = true
        method.invoke(service)
    }.getOrNull()

    private fun setModeMethod(service: Any?): Method? = runCatching {
        generateSequence(service?.javaClass) { it.superclass }
            .flatMap { it.declaredMethods.asSequence() }
            .filter {
                it.name == "setMode" && it.parameterTypes.isNotEmpty() &&
                    it.parameterTypes[0] == Int::class.javaPrimitiveType
            }
            .firstOrNull { it.parameterTypes.size == 3 }
            ?: generateSequence(service?.javaClass) { it.superclass }
                .flatMap { it.declaredMethods.asSequence() }
                .filter {
                    it.name == "setMode" && it.parameterTypes.size == 2 &&
                        it.parameterTypes[0] == Int::class.javaPrimitiveType
                }
                .firstOrNull()
    }.getOrNull()


    private fun findContext(service: Any?): Context? = runCatching {
        generateSequence(service?.javaClass) { it.superclass }
            .mapNotNull { it.declaredFields.firstOrNull { field -> field.name == "mContext" } }
            .firstOrNull()
            ?.apply { isAccessible = true }
            ?.get(service) as? Context
    }.getOrNull()

    private fun hasRoutingPrivilege(context: Context?): Boolean = context?.let {
        it.checkCallingPermission("android.permission.MODIFY_PHONE_STATE") == PackageManager.PERMISSION_GRANTED ||
            it.checkCallingPermission("android.permission.MODIFY_AUDIO_ROUTING") == PackageManager.PERMISSION_GRANTED
    } ?: true

    private fun resolvePackageName(context: Context?, uid: Int): String = runCatching {
        context?.packageManager?.getNameForUid(uid) ?: "<unknown>"
    }.getOrDefault("<unknown>")

    private fun isSystemApplication(context: Context?, uid: Int): Boolean {
        if (uid < Process.FIRST_APPLICATION_UID) return true
        return runCatching {
            val packageManager = context?.packageManager ?: return true
            val packages = packageManager.getPackagesForUid(uid) ?: return true
            packages.any { packageName ->
                val flags = packageManager.getApplicationInfo(packageName, 0).flags
                flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            }
        }.onFailure {
            HookLog.e(TAG, "[Audio] failed to resolve system-app state uid=$uid; allowing", it)
        }.getOrDefault(true)
    }

    private fun describeState(service: Any?, context: Context?): String = runCatching {
        val mode = invokeInt(service, "getMode") ?: readIntField(service, "mMode")
        val activeStream = invokeInt(service, "getActiveStreamType", AudioManager.USE_DEFAULT_STREAM_TYPE)
        "mode=${mode ?: "?"},activeStream=${activeStream ?: "?"},callState=${readCallState(context)}"
    }.getOrDefault("mode=?,activeStream=?,callState=?")

    private fun invokeInt(service: Any?, name: String, vararg args: Any): Int? = runCatching {
        val method = generateSequence(service?.javaClass) { it.superclass }
            .flatMap { it.declaredMethods.asSequence() }
            .firstOrNull { it.name == name && it.parameterTypes.size == args.size }
            ?: return null
        method.isAccessible = true
        method.invoke(service, *args) as? Int
    }.getOrNull()

    private fun readIntField(service: Any?, name: String): Int? = runCatching {
        val field = generateSequence(service?.javaClass) { it.superclass }
            .flatMap { it.declaredFields.asSequence() }
            .firstOrNull { it.name == name }
            ?: return null
        field.isAccessible = true
        when (val value = field.get(service)) {
            is Int -> value
            is AtomicInteger -> value.get()
            else -> null
        }
    }.getOrNull()

    private fun defaultResult(returnType: Class<*>): Any? = when (returnType) {
        Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> false
        Int::class.javaPrimitiveType, Int::class.javaObjectType -> 0
        Long::class.javaPrimitiveType, Long::class.javaObjectType -> 0L
        Float::class.javaPrimitiveType, Float::class.javaObjectType -> 0f
        Double::class.javaPrimitiveType, Double::class.javaObjectType -> 0.0
        else -> null
    }

    private data class Decision(
        val uid: Int,
        val packageName: String,
        val enabled: Boolean,
        val privileged: Boolean,
        val block: Boolean,
        val state: String
    )
}
