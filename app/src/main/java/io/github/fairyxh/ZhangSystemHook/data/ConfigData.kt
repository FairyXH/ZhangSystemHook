package io.github.fairyxh.ZhangSystemHook.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Xml
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors

object ConfigData {
    const val GROUP = "zhang_system_hook"
    val ENABLE_MODULE = PreferenceKey("_enable_module", true)
    val BLOCK_THIRD_PARTY_COMMUNICATION_MODE = PreferenceKey("block_third_party_communication_mode", false)
    val BLOCK_SCREEN_CAPTURE_DETECTION = PreferenceKey("block_screen_capture_detection", false)
    val knownKeys = setOf("_enable_module", "accessibility_hook_enable", "high_performance_mode",
        "allow_client_method", "system_app_whitelist", "block_system", "_block_apps", "show_notice",
        "MothodMonitor_SW", "block_screen_capture_detection", "enable_android14_screenshot_blocker",
        "enable_enhanced_screenshot_blocker", "block_third_party_communication_mode", "dpm_enabled", "launcher_enabled",
        "extended_filter_enabled")
    @Volatile private var snapshot: Map<String, *> = emptyMap<String, Any>()
    @Volatile private var packageSnapshot: Set<String> = setOf(BuildConfig.APPLICATION_ID)
    private var prefs: SharedPreferences? = null
    private var local: SharedPreferences? = null
    private var remote: SharedPreferences? = null
    private val writer = Executors.newSingleThreadExecutor { task -> Thread(task, "ConfigWriter").apply { isDaemon = true } }
    @Volatile private var pending: Map<String, Any> = emptyMap()
    private val listeners = CopyOnWriteArraySet<() -> Unit>()
    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener { source, _ ->
        if (source === prefs) refresh()
    }
    val blockApps = PackageSet()
    var isEnableModule: Boolean
        get() = getBoolean(ENABLE_MODULE)
        set(value) = putBoolean(ENABLE_MODULE, value)

    fun init(context: Context) {
        val app = context.applicationContext
        local = app.getSharedPreferences(GROUP, Context.MODE_PRIVATE)
        migrateLegacy(app)
        bind(local!!)
    }

    @Synchronized fun bind(source: SharedPreferences) {
        prefs?.unregisterOnSharedPreferenceChangeListener(preferenceListener)
        prefs = source
        source.registerOnSharedPreferenceChangeListener(preferenceListener)
        refresh()
    }

    @Synchronized fun connect(source: SharedPreferences) {
        val mirror = local ?: return
        val dirty = mirror.getStringSet("_pending_keys", emptySet()).orEmpty()
        val remoteEmpty = source.all.isEmpty()
        val editor = source.edit()
        mirror.all.filterKeys { it in knownKeys && (remoteEmpty || it in dirty) }
            .forEach { (key, value) -> put(editor, key, value) }
        check(editor.commit()) { "Remote preferences commit failed" }
        remote = source
        pending = emptyMap()
        mirror.edit().remove("_pending_keys").apply()
        bind(source)
        val copy = mirror.edit()
        source.all.filterKeys { it in knownKeys }.forEach { (key, value) -> put(copy, key, value) }
        copy.apply()
    }

    @Synchronized fun disconnect() { remote = null; local?.let(::bind) }
    fun observe(listener: () -> Unit) { listeners.add(listener) }
    fun removeObserver(listener: () -> Unit) { listeners.remove(listener) }
    fun refresh() {
        snapshot = (prefs?.all?.toMap() ?: emptyMap<String, Any>()) + pending
        packageSnapshot = (snapshot["_block_apps"] as? Set<*>)?.filterIsInstance<String>()?.toSet()
            ?: setOf(BuildConfig.APPLICATION_ID)
        listeners.forEach { it() }
    }
    fun flag(key: String, default: Boolean = false): Boolean = snapshot[key] as? Boolean ?: default
    fun getBoolean(key: PreferenceKey<Boolean>): Boolean = flag(key.key, key.default)
    fun putBoolean(key: PreferenceKey<Boolean>, value: Boolean) = set(key.key, value)
    fun setFlag(key: String, value: Boolean) = set(key, value)
    fun values(): Map<String, *> = snapshot.filterKeys { it in knownKeys }

    @Synchronized private fun set(key: String, value: Any) {
        val mirror = local ?: error("Preferences are read-only in target processes")
        val dirty = mirror.getStringSet("_pending_keys", emptySet()).orEmpty() + key
        val localEditor = mirror.edit()
        put(localEditor, key, value)
        localEditor.putStringSet("_pending_keys", dirty).apply()
        pending = pending + (key to value)
        refresh()
        writer.execute { flush() }
    }

    private fun flush() {
        val batch = synchronized(this) { Triple(remote, local, pending) }
        val destination = batch.first ?: return
        val mirror = batch.second ?: return
        val updates = batch.third
        if (updates.isEmpty()) return
        runCatching {
            val editor = destination.edit()
            updates.forEach { (key, value) -> put(editor, key, value) }
            check(editor.commit()) { "Remote preferences commit failed" }
            synchronized(this) {
                if (remote !== destination) return
                val completed = updates.keys.filter { pending[it] == updates[it] }.toSet()
                pending = pending - completed
                val dirty = mirror.getStringSet("_pending_keys", emptySet()).orEmpty()
                mirror.edit().putStringSet("_pending_keys", dirty - completed).apply()
                refresh()
            }
        }.onFailure { disconnect() }
    }

    private fun put(editor: SharedPreferences.Editor, key: String, value: Any?) {
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Set<*> -> editor.putStringSet(key, value.filterIsInstance<String>().toSet())
        }
    }

    private fun migrateLegacy(context: Context) {
        val target = local ?: return
        if (target.getBoolean("_legacy_migrated", false)) return
        val existing = target.all.keys
        val editor = target.edit()
        val migrated = mutableSetOf<String>()
        File(context.applicationInfo.dataDir, "shared_prefs").listFiles()
            ?.filter { it.extension == "xml" && it.nameWithoutExtension != GROUP }
            ?.sortedBy { it.lastModified() }?.forEach { file ->
                context.getSharedPreferences(file.nameWithoutExtension, Context.MODE_PRIVATE).all
                    .filterKeys { it in knownKeys && it !in existing }.forEach { (key, value) ->
                        put(editor, key, value); migrated.add(key)
                    }
            }
        editor.putStringSet("_pending_keys", target.getStringSet("_pending_keys", emptySet()).orEmpty() + migrated)
            .putBoolean("_legacy_migrated", true).commit()
    }

    class PackageSet {
        val data: Set<String> get() = packageSnapshot
        fun contains(packageName: String) = packageName in data
        fun switch(packageName: String) { set("_block_apps", if (contains(packageName)) data - packageName else data + packageName) }
        fun replaceAll(packages: Set<String>) = set("_block_apps", packages.toSet())
    }

    fun exportConfig(context: Context, uri: Uri): Boolean = runCatching {
        val output = context.contentResolver.openOutputStream(uri) ?: error("Cannot open output")
        output.use { stream ->
            val xml = Xml.newSerializer()
            xml.setOutput(stream, "UTF-8"); xml.startDocument("UTF-8", true); xml.startTag(null, "map")
            val data = values().toMutableMap()
            data["_block_apps"] = blockApps.data
            data.forEach { (key, value) ->
                when (value) {
                    is Boolean -> xml.startTag(null, "boolean").attribute(null, "name", key)
                        .attribute(null, "value", value.toString()).endTag(null, "boolean")
                    is Set<*> -> {
                        xml.startTag(null, "set").attribute(null, "name", key)
                        value.filterIsInstance<String>().sorted().forEach { xml.startTag(null, "string").text(it).endTag(null, "string") }
                        xml.endTag(null, "set")
                    }
                }
            }
            xml.endTag(null, "map"); xml.endDocument()
        }
        true
    }.getOrDefault(false)

    fun importConfig(context: Context, uri: Uri): Boolean = runCatching {
        val input = context.contentResolver.openInputStream(uri) ?: error("Cannot open input")
        val updates = mutableMapOf<String, Any>()
        input.use {
            val parser = XmlPullParserFactory.newInstance().newPullParser()
            parser.setInput(it, "UTF-8")
            var setName: String? = null
            val packages = mutableSetOf<String>()
            while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                if (parser.eventType == XmlPullParser.START_TAG) when (parser.name) {
                    "set" -> setName = parser.getAttributeValue(null, "name")
                    "string" -> if (setName == "_block_apps") packages.add(parser.nextText())
                    "boolean" -> {
                        val key = parser.getAttributeValue(null, "name")
                        val value = parser.getAttributeValue(null, "value")?.toBooleanStrictOrNull()
                        if (key in knownKeys && value != null) updates[key] = value
                    }
                }
                if (parser.eventType == XmlPullParser.END_TAG && parser.name == "set") {
                    if (setName == "_block_apps") updates["_block_apps"] = packages.toSet()
                    setName = null
                }
                parser.next()
            }
        }
        check(updates.isNotEmpty()) { "No recognized configuration" }
        updates.forEach { (key, value) -> set(key, value) }
        true
    }.getOrDefault(false)
}
