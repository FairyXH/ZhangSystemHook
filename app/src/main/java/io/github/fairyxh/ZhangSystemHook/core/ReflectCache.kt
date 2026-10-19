package io.github.fairyxh.ZhangSystemHook.core

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

object ReflectCache {
    private data class MethodKey(val type: Class<*>, val name: String, val arguments: List<Class<*>>)
    private val fields = ConcurrentHashMap<Class<*>, Map<String, Field>>()
    private val methods = ConcurrentHashMap<MethodKey, Optional<Method>>()
    fun field(receiver: Any?, name: String): Field? {
        val type = receiver?.javaClass ?: return null
        return fields.computeIfAbsent(type) {
            val result = LinkedHashMap<String, Field>()
            generateSequence(it) { c -> c.superclass }.forEach { c ->
                c.declaredFields.forEach { field -> runCatching {
                    if (field.name !in result) { field.isAccessible = true; result[field.name] = field }
                } }
            }
            result
        }[name]
    }
    fun method(receiver: Any?, name: String, vararg arguments: Any): Method? {
        val type = receiver?.javaClass ?: return null
        val key = MethodKey(type, name, arguments.map { it.javaClass })
        return methods.computeIfAbsent(key) {
            Optional.ofNullable(HookRuntime.methods(type).firstOrNull { candidate ->
                candidate.name == name && candidate.parameterTypes.size == arguments.size &&
                    candidate.parameterTypes.zip(arguments).all { (parameter, argument) ->
                        parameter.isInstance(argument) ||
                            (parameter == Int::class.javaPrimitiveType && argument is Int) ||
                            (parameter == Boolean::class.javaPrimitiveType && argument is Boolean)
                    }
            }?.apply { isAccessible = true })
        }.orElse(null)
    }
    fun invoke(receiver: Any?, name: String, vararg arguments: Any): Any? =
        method(receiver, name, *arguments)?.invoke(receiver, *arguments)
}
