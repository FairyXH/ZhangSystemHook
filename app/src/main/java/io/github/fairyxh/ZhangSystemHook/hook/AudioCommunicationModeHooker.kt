package io.github.fairyxh.ZhangSystemHook.hook

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Binder
import android.os.Process
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.lang.reflect.Method

/** 仅在 system_server 的 AudioService Binder 实现中限制普通应用获取通信模式/路由。 */
object AudioCommunicationModeHooker : YukiBaseHooker() {
    override fun onHook() {
        val audioService = "com.android.server.audio.AudioService".toClass()
        hookMethods(audioService, "setMode")
        hookMethods(audioService, "setCommunicationDevice")
        hookMethods(audioService, "startBluetoothSco")
        hookMethods(audioService, "startBluetoothScoVirtualCall")
        hookMethods(audioService, "stopBluetoothSco")
        hookVolumeSelection(audioService)
    }

    private fun hookMethods(clazz: Class<*>, methodName: String) {
        runCatching {
            clazz.declaredMethods.filter { it.name == methodName }.forEach { method: Method ->
                method.isAccessible = true
                method.hook {
                    before {
                        val decision = decide(methodName, instance, args)
                        HookLog.i(
                            "AudioMode",
                            "$methodName${method.parameterTypes.contentToString()} uid=${decision.uid} " +
                                "pkg=${decision.packageName} args=${args.contentToString()} " +
                                "enabled=${decision.enabled} privileged=${decision.privileged} " +
                                "state=${decision.state} " +
                                "decision=${if (decision.block) "BLOCK" else "ALLOW"}"
                        )
                        if (decision.block) result = defaultResult(method.returnType)
                    }
                }
            }
        }.onFailure { HookLog.e("AudioMode", "failed to hook $methodName", it) }
    }

    private fun hookVolumeSelection(clazz: Class<*>) {
        runCatching {
            clazz.declaredMethods
                .filter { it.name == "adjustSuggestedStreamVolume" }
                .forEach { method: Method ->
                    method.isAccessible = true
                    method.hook {
                        before {
                            val uid = Binder.getCallingUid()
                            val context = findContext(instance)
                            val packageName = resolvePackageName(context, uid)
                            val mode = readIntField(instance, "mMode")
                            val suggestedStream = args.getOrNull(0) as? Int
                            val activeStream = invokeInt(
                                instance,
                                "getActiveStreamType",
                                AudioManager.USE_DEFAULT_STREAM_TYPE
                            )
                            val forceTestIdentity = packageName == BuildConfig.APPLICATION_ID
                            val shouldRewrite = ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE) &&
                                (forceTestIdentity || (uid >= Process.FIRST_APPLICATION_UID && !hasRoutingPrivilege(context))) &&
                                mode == AudioManager.MODE_NORMAL &&
                                activeStream == AudioManager.STREAM_VOICE_CALL
                            HookLog.i(
                                "AudioMode",
                                "adjustSuggestedStreamVolume args=${args.contentToString()} uid=$uid " +
                                    "pkg=$packageName mode=$mode requestedStream=$suggestedStream activeStream=$activeStream " +
                                    "decision=${if (shouldRewrite) "REWRITE_TO_MUSIC" else "ALLOW"}"
                            )
                            if (shouldRewrite && suggestedStream != AudioManager.STREAM_MUSIC) {
                                args[0] = AudioManager.STREAM_MUSIC
                            }
                        }
                    }
                }
        }.onFailure { HookLog.e("AudioMode", "failed to hook adjustSuggestedStreamVolume", it) }
    }

    private fun decide(methodName: String, audioService: Any?, args: Array<Any?>): Decision {
        val uid = Binder.getCallingUid()
        val enabled = runCatching {
            ConfigData.getBoolean(ConfigData.BLOCK_THIRD_PARTY_COMMUNICATION_MODE)
        }.getOrDefault(false)
        val context = findContext(audioService)
        val privileged = context?.let { hasRoutingPrivilege(it) } ?: true
        val packageName = resolvePackageName(context, uid)
        val forceTestIdentity = packageName == BuildConfig.APPLICATION_ID
        val acquisition = when (methodName) {
            "setMode" -> firstInt(args) == AudioManager.MODE_IN_CALL ||
                firstInt(args) == AudioManager.MODE_IN_COMMUNICATION
            "setCommunicationDevice" -> lastInt(args)?.let { it > 0 } == true
            "startBluetoothSco", "startBluetoothScoVirtualCall" -> true
            "stopBluetoothSco" -> false
            else -> false
        }
        val block = enabled && acquisition &&
            (forceTestIdentity || (uid >= Process.FIRST_APPLICATION_UID && !privileged))
        return Decision(uid, packageName, enabled, privileged, block, describeState(audioService))
    }

    private fun firstInt(args: Array<Any?>): Int? = args.firstOrNull { it is Int } as? Int

    private fun lastInt(args: Array<Any?>): Int? = args.lastOrNull { it is Int } as? Int

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

    private fun describeState(service: Any?): String = runCatching {
        val mode = invokeInt(service, "getMode") ?: readIntField(service, "mMode")
        val activeStream = invokeInt(service, "getActiveStreamType", AudioManager.USE_DEFAULT_STREAM_TYPE)
        "mode=${mode ?: "?"},activeStream=${activeStream ?: "?"}"
    }.getOrDefault("mode=?,activeStream=?")

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
        field.get(service) as? Int
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
