package io.github.fairyxh.ZhangSystemHook.core

import org.junit.Assert.*
import org.junit.Test

class VolumeScaleTest {
    @Test fun colorOsMediaRangeMapsWithoutJumpingToMaximum() {
        assertEquals(1, VolumeScale.map(0, 0, 160, 1, 9))
        assertEquals(5, VolumeScale.map(80, 0, 160, 1, 9))
        assertEquals(9, VolumeScale.map(160, 0, 160, 1, 9))
    }
    @Test fun clampsAndRoundsInput() {
        assertEquals(1, VolumeScale.map(-50, 0, 160, 1, 9))
        assertEquals(9, VolumeScale.map(900, 0, 160, 1, 9))
        assertEquals(8, VolumeScale.map(80, 0, 160, 1, 15))
    }
    @Test fun monotonicAcrossFullMediaRange() {
        val results = (0..160).map { VolumeScale.map(it, 0, 160, 1, 9) }
        assertTrue(results.zipWithNext().all { (a, b) -> b >= a && b - a <= 1 })
    }
    @Test fun degenerateRangesDoNotDivideByZero() {
        assertEquals(5, VolumeScale.map(5, 0, 0, 1, 9))
        assertEquals(5, VolumeScale.map(5, 0, 160, 1, 1))
    }
}
