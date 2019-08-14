package com.espin.chilean_rut

import kotlin.random.Random

class Rut(number: String, dv: String) {

    enum class FORMAT { FULL, ONLY_DASH, ESCAPED }

    val number: Int
    val dv: String

    companion object Util {
        private const val numberRgx = ""
        private const val K = "k"
        private const val ELEVEN = 11
        private const val TEN = 10
        private val SERIE = intArrayOf(2, 3, 4, 5, 6, 7)
        private const val MIN_RANGE = 4_000_000
        private const val MAX_RANGE = 80_000_000

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
            ELEVEN -> "0"
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
//        require(isValidNumber(number)){ "Formato Inválido" }
        this.number = number.toInt()
        this.dv = dv.toLowerCase()
    }

    fun isValidNumber(number: String) : Boolean = number.matches(numberRgx.toRegex())

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