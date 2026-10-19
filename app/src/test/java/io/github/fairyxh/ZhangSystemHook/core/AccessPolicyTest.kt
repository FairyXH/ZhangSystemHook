package io.github.fairyxh.ZhangSystemHook.core

import org.junit.Assert.*
import org.junit.Test

class AccessPolicyTest {
    private fun allow(packages: List<String>, selected: Set<String> = emptySet(), system: Boolean = false,
                      whitelist: Boolean = false, blockSystem: Boolean = false) =
        AccessPolicy.allows(packages, "module", selected, system, whitelist, blockSystem)
    @Test fun userApplicationsUseWhitelist() {
        assertFalse(allow(listOf("app")))
        assertTrue(allow(listOf("app"), setOf("app")))
    }
    @Test fun systemApplicationsUseBlacklistByDefault() {
        assertTrue(allow(listOf("system.app"), system = true))
        assertFalse(allow(listOf("system.app"), setOf("system.app"), system = true))
    }
    @Test fun systemWhitelistHonorsKernelProtection() {
        assertTrue(allow(listOf("android.component"), system = true, whitelist = true))
        assertFalse(allow(listOf("android.component"), system = true, whitelist = true, blockSystem = true))
        assertTrue(allow(listOf("android.component"), setOf("android.component"), true, true, true))
    }
    @Test fun moduleSelfAndUnknownCallersRetainSpecialRules() {
        assertFalse(allow(listOf("module"), setOf("module")))
        assertTrue(allow(emptyList()))
        assertTrue(allow(listOf("android")))
    }
    @Test fun sharedUidNeedsEveryUserPackageAllowed() {
        assertFalse(allow(listOf("a", "b"), setOf("a")))
        assertTrue(allow(listOf("a", "b"), setOf("a", "b")))
    }
}
