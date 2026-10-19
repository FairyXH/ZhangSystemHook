package io.github.fairyxh.ZhangSystemHook.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.widget.Switch

/** Retains native Switch input/accessibility while drawing the iOS track and thumb. */
class IosSwitch(context: Context) : Switch(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val track = RectF()
    private val density = resources.displayMetrics.density
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((51 * density).toInt(), (44 * density).toInt())
    }
    override fun onDraw(canvas: Canvas) {
        val night = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val radius = 15.5f * density
        val top = (height - 31 * density) / 2f
        track.set(0f, top, width.toFloat(), top + 31 * density)
        paint.color = when {
            isChecked -> 0xFF34C759.toInt()
            night -> 0xFF39393D.toInt()
            else -> 0xFFE5E5EA.toInt()
        }
        canvas.drawRoundRect(track, radius, radius, paint)
        paint.color = 0xFFFFFFFF.toInt()
        val checkedOnRight = isChecked != (layoutDirection == LAYOUT_DIRECTION_RTL)
        val centerX = if (checkedOnRight) width - radius else radius
        canvas.drawCircle(centerX, height / 2f, 13.5f * density, paint)
    }
}
