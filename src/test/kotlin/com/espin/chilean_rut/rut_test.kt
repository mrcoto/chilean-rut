package com.espin.chilean_rut

import java.lang.IllegalArgumentException
import kotlin.test.*

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
        assertEquals("13.239.959-k", Rut("13239959", "k").format(RUTFORMAT.FULL))
    }

    @Test fun test_ShouldFormatDash() {
        assertEquals("1-9", Rut("1", "9").format(RUTFORMAT.ONLY_DASH))
        assertEquals("15605286-8", Rut("15605286", "8").format(RUTFORMAT.ONLY_DASH))
    }

    @Test fun test_ShouldFormatEscaped() {
        assertEquals("19", Rut("1", "9").format(RUTFORMAT.ESCAPED))
        assertEquals("156052868", Rut("15605286", "8").format(RUTFORMAT.ESCAPED))
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

    @Test fun test_ShouldGiveUniques() {
        val ruts = Rut.uniques(42)
        assertEquals(42, ruts.size)
    }

    @Test fun test_ShouldParseStrings() {
        val ruts = arrayOf("19253299-k", "19253299k", "19.253.299-k", "19.253.299k", "19.253.299K")
        ruts.forEach { assertTrue(Rut.parse(it).isValid()) }
    }

    @Test fun test_ShouldThrowException_OnInvalidRut() {
        val ruts = arrayOf(
            "",
            "1.2",
            "12.345.67c",
            "1a.345.678",
            "7.6543.21",
            "123.456.78",
            "12.345.678.345",
            "12,345,678",
            "0",
            "12-A"
        );
        ruts.forEach {
            assertFailsWith(IllegalArgumentException::class) {
                Rut.parse(it)
            }
        }
    }

    @Test fun test_ShouldCompareRuts() {
        val rut1 = Rut("1234", "3")
        val rut2 = Rut("1345", "5")
        assertTrue(rut1 < rut2)
        assertFalse(rut1 > rut2)
    }

}
