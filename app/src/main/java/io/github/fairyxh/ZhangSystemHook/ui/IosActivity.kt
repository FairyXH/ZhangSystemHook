package io.github.fairyxh.ZhangSystemHook.ui

import android.app.Activity
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.widget.*

open class IosActivity : Activity() {
    val night get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    val background get() = Color.parseColor(if (night) "#000000" else "#F2F2F7")
    val surface get() = Color.parseColor(if (night) "#1C1C1E" else "#FFFFFF")
    val ink get() = Color.parseColor(if (night) "#F5F5F7" else "#1C1C1E")
    val secondary get() = Color.parseColor(if (night) "#AEAEB2" else "#737378")
    val blue = Color.rgb(0, 122, 255)
    lateinit var content: LinearLayout
    fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    fun rounded(color: Int, radius: Int = 14) = GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = background
        window.navigationBarColor = background
        window.decorView.systemUiVisibility = if (night) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    fun page(title: String, subtitle: String, scroll: Boolean = true) {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(this@IosActivity.background) }
        root.setOnApplyWindowInsetsListener { view, insets ->
            if (android.os.Build.VERSION.SDK_INT >= 30) {
                val bars = insets.getInsets(WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout())
                view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            }
            insets
        }
        setContentView(root)
        content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(12), dp(20), dp(28)) }
        if (scroll) root.addView(ScrollView(this).apply {
            isFillViewport = true; isVerticalScrollBarEnabled = false; addView(content)
        }, LinearLayout.LayoutParams(-1, -1))
        else root.addView(content, LinearLayout.LayoutParams(-1, -1))
        content.addView(label(title, 34, true).apply { setPadding(dp(2), dp(8), 0, dp(4)) })
        content.addView(label(subtitle, 13, color = secondary).apply { setPadding(dp(2), 0, 0, dp(24)) })
    }
    fun label(text: String, size: Int = 16, bold: Boolean = false, color: Int = ink): TextView = TextView(this).apply {
        this.text = text; textSize = size.toFloat(); setTextColor(color)
        if (bold) typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }
    fun group(title: String, footer: String? = null, block: (LinearLayout) -> Unit) {
        content.addView(label(title.uppercase(), 12, color = secondary).apply { setPadding(dp(16), dp(22), 0, dp(8)) })
        val card = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; background = rounded(surface); clipToOutline = true }
        content.addView(card, LinearLayout.LayoutParams(-1, -2))
        block(card)
        if (footer != null) content.addView(label(footer, 12, color = secondary).apply {
            setPadding(dp(16), dp(8), dp(12), 0); setLineSpacing(dp(3).toFloat(), 1f)
        })
    }
    fun row(parent: LinearLayout, title: String, detail: String? = null, action: (() -> Unit)? = null): LinearLayout {
        if (parent.childCount > 0) parent.addView(View(this).apply {
            setBackgroundColor(if (night) 0xFF38383A.toInt() else 0xFFE5E5EA.toInt())
        }, LinearLayout.LayoutParams(-1, dp(1)).apply { leftMargin = dp(16) })
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(56); setPadding(dp(16), dp(12), dp(16), dp(12))
        }
        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(label(title))
        if (detail != null) texts.addView(label(detail, 12, color = secondary).apply { setPadding(0, dp(5), dp(8), 0) })
        row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))
        if (action != null) {
            row.addView(label("›", 26, color = secondary))
            row.isClickable = true
            row.setOnClickListener { action() }
            val value = android.util.TypedValue()
            theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
            row.foreground = getDrawable(value.resourceId)
        }
        parent.addView(row)
        return row
    }
    fun toggle(parent: LinearLayout, title: String, detail: String?, value: Boolean, update: (Boolean) -> Unit): Switch {
        val row = row(parent, title, detail)
        val control = IosSwitch(this).apply {
            contentDescription = title
            isChecked = value
            showText = false
            thumbTintList = ColorStateList.valueOf(Color.WHITE)
            trackTintList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                intArrayOf(0xFF34C759.toInt(), if (night) 0xFF48484A.toInt() else 0xFFE5E5EA.toInt()))
            setOnCheckedChangeListener { _, checked -> update(checked) }
        }
        row.addView(control)
        row.setOnClickListener { control.isChecked = !control.isChecked }
        return control
    }
    fun toast(message: String) = runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}
