package com.espin.chilean_rut

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RutTests {

    @Test fun test_ValidRut() {
        val ruts = arrayOf(
            Rut("1", "9"),
            Rut("21198663","8"),
            Rut("21313774","3"),
            Rut("13239959","k"),
            Rut("21770960","1"),
            Rut("19963722","3"),
            Rut("11765793","0"),
            Rut("14713193","3"),
            Rut("14449209","9"),
            Rut("15605286","8"),
            Rut("5942232","4")
        )
        ruts.forEach { assertTrue(it.isValid()) }
    }

    @Test fun test_InvalidRut() {
        val ruts = arrayOf(
            Rut("1", "0"),
            Rut("21198663","7"),
            Rut("21313774","4"),
            Rut("13239959","1"),
            Rut("21770960","2"),
            Rut("19963722","6"),
            Rut("11765793","7"),
            Rut("14713193","9"),
            Rut("14449209","0"),
            Rut("15605286","k"),
            Rut("5942232","1")
        )
        ruts.forEach { assertFalse(it.isValid()) }
    }

    @Test fun test_ShouldFormatFull() {
        assertEquals("15.605.286-8", Rut("15605286", "8").format())
        assertEquals("1-9", Rut("1", "9").format())
        assertEquals("13.239.959-k", Rut("13239959", "k").format(Rut.FORMAT.FULL))
    }

    @Test fun test_ShouldFormatDash() {
        assertEquals("1-9", Rut("1", "9").format(Rut.FORMAT.ONLY_DASH))
        assertEquals("15605286-8", Rut("15605286", "8").format(Rut.FORMAT.ONLY_DASH))
    }

    @Test fun test_ShouldFormatEscaped() {
        assertEquals("19", Rut("1", "9").format(Rut.FORMAT.ESCAPED))
        assertEquals("156052868", Rut("15605286", "8").format(Rut.FORMAT.ESCAPED))
    }

    @Test fun test_ShouldDestructureInComponents() {
        val rut = Rut("1", "9")
        val (number, dv) = rut
        assertEquals(1, number)
        assertEquals("9", dv)
    }

    @Test fun test_ShouldGiveRandom() {
        val rut = Rut.random()
        assertTrue(rut.isValid())
    }

    @Test fun test_ShouldGiveRandoms() {
        val ruts = Rut.randoms(42)
        assertEquals(42, ruts.size)
    }

}
