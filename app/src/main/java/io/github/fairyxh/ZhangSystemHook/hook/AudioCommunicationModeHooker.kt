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

/** 仅在 system_server 的 AudioService Binder 实现中限制普通应用获取通信模式/路由。 */
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
        hookCommunicationDevice(audioService)
        hookMethods(audioService, "startBluetoothSco")
        hookMethods(audioService, "startBluetoothScoVirtualCall")
        hookMethods(audioService, "stopBluetoothSco")
        hookVolumeSelection(audioService)
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
                            val context = findContext(instance)
                            val callState = readCallState(context)
                            if (callState == null || callState == TelephonyManager.CALL_STATE_IDLE) {
                                args[modeIndex] = AudioManager.MODE_NORMAL
                                HookLog.i(
                                    "AudioMode",
                                    "[ModeCommit] FORCE_MODE_NORMAL ${method.name}${method.parameterTypes.contentToString()} " +
                                        "requested=$requested callState=$callState"
                                )
                            }
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

    /** 兜底：mMode 已进入通话/通信模式且无真实蜂窝通话时，强行切回 MODE_NORMAL。 */
    private fun forceNormalIfNeeded(audioService: Any?, context: Context?) {
        if (!communicationModeBlocked()) return
        val callState = readCallState(context)
        if (callState != null && callState != TelephonyManager.CALL_STATE_IDLE) return
        val mode = readMode(audioService) ?: return
        if (!isCallMode(mode)) return
        if (!forceNormalPending.compareAndSet(false, true)) return
        HookLog.i(TAG, "[ForceNormal] mode=$mode callState=$callState scheduling setMode(MODE_NORMAL)")
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

    private fun hookCommunicationDevice(clazz: Class<*>) {
        val candidates = runCatching {
            allMethods(clazz)
                .filter { method ->
                    method.name == "setCommunicationDevice" &&
                        method.parameterTypes.size == 2 &&
                        IBinder::class.java.isAssignableFrom(method.parameterTypes[0]) &&
                        method.parameterTypes[1] == Int::class.javaPrimitiveType
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
                        rewriteCommunicationDevice(instance, args)
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

    private fun rewriteCommunicationDevice(audioService: Any?, args: Array<Any?>) {
        val uid = Binder.getCallingUid()
        val context = findContext(audioService)
        val packageName = resolvePackageName(context, uid)
        val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
        val systemApp = isSystemApplication(context, uid)
        val nonSystemApp = uid >= Process.FIRST_APPLICATION_UID && !privileged && !systemApp
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
            if (resolvedDevice == null) {
                HookLog.w(
                    TAG,
                    "[CommunicationDevice] ALLOW device unresolved uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=null selectedSpeakerId=null " +
                        "enabled=$enabled nonSystemApp=$nonSystemApp"
                )
                return
            }
            if (!enabled || !nonSystemApp || resolvedDevice.type != AudioDeviceInfo.TYPE_BUILTIN_EARPIECE) {
                HookLog.i(
                    TAG,
                    "[CommunicationDevice] ALLOW uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice.type} selectedSpeakerId=null"
                )
                return
            }
            val speaker = devices.firstOrNull { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
            if (speaker == null) {
                HookLog.w(
                    TAG,
                    "[CommunicationDevice] ALLOW speaker unavailable uid=$uid pkg=$packageName " +
                        "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice.type} selectedSpeakerId=null"
                )
                return
            }
            args[1] = speaker.id
            HookLog.i(
                TAG,
                "[CommunicationDevice] REWRITE_EARPIECE_TO_SPEAKER uid=$uid pkg=$packageName " +
                    "requestedDeviceId=$requestedDeviceId resolvedType=${resolvedDevice.type} " +
                    "selectedSpeakerId=${speaker.id} speakerType=${speaker.type} " +
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

    private fun hookVolumeSelection(clazz: Class<*>) {
        runCatching {
            val candidates = allMethods(clazz)
                .filter { it.name == "adjustSuggestedStreamVolume" }
                .distinctBy(Method::toGenericString)
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[Audio] candidate not found: ${clazz.name}.adjustSuggestedStreamVolume")
                return
            }
            candidates.forEach { method: Method ->
                    method.isAccessible = true
                    method.hook {
                        before {
                            val uid = Binder.getCallingUid()
                            val context = findContext(instance)
                            val packageName = resolvePackageName(context, uid)
                            val mode = readMode(instance)
                            val suggestedStreamIndex = suggestedStreamIndex(method)
                            val suggestedStream = suggestedStreamIndex?.let { args.getOrNull(it) as? Int }
                            val activeStream = invokeInt(
                                instance,
                                "getActiveStreamType",
                                AudioManager.USE_DEFAULT_STREAM_TYPE
                            )
                            val enabled = runCatching {
                                ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE)
                            }.getOrDefault(false)
                            val communicationMode = mode != null && isCallMode(mode)
                            val voiceCallSelected = activeStream == AudioManager.STREAM_VOICE_CALL
                            val shouldRewrite = enabled && (communicationMode || voiceCallSelected) &&
                                suggestedStreamIndex != null
                            HookLog.i(
                                "AudioMode",
                                "adjustSuggestedStreamVolume args=${args.contentToString()} uid=$uid " +
                                    "pkg=$packageName mode=$mode requestedStream=$suggestedStream " +
                                    "streamArg=$suggestedStreamIndex activeStream=$activeStream " +
                                    "decision=${if (shouldRewrite) "REWRITE_TO_MUSIC" else "ALLOW"}"
                            )
                            if (shouldRewrite && suggestedStream != AudioManager.STREAM_MUSIC) {
                                args[suggestedStreamIndex] = AudioManager.STREAM_MUSIC
                            }
                        }
                    }
                    HookLog.i(TAG, "[Audio] installed adjustSuggestedStreamVolume${method.parameterTypes.contentToString()}")
                }
        }.onFailure { HookLog.e(TAG, "[Audio] failed to hook adjustSuggestedStreamVolume", it) }
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

    private fun suggestedStreamIndex(method: Method): Int? {
        val parameterTypes = method.parameterTypes
        val intType = Int::class.javaPrimitiveType
        return if (parameterTypes.size >= 2 && parameterTypes[0] == intType && parameterTypes[1] == intType) {
            1
        } else {
            null
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
            "setMode" -> firstInt(args) == AudioManager.MODE_IN_CALL ||
                firstInt(args) == AudioManager.MODE_IN_COMMUNICATION
            "startBluetoothSco", "startBluetoothScoVirtualCall" -> true
            "stopBluetoothSco" -> false
            else -> false
        }
        val block = enabled && acquisition && when (methodName) {
            "setMode" -> shouldBlockCallMode(context, uid, privileged, systemApp)
            else -> uid >= Process.FIRST_APPLICATION_UID && !privileged && !systemApp
        }
        return Decision(uid, packageName, enabled, privileged, block, describeState(audioService, context))
    }

    private fun firstInt(args: Array<Any?>): Int? = args.firstOrNull { it is Int } as? Int

    /**
     * 新策略：开关开启时，除真实蜂窝通话进行中以外，一律禁止进入通话/通信模式。
     * 这样可拦截经 Telecom(uid 1001)/系统应用/特权应用代申请的第三方 VoIP 通话模式；
     * Telephony 状态不可读时退回旧规则（仅拦截普通第三方），避免误伤真实通话。
     */
    private fun shouldBlockCallMode(context: Context?, uid: Int, privileged: Boolean, systemApp: Boolean): Boolean {
        val callState = readCallState(context)
        if (callState == null) {
            return uid >= Process.FIRST_APPLICATION_UID && !privileged && !systemApp
        }
        return callState == TelephonyManager.CALL_STATE_IDLE
    }

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
