package io.github.fairyxh.ZhangSystemHook.hook

import android.database.AbstractCursor
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.database.DataSetObservable
import android.os.Bundle

/** AbstractCursor owns logical position; only onMove translates it to a physical source row. */
class ScreenshotFilteringCursor(private val source: Cursor) : AbstractCursor() {
    private var visible = scan()
    private val changes = DataSetObservable()
    private val observer = object : DataSetObserver() {
        override fun onChanged() {
            visible = scan()
            mPos = -1
            changes.notifyChanged()
        }
        override fun onInvalidated() { changes.notifyInvalidated() }
    }
    init { source.registerDataSetObserver(observer) }

    private fun scan(): IntArray {
        val previous = source.position
        val columns = arrayOf("_display_name", "title", "relative_path", "_data")
            .map { source.getColumnIndex(it) }.filter { it >= 0 }
        if (columns.isEmpty()) return IntArray(source.count) { it }
        val positions = ArrayList<Int>(source.count)
        try {
            source.moveToPosition(-1)
            while (source.moveToNext()) {
                if (!ScreenshotMatcher.matches(columns.map { source.getString(it).orEmpty() })) positions.add(source.position)
            }
        } finally { source.moveToPosition(previous) }
        return positions.toIntArray()
    }
    override fun getCount() = visible.size
    override fun getColumnNames(): Array<String> = source.columnNames
    override fun onMove(oldPosition: Int, newPosition: Int): Boolean = source.moveToPosition(visible[newPosition])
    override fun getString(column: Int): String? { checkPosition(); return source.getString(column) }
    override fun getShort(column: Int): Short { checkPosition(); return source.getShort(column) }
    override fun getInt(column: Int): Int { checkPosition(); return source.getInt(column) }
    override fun getLong(column: Int): Long { checkPosition(); return source.getLong(column) }
    override fun getFloat(column: Int): Float { checkPosition(); return source.getFloat(column) }
    override fun getDouble(column: Int): Double { checkPosition(); return source.getDouble(column) }
    override fun getBlob(column: Int): ByteArray? { checkPosition(); return source.getBlob(column) }
    override fun getType(column: Int): Int { checkPosition(); return source.getType(column) }
    override fun isNull(column: Int): Boolean { checkPosition(); return source.isNull(column) }
    override fun getExtras(): Bundle = source.extras
    override fun respond(extras: Bundle?): Bundle = source.respond(extras)
    override fun registerContentObserver(observer: ContentObserver) = source.registerContentObserver(observer)
    override fun unregisterContentObserver(observer: ContentObserver) = source.unregisterContentObserver(observer)
    override fun registerDataSetObserver(observer: DataSetObserver) = changes.registerObserver(observer)
    override fun unregisterDataSetObserver(observer: DataSetObserver) = changes.unregisterObserver(observer)
    override fun close() {
        if (isClosed) return
        source.unregisterDataSetObserver(observer)
        source.close()
        changes.unregisterAll()
        super.close()
    }
    @Suppress("DEPRECATION")
    override fun requery(): Boolean {
        val changed = source.requery()
        if (changed) { visible = scan(); mPos = -1 }
        return changed
    }
    @Suppress("DEPRECATION")
    override fun deactivate() { source.deactivate(); super.deactivate() }
}
