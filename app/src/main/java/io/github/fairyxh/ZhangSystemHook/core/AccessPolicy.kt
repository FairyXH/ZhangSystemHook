package io.github.fairyxh.ZhangSystemHook.core

object AccessPolicy {
    fun allows(packages: List<String>, modulePackage: String, selected: Set<String>,
               system: Boolean, whitelist: Boolean, blockSystem: Boolean): Boolean {
        if (packages.isEmpty() || "android" in packages) return true
        if (modulePackage in packages) return false
        return packages.all { name ->
            when {
                whitelist && system && name.startsWith("android.") && !blockSystem -> true
                whitelist -> name in selected
                system -> name !in selected
                else -> name in selected
            }
        }
    }
}
