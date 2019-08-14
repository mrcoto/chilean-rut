package com.espin.chilean_rut

import kotlin.random.Random

class Rut(number: String, dv: String) {

    enum class FORMAT { FULL, ONLY_DASH, ESCAPED }

    val number: Int
    val dv: String

    companion object Util {
        private const val numberRgx = """^[1-9][0-9]?(\.?\d{3}){0,2}$"""
        private const val ZERO = "0"
        private const val K = "k"
        private const val ELEVEN = 11
        private const val TEN = 10
        private val SERIE = intArrayOf(2, 3, 4, 5, 6, 7)
        private const val MIN_RANGE = 4_000_000
        private const val MAX_RANGE = 80_000_000

        fun parse(rut: String) : Rut {
            if (rut.isEmpty() || rut == ZERO)
                return Rut("", "")
            val dv = if (rut.isNotEmpty()) rut[rut.length - 1].toString() else ""
            val sub = rut.substring(0, rut.length - 1)
            val number =  if (sub[sub.length - 1] == '-') sub.substring(0, sub.length - 1) else sub
            return Rut(number, dv)
        }

        fun calcDv(number: Int) : String {
            var copy = number
            var sum = 0
            var index = 0
            while (copy > 0) {
                sum += SERIE[index] * (copy % TEN)
                copy /= TEN
                index = if (index < SERIE.size - 1) index + 1 else 0
            }
            return dvFromResult(ELEVEN - (sum % ELEVEN))
        }

        private fun dvFromResult(result: Int) : String = when(result) {
            ELEVEN -> ZERO
            TEN -> K
            else -> result.toString()
        }

        fun random(): Rut {
            val number = Random.nextInt(MIN_RANGE, MAX_RANGE)
            return Rut(number.toString(), calcDv(number))
        }

        fun randoms(n: Int = 1): List<Rut> {
            val list = mutableListOf<Rut>()
            repeat(n) { list.add(random()) }
            return list
        }

    }

    init {
        require(number.matches(numberRgx.toRegex())){ "Formato Inválido" }
        this.number = number.replace(".", "").toInt()
        this.dv = dv.toLowerCase()
    }

    fun isValid() : Boolean = calcDv(number) == dv

    fun format(format: FORMAT = FORMAT.FULL) : String = when(format) {
        FORMAT.FULL -> "%,d-$dv".format(number).replace(",", ".")
        FORMAT.ONLY_DASH -> "$number-$dv"
        FORMAT.ESCAPED -> "$number$dv"
    }

    operator fun component1() : Int = number
    operator fun component2() : String = dv

}

fun main() {
    val rut = Rut("17679133", "0")
    println(rut.format())
    println(rut.format(Rut.FORMAT.ONLY_DASH))
    println(rut.format(Rut.FORMAT.ESCAPED))
    println(rut.isValid())
    println(Rut.calcDv(1234567))
    println(rut.number)
    println(rut.dv)
    val (number, dv) = rut
    println("$number, $dv")
}