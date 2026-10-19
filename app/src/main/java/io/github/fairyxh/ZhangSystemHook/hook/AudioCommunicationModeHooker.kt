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
import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/** 在 system_server 的 AudioService 中：开关开启时禁止所有应用进入通话/通信模式、使用听筒，音频强制到扬声器/媒体音量。 */
object AudioCommunicationModeHooker : BaseHooker() {
    private const val TAG = "AudioBlocker"
    private const val FORCE_NORMAL_DELAY_MS = 600L
    private val forceNormalPending = AtomicBoolean(false)
    private val syncingVolumeAlias = AtomicBoolean(false)
    // ColorOS 音量索引范围大（MUSIC=160 级），通话/SCO 只有几档；换算用缓存（syncing 保护下读取真实 max）
    private var cachedMusicMaxVolume = 0
    private var cachedVoiceCallMaxVolume = 0
    private var cachedScoMaxVolume = 0
    private val correctionHandler: Handler by lazy {
        Handler(HandlerThread("AudioModeCorrector").apply { start() }.looper)
    }

    override fun onHook() {
        HookLog.i(TAG) { "[Audio] installing AudioService hooks" }
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
        hookVolumeAliasToMusic(audioService)
        hookPhoneState()
        hookEarpieceDisable()
        HookLog.i(TAG) { "[Audio] AudioService hook scan completed" }
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
                        if (!communicationModeBlocked()) return@before
                        val decision = decide(methodName, instance, args)
                        val normalizeMode = methodName == "setMode" && decision.block
                        HookLog.i("AudioMode") { "$methodName${method.parameterTypes.contentToString()} uid=${decision.uid} " +
                                "pkg=${decision.packageName} args=${args.contentToString()} " +
                                "enabled=${decision.enabled} privileged=${decision.privileged} " +
                                "state=${decision.state} " +
                                "decision=${when {
                                    normalizeMode -> "FORCE_MODE_NORMAL"
                                    decision.block -> "BLOCK"
                                    else -> "ALLOW"
                                }}" }
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
                HookLog.i(TAG) { "[Audio] installed $methodName${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        val modeIndex = firstIntParameterIndex(method)
                        val requested = if (modeIndex >= 0) args.getOrNull(modeIndex) as? Int else null
                        // 仅拦截第三方 app 持有的通话模式；Telephony 等系统模式放行（真实电话不受影响）
                        if (requested != null && isCallMode(requested) && communicationModeBlocked() &&
                            isAppOwnedCallMode(instance)
                        ) {
                            args[modeIndex] = AudioManager.MODE_NORMAL
                            HookLog.i("AudioMode") { "[ModeCommit] FORCE_MODE_NORMAL ${method.name}${method.parameterTypes.contentToString()} " +
                                    "requested=$requested" }
                        }
                    }
                    after {
                        forceNormalIfNeeded(instance, findContext(instance))
                    }
                }
                HookLog.i(TAG) { "[ModeCommit] installed ${method.name}${method.parameterTypes.contentToString()}" }
            }.onFailure {
                HookLog.e(TAG, "[ModeCommit] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    /** 兜底：第三方 app 持有的通话/通信模式已进入 mMode 时，强行切回 MODE_NORMAL。 */
    private fun forceNormalIfNeeded(audioService: Any?, context: Context?) {
        if (!communicationModeBlocked()) return
        // 系统（Telephony）持有的真实通话模式一律不动
        if (!isAppOwnedCallMode(audioService)) return
        val mode = readMode(audioService) ?: return
        if (!isCallMode(mode)) return
        if (!forceNormalPending.compareAndSet(false, true)) return
        HookLog.i(TAG) { "[ForceNormal] mode=$mode callState=${readCallState(context)} scheduling setMode(MODE_NORMAL)" }
        correctionHandler.postDelayed({
            forceNormalPending.set(false)
            if (!communicationModeBlocked() || !isAppOwnedCallMode(audioService) || readMode(audioService)?.let(::isCallMode) != true) return@postDelayed
            runCatching {
                val method = setModeMethod(audioService) ?: error("setMode not found")
                val binder = readModeOwnerBinder(audioService) ?: Binder()
                val invokeArgs = when (method.parameterTypes.size) {
                    3 -> arrayOf<Any?>(AudioManager.MODE_NORMAL, binder, "ZhangSystemHook")
                    else -> arrayOf<Any?>(AudioManager.MODE_NORMAL, binder)
                }
                method.isAccessible = true
                method.invoke(audioService, *invokeArgs)
                HookLog.i(TAG) { "[ForceNormal] invoked ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        val modeIndex = firstIntParameterIndex(method)
                        val requested = if (modeIndex >= 0) args.getOrNull(modeIndex) as? Int else null
                        if (requested != null && isCallMode(requested) && communicationModeBlocked() &&
                            isAppOwnedCallMode(instance)
                        ) {
                            args[modeIndex] = AudioManager.MODE_NORMAL
                            HookLog.i("AudioMode") { "[OriginalMode] FORCE_MODE_NORMAL ${method.name}${method.parameterTypes.contentToString()} " +
                                    "requested=$requested" }
                        }
                    }
                }
                HookLog.i(TAG) { "[OriginalMode] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
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
                                    HookLog.i(TAG) { "[Strategy] REWRITE_EARPIECE_TO_SPEAKER ${method.name}${method.parameterTypes.contentToString()} " +
                                            "args=${args.contentToString()}" }
                                }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[Strategy] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        if (communicationModeBlocked() && args.getOrNull(1) == false) {
                            val context = findContext(instance)
                            val uid = Binder.getCallingUid()
                            val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
                            val systemApp = isSystemApplication(context, uid)
                            val bluetoothActive = runCatching {
                                context?.getSystemService(AudioManager::class.java)?.communicationDevice
                                    ?.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
                            }.getOrDefault(false)
                            // 系统/特权调用与蓝牙耳机场景放行，避免切回扬声器打断蓝牙音频
                            if (!privileged && !systemApp && !bluetoothActive) {
                                args[1] = true
                                HookLog.i(TAG) { "[Speaker] FORCE_SPEAKER_ON ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}" }
                            } else {
                                HookLog.i(TAG) { "[Speaker] ALLOW off uid=$uid pkg=${resolvePackageName(context, uid)} " +
                                        "privileged=$privileged systemApp=$systemApp bluetooth=$bluetoothActive " +
                                        "${method.name}${method.parameterTypes.contentToString()}" }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[Speaker] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        // setPhoneState 是 framework 内部提交点，正常只有系统进程调用；
                        // 仅防御第三方应用反射调用，系统（Telephony）真实电话不受影响
                        if (communicationModeBlocked() && Binder.getCallingUid() >= Process.FIRST_APPLICATION_UID) {
                            val state = firstInt(args)
                            if (state != null && isCallMode(state)) {
                                result = -1
                                HookLog.i(TAG) { "[PhoneState] BLOCK_HAL_CALL_MODE ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}" }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[PhoneState] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        rewriteCommunicationDevice(instance, method, args)
                    }
                }
                HookLog.i(TAG) { "[CommunicationDevice] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                HookLog.i(TAG) { "[CommunicationDevice] available deviceId=${device.id} type=${device.type} " +
                        "productName=${device.productName} address=${device.address} isSource=${device.isSource}" }
            }
            val resolvedDevice = if (requestedDeviceId == 0) {
                audioManager.communicationDevice
            } else {
                devices.firstOrNull { it.id == requestedDeviceId }
            }
            HookLog.i(TAG) { "[CommunicationDevice] request uid=$uid pkg=$packageName privileged=$privileged " +
                    "systemApp=$systemApp enabled=$enabled requestedDeviceId=$requestedDeviceId " +
                    "resolvedDeviceId=${resolvedDevice?.id} resolvedType=${resolvedDevice?.type} " +
                    "address=${resolvedDevice?.address} isSource=${resolvedDevice?.isSource}" }
            if (!enabled) {
                HookLog.i(TAG) { "[CommunicationDevice] ALLOW switch off uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type}" }
                return
            }
            // 系统/特权调用（SystemUI 设备选择、蓝牙服务）放行，用户主动切换蓝牙/耳机不受干扰
            if (privileged || systemApp) {
                HookLog.i(TAG) { "[CommunicationDevice] ALLOW system/privileged uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type}" }
                return
            }
            // 默认路由（deviceId=0）交给系统决策，蓝牙自动连接后默认路由不再被改写成扬声器
            if (requestedDeviceId == 0) {
                HookLog.i(TAG) { "[CommunicationDevice] ALLOW default routing uid=$uid pkg=$packageName " +
                        "resolvedType=${resolvedDevice?.type}" }
                return
            }
            // 蓝牙耳机是用户明确使用的设备，放行
            if (resolvedDevice?.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                HookLog.i(TAG) { "[CommunicationDevice] ALLOW bluetooth uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type}" }
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
                HookLog.i(TAG) { "[CommunicationDevice] ALLOW already speaker uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId speakerId=${speaker.id}" }
                return
            }
            args[1] = speaker.id
            HookLog.i(TAG) { "[CommunicationDevice] REWRITE_TO_SPEAKER uid=$uid pkg=$packageName " +
                    "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice?.type} " +
                    "speakerId=${speaker.id} speakerType=${speaker.type} " +
                    "speakerAddress=${speaker.address} speakerIsSource=${speaker.isSource}" }
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
        val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
        val systemApp = isSystemApplication(context, uid)
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
            HookLog.i(TAG) { "[CommunicationDevice] ALLOW uid=$uid pkg=$packageName requestedType=${requested.type} enabled=$enabled" }
            return
        }
        // 系统/特权调用与蓝牙设备放行，仅第三方明确请求听筒等设备时强制扬声器
        if (privileged || systemApp || requested.type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
            HookLog.i(TAG) { "[CommunicationDevice] ALLOW system/privileged/bluetooth uid=$uid pkg=$packageName " +
                    "requestedType=${requested.type} privileged=$privileged systemApp=$systemApp" }
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
            HookLog.i(TAG) { "[CommunicationDevice] REWRITE_TO_SPEAKER(INFO) uid=$uid pkg=$packageName " +
                    "requestedType=${requested.type} speakerId=${speaker.id} speakerType=${speaker.type}" }
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
                        if (!communicationModeBlocked()) return@before
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
                            HookLog.i("AudioMode") { "${method.name} args=${args.contentToString()} uid=$uid " +
                                    "pkg=$packageName mode=$mode requestedStream=$requestedStream " +
                                    "streamArg=$streamIndex activeStream=$activeStream " +
                                    "decision=${if (shouldRewrite) "REWRITE_TO_MUSIC" else "ALLOW"}" }
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
                    after {
                        // VoIP 实际增益走 STREAM_VOICE_CALL；媒体音量调整后同步通话音量索引，
                        // 保证 App 通话中音量键立即生效（循环由 syncingVolumeAlias 保护）
                        if (communicationModeBlocked() && !syncingVolumeAlias.get()) {
                            syncVoiceCallToMusic(instance)
                        }
                    }
                }
                HookLog.i(TAG) { "[Audio] installed ${method.name}${method.parameterTypes.contentToString()}" }
            }
        }.onFailure { HookLog.e(TAG, "[Audio] failed to hook adjustStreamVolume family", it) }
    }

    /**
     * 通话/蓝牙 SCO 音量流别名到媒体流：get/set/max 读写全部映射 STREAM_MUSIC。
     * VoIP（USAGE_VOICE_COMMUNICATION）的音量索引在 AudioService 中绑定 STREAM_VOICE_CALL，
     * 而强制普通模式后音量键调整的是 STREAM_MUSIC；不别名的话 App 通话音量永远无效。
     */
    private fun hookVolumeAliasToMusic(clazz: Class<*>) {
        runCatching {
            val candidates = allMethods(clazz)
                .filter { method ->
                    (method.name == "getStreamVolume" || method.name == "getStreamMaxVolume" ||
                        method.name.startsWith("setStreamVolume")) &&
                        method.parameterTypes.isNotEmpty() &&
                        method.parameterTypes[0] == Int::class.javaPrimitiveType
                }
                .distinctBy(Method::toGenericString)
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[VolumeAlias] candidate not found: getStreamVolume/setStreamVolume family")
                return
            }
            candidates.forEach { method ->
                method.isAccessible = true
                method.hook {
                    before {
                        if (!communicationModeBlocked()) return@before
                        if (communicationModeBlocked() && !syncingVolumeAlias.get()) {
                            val stream = args.getOrNull(0) as? Int
                            if (stream != null && isCallVolumeStream(stream)) {
                                val uid = Binder.getCallingUid()
                                val index = args.getOrNull(1) as? Int
                                if (uid < Process.FIRST_APPLICATION_UID) {
                                    // 系统内部调用（音量限制模块的 100ms 校正循环、系统恢复音量等）一律放行：
                                    // 别名会把 VOICE_CALL/SCO 设置放大成媒体音量，配合音量限制时媒体音量会被
                                    // 突然抬到限制值（微信语音/高德导航复现）；放行也避免 get/getMax 别名污染
                                    // 其他模块对真实通话流最大值的缓存。
                                    HookLog.i("AudioMode") { "[VolumeAlias] ALLOW_SYSTEM_INTERNAL " +
                                            "${method.name}${method.parameterTypes.contentToString()} " +
                                            "stream=$stream uid=$uid" }
                                } else if (index == null) {
                                    // App 读取通话/SCO 音量：别名到媒体流，让 App 音量 UI 显示媒体音量
                                    args[0] = AudioManager.STREAM_MUSIC
                                    HookLog.i("AudioMode") { "[VolumeAlias] READ_AS_MUSIC " +
                                            "${method.name}${method.parameterTypes.contentToString()} " +
                                            "stream=$stream uid=$uid" }
                                } else {
                                    // App 显式设置通话/SCO 流：通信音频强制按媒体音量播放。
                                    // 旧逻辑把通话档位等比放大成媒体档位（如 setStreamVolume(VOICE_CALL, 9)
                                    // -> setStreamVolume(MUSIC, 160)），配合音量限制模块会把媒体音量突然
                                    // 抬到限制值；现在只把通话流同步到当前媒体音量的比例，绝不改写媒体音量。
                                    val audioManager = findContext(instance)
                                        ?.getSystemService(AudioManager::class.java)
                                    if (audioManager == null) {
                                        HookLog.w(
                                            TAG,
                                            "[VolumeAlias] AudioManager unavailable; ALLOW set stream=$stream uid=$uid"
                                        )
                                    } else {
                                        if (cachedMusicMaxVolume <= 0) {
                                            cachedMusicMaxVolume = runCatching {
                                                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                                            }.getOrDefault(0)
                                        }
                                        val fromMax = when (stream) {
                                            AudioManager.STREAM_VOICE_CALL -> {
                                                if (cachedVoiceCallMaxVolume <= 0) {
                                                    cachedVoiceCallMaxVolume = runCatching {
                                                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
                                                    }.getOrDefault(0)
                                                }
                                                if (cachedVoiceCallMaxVolume > 0) cachedVoiceCallMaxVolume else 9
                                            }
                                            else -> {
                                                if (cachedScoMaxVolume <= 0) {
                                                    cachedScoMaxVolume = runCatching {
                                                        audioManager.getStreamMaxVolume(6)
                                                    }.getOrDefault(0)
                                                }
                                                if (cachedScoMaxVolume > 0) cachedScoMaxVolume else 15
                                            }
                                        }
                                        val currentMusic = runCatching {
                                            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                                        }.getOrNull()
                                        if (cachedMusicMaxVolume <= 0 || currentMusic == null) {
                                            // 媒体音量状态未知时 fail-open：不放大也不改写
                                            HookLog.w(
                                                TAG,
                                                "[VolumeAlias] music state unknown; ALLOW set stream=$stream index=$index uid=$uid"
                                            )
                                        } else {
                                            val expected =
                                                scaleIndex(currentMusic, 0, cachedMusicMaxVolume, 1, fromMax)
                                            args[1] = expected
                                            HookLog.i("AudioMode") { "[VolumeAlias] CLAMP_TO_MEDIA " +
                                                    "${method.name}${method.parameterTypes.contentToString()} " +
                                                    "stream=$stream requested=$index -> $expected music=$currentMusic uid=$uid" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[VolumeAlias] installed ${method.name}${method.parameterTypes.contentToString()}" }
            }
        }.onFailure { HookLog.e(TAG, "[VolumeAlias] failed to hook volume alias family", it) }
    }

    private fun isCallVolumeStream(stream: Int): Boolean =
        stream == AudioManager.STREAM_VOICE_CALL || stream == 6 // AudioManager.STREAM_BLUETOOTH_SCO = 6

    /** 把一个音量索引按范围等比映射到另一个范围（避免 clamp 到目标流最大档导致爆音）。 */
    private fun scaleIndex(index: Int, fromMin: Int, fromMax: Int, toMin: Int, toMax: Int): Int =
        io.github.fairyxh.ZhangSystemHook.core.VolumeScale.map(index, fromMin, fromMax, toMin, toMax)

    /** 把媒体音量按比例同步到 STREAM_VOICE_CALL / BLUETOOTH_SCO，让 VoIP 输出增益跟随音量键且不爆音。 */
    private fun syncVoiceCallToMusic(service: Any?) {
        val context = findContext(service) ?: return
        val audioManager = runCatching { context.getSystemService(AudioManager::class.java) }.getOrNull() ?: return
        if (!syncingVolumeAlias.compareAndSet(false, true)) return
        try {
            val musicIndex = runCatching { audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) }.getOrNull()
                ?: return
            // syncingVolumeAlias 保护下读取的是真实 max，不会被别名 hook 改写
            if (cachedMusicMaxVolume <= 0) {
                cachedMusicMaxVolume =
                    runCatching { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }.getOrDefault(0)
            }
            if (cachedVoiceCallMaxVolume <= 0) {
                cachedVoiceCallMaxVolume =
                    runCatching { audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL) }.getOrDefault(0)
            }
            if (cachedScoMaxVolume <= 0) {
                cachedScoMaxVolume = runCatching { audioManager.getStreamMaxVolume(6) }.getOrDefault(0)
            }
            if (cachedMusicMaxVolume <= 0 || cachedVoiceCallMaxVolume <= 0) return
            val voiceCallIndex = scaleIndex(musicIndex, 0, cachedMusicMaxVolume, 1, cachedVoiceCallMaxVolume)
            runCatching { audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voiceCallIndex, 0) }
                .onFailure { HookLog.e(TAG, "[VolumeAlias] sync voiceCall failed", it) }
            var scoIndex = -1
            if (cachedScoMaxVolume > 0) {
                scoIndex = scaleIndex(musicIndex, 0, cachedMusicMaxVolume, 1, cachedScoMaxVolume)
                runCatching { audioManager.setStreamVolume(6, scoIndex, 0) }
                    .onFailure { HookLog.e(TAG, "[VolumeAlias] sync sco failed", it) }
            }
            HookLog.i(TAG) { "[VolumeAlias] SYNC music=$musicIndex voiceCall=$voiceCallIndex sco=$scoIndex" }
        } finally {
            syncingVolumeAlias.set(false)
        }
    }

    /**
     * AudioSystem 隐藏常量：听筒输出设备 / 设备状态。
     */
    private const val DEVICE_OUT_EARPIECE = 0x00000001
    private const val DEVICE_OUT_SPEAKER = 0x00000002
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
        // Android 14+ 新签名 setDeviceConnectionState(AudioDeviceAttributes, int, int) 的设备类型
        // 在 attributes 中，旧签名 (int, int, ...) 参数 0 才是设备类型；不能用 firstInt 以免把
        // state=AVAILABLE(1) 误判成听筒导致蓝牙 SCO/A2DP 连接被强制断开
        val attributesClass = runCatching {
            "android.media.AudioDeviceAttributes".toClass()
        }.getOrNull()
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
                        if (!communicationModeBlocked()) return@before
                        if (communicationModeBlocked()) {
                            val device = when {
                                method.parameterTypes.isNotEmpty() &&
                                    method.parameterTypes[0] == Int::class.javaPrimitiveType ->
                                    args.getOrNull(0) as? Int
                                attributesClass != null && method.parameterTypes.isNotEmpty() &&
                                    method.parameterTypes[0] == attributesClass ->
                                    args.getOrNull(0)?.let { invokeInt(it, "getType") }
                                else -> null
                            }
                            val state = if (method.parameterTypes.size >= 2 &&
                                method.parameterTypes[1] == Int::class.javaPrimitiveType
                            ) {
                                args.getOrNull(1) as? Int
                            } else null
                            if (device == DEVICE_OUT_EARPIECE && state == DEVICE_STATE_AVAILABLE) {
                                args[1] = DEVICE_STATE_UNAVAILABLE
                                HookLog.i(TAG) { "[Earpiece] REWRITE_EARPIECE_OFF ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}" }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                        if (!communicationModeBlocked()) return@before
                        if (communicationModeBlocked()) {
                            val device = firstInt(args)
                            if (device == DEVICE_OUT_EARPIECE) {
                                result = DEVICE_STATE_UNAVAILABLE
                                HookLog.i(TAG) { "[Earpiece] REPORT_UNAVAILABLE ${method.name}${method.parameterTypes.contentToString()} " +
                                        "args=${args.contentToString()}" }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
                                HookLog.i(TAG) { "[Earpiece] FILTER_DEVICE_MASK ${method.name}${method.parameterTypes.contentToString()} " +
                                        "devices=$devices filtered=$filtered" }
                            }
                        }
                    }
                }
                HookLog.i(TAG) { "[Earpiece] installed ${method.name}${method.parameterTypes.contentToString()}" }
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
            HookLog.i(TAG) { "[Earpiece] disconnected earpiece via ${method.name}" }
        }.onFailure {
            HookLog.e(TAG, "[Earpiece] disconnect failed", it)
        }
    }

    private fun allMethods(clazz: Class<*>): List<Method> = HookRuntime.methods(clazz)

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
            // 蓝牙 SCO 是蓝牙耳机的正常音频链路，必须放行；禁止的是通话/通信模式本身
            "startBluetoothSco", "startBluetoothScoVirtualCall" -> false
            "stopBluetoothSco" -> false
            else -> false
        }
        // 系统/特权调用（Telephony、SystemUI、蓝牙服务）放行，避免误伤真实电话与用户主动切换
        val systemCall = privileged || systemApp
        val block = enabled && acquisition && !systemCall
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

    /** 当前通话/通信模式的持有者 uid；无法判定时返回 null（调用方需 fail-open）。 */
    private fun currentModeOwnerUid(service: Any?): Int? = runCatching {
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
            invokeInt(handler, "getUid")?.let { return it }
            readIntField(handler, "mUid")?.let { return it }
            return null
        }
        null
    }.getOrNull()

    /** 通话/通信模式是否由第三方 app（非系统）持有。 */
    private fun isAppOwnedCallMode(service: Any?): Boolean {
        val uid = currentModeOwnerUid(service) ?: return false
        return uid >= Process.FIRST_APPLICATION_UID
    }

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
