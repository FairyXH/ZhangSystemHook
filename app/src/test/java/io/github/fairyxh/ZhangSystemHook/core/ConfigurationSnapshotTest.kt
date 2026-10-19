package io.github.fairyxh.ZhangSystemHook.core

import android.content.SharedPreferences
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import org.junit.Assert.*
import org.junit.Test

class ConfigurationSnapshotTest {
    class Preferences : SharedPreferences {
        val values = mutableMapOf<String, Any>()
        private val listeners = mutableSetOf<SharedPreferences.OnSharedPreferenceChangeListener>()
        var reads = 0
        override fun getAll(): Map<String, *> { reads++; return values.toMap() }
        fun update(key: String, value: Any) { values[key] = value; listeners.toList().forEach { it.onSharedPreferenceChanged(this, key) } }
        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) { listeners.add(listener) }
        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) { listeners.remove(listener) }
        override fun getString(key: String?, defValue: String?) = values[key] as? String ?: defValue
        override fun getStringSet(key: String?, defValues: Set<String>?) = values[key] as? Set<String> ?: defValues
        override fun getInt(key: String?, defValue: Int) = values[key] as? Int ?: defValue
        override fun getLong(key: String?, defValue: Long) = values[key] as? Long ?: defValue
        override fun getFloat(key: String?, defValue: Float) = values[key] as? Float ?: defValue
        override fun getBoolean(key: String?, defValue: Boolean) = values[key] as? Boolean ?: defValue
        override fun contains(key: String?) = key in values
        override fun edit(): SharedPreferences.Editor = error("Target preferences are read-only")
    }
    @Test fun remoteChangesInvalidateSnapshotsWithoutPolling() {
        val preferences = Preferences()
        ConfigData.bind(preferences)
        assertFalse(ConfigData.flag("accessibility_hook_enable"))
        preferences.update("accessibility_hook_enable", true)
        assertTrue(ConfigData.flag("accessibility_hook_enable"))
        preferences.update("_block_apps", setOf("app"))
        assertEquals(setOf("app"), ConfigData.blockApps.data)
    }
    @Test fun hotReadsDoNotReadPreferencesOrAllocatePackageCopies() {
        val preferences = Preferences()
        preferences.values["_block_apps"] = setOf("a", "b")
        ConfigData.bind(preferences)
        val reads = preferences.reads
        val packages = ConfigData.blockApps.data
        repeat(10000) { ConfigData.flag("accessibility_hook_enable"); assertSame(packages, ConfigData.blockApps.data) }
        assertEquals(reads, preferences.reads)
    }
    @Test fun rebindingUnsubscribesOldPreferenceSource() {
        val old = Preferences()
        val current = Preferences()
        ConfigData.bind(old); ConfigData.bind(current)
        old.update("accessibility_hook_enable", true)
        assertFalse(ConfigData.flag("accessibility_hook_enable"))
    }
}
