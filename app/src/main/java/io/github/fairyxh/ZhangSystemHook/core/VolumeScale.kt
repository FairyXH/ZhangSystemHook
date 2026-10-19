package io.github.fairyxh.ZhangSystemHook.core

object VolumeScale {
    fun map(index: Int, fromMin: Int, fromMax: Int, toMin: Int, toMax: Int): Int {
        if (fromMax <= fromMin || toMax <= toMin) return index
        val span = fromMax.toLong() - fromMin
        val normalized = (index.toLong() - fromMin).coerceIn(0L, span)
        return (toMin + (normalized * (toMax.toLong() - toMin) + span / 2) / span).toInt()
    }
}
