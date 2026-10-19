package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.LruCache
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.ui.IosActivity
import java.util.concurrent.Executors

class AppsConfigActivity : IosActivity() {
    private data class App(val info: ApplicationInfo, val name: String, val system: Boolean)
    private val worker = Executors.newSingleThreadExecutor()
    private val icons = LruCache<String, Drawable>(64)
    private val handler = Handler(Looper.getMainLooper())
    private var all = emptyList<App>()
    private var visible = emptyList<App>()
    private var category = 0
    private var query = ""
    private lateinit var count: TextView
    private lateinit var list: ListView
    private val adapter = AppsAdapter()
    private val refilter = Runnable { filter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        page("应用名单", "用户应用勾选允许读取；系统应用依据黑白名单模式处理", scroll = false)
        content.addView(label("‹ 返回设置", 16, color = blue).apply {
            setPadding(0, 0, 0, dp(16)); setOnClickListener { finish() }
        })
        val search = EditText(this).apply {
            hint = "搜索名称或包名"; textSize = 16f; setSingleLine(); setTextColor(ink); setHintTextColor(secondary)
            background = rounded(if (night) 0xFF1C1C1E.toInt() else 0xFFE5E5EA.toInt(), 11)
            setPadding(dp(14), dp(10), dp(14), dp(10))
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    query = s.toString().trim(); handler.removeCallbacks(refilter); handler.postDelayed(refilter, 150)
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
        content.addView(search, LinearLayout.LayoutParams(-1, dp(46)))
        val categories = Spinner(this).apply {
            adapter = ArrayAdapter(this@AppsConfigActivity, android.R.layout.simple_spinner_dropdown_item, arrayOf("全部应用", "用户应用", "系统应用"))
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { category = position; filter() }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        content.addView(categories, LinearLayout.LayoutParams(-1, dp(48)))
        count = label("正在读取应用…", 12, color = secondary).apply { setPadding(dp(4), dp(8), 0, dp(12)) }
        content.addView(count)
        list = ListView(this).apply {
            divider = null
            background = rounded(surface)
            clipToOutline = true
            adapter = this@AppsConfigActivity.adapter
            emptyView = this@AppsConfigActivity.count
            setOnItemClickListener { _, _, position, _ ->
                ConfigData.blockApps.switch(visible[position].info.packageName)
                this@AppsConfigActivity.adapter.notifyDataSetChanged()
            }
        }
        content.addView(list, LinearLayout.LayoutParams(-1, 0, 1f))
        worker.execute {
            val loaded = runCatching {
                packageManager.getInstalledApplications(0).filter { it.packageName != "android" }.map {
                    App(it, it.loadLabel(packageManager).toString(),
                        it.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
                }.sortedWith(compareBy<App> { !ConfigData.blockApps.contains(it.info.packageName) }.thenBy { it.name.lowercase() })
            }.getOrDefault(emptyList())
            runOnUiThread { if (!isDestroyed) { all = loaded; filter() } }
        }
    }
    private fun filter() {
        visible = all.filter { app ->
            (category == 0 || (category == 2) == app.system) &&
                (app.name.contains(query, true) || app.info.packageName.contains(query, true))
        }
        if (::count.isInitialized) count.text = "${visible.size} 个应用"
        adapter.notifyDataSetChanged()
    }
    private inner class Holder(val root: LinearLayout, val icon: ImageView, val title: TextView,
                               val detail: TextView, val check: CheckBox)
    private inner class AppsAdapter : BaseAdapter() {
        override fun getCount() = visible.size
        override fun getItem(position: Int) = visible[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, recycled: View?, parent: ViewGroup?): View {
            val holder = recycled?.tag as? Holder ?: run {
                val row = LinearLayout(this@AppsConfigActivity).apply {
                    gravity = Gravity.CENTER_VERTICAL; minimumHeight = dp(76); setPadding(dp(14), dp(12), dp(14), dp(12))
                }
                val icon = ImageView(this@AppsConfigActivity)
                row.addView(icon, LinearLayout.LayoutParams(dp(40), dp(40)).apply { marginEnd = dp(12) })
                val texts = LinearLayout(this@AppsConfigActivity).apply { orientation = LinearLayout.VERTICAL }
                val title = label("", 15)
                val detail = label("", 11, color = secondary)
                texts.addView(title); texts.addView(detail)
                row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))
                val check = CheckBox(this@AppsConfigActivity).apply {
                    isClickable = false; isFocusable = false
                    buttonTintList = android.content.res.ColorStateList.valueOf(0xFF34C759.toInt())
                }
                row.addView(check)
                Holder(row, icon, title, detail, check).also { row.tag = it }
            }
            val app = visible[position]
            val pkg = app.info.packageName
            holder.title.text = app.name
            val mode = if (!app.system || ConfigData.flag("system_app_whitelist")) "白名单" else "黑名单"
            holder.detail.text = "$pkg\n${if (app.system) "系统应用" else "用户应用"} · $mode"
            holder.check.isChecked = ConfigData.blockApps.contains(pkg)
            holder.icon.tag = pkg
            holder.icon.setImageDrawable(icons.get(pkg))
            if (icons.get(pkg) == null) worker.execute {
                val drawable = runCatching { app.info.loadIcon(packageManager) }.getOrNull()
                if (drawable != null) {
                    icons.put(pkg, drawable)
                    runOnUiThread { if (!isDestroyed && holder.icon.tag == pkg) holder.icon.setImageDrawable(drawable) }
                }
            }
            return holder.root
        }
    }
    override fun onDestroy() { handler.removeCallbacksAndMessages(null); worker.shutdownNow(); super.onDestroy() }
}
