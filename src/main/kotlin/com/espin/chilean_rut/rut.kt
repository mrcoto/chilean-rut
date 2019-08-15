package com.espin.chilean_rut

import kotlin.random.Random

enum class RUTFORMAT { FULL, ONLY_DASH, ESCAPED }

class Rut(number: String, dv: String) : Comparable<Rut> {

    val number: Int
    val dv: String

    companion object Util {
        private const val numberRgx = """^[1-9][0-9]?(\.?\d{3}){0,2}$"""
        private const val dvRgx = """^([0-9]|k|K)$"""
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

        fun random(min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): Rut {
            val number = (if (seed == null) Random else Random(seed)).nextInt(min, max)
            return Rut(number.toString(), calcDv(number))
        }

        fun randoms(n: Int = 1, min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): List<Rut> {
            val list = mutableListOf<Rut>()
            repeat(n) { list.add(random(min, max, seed)) }
            return list
        }

        fun uniques(n: Int = 1, min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): List<Rut> {
            val set = mutableSetOf<Rut>()
            while(set.size < n) { set.add(random(min, max, seed)) }
            return set.toList()
        }

    }

    init {
        require(number.matches(numberRgx.toRegex())){ "Formato Inválido" }
        require(dv.matches(dvRgx.toRegex())) { "Dígito Verificador inválido" }
        this.number = number.replace(".", "").toInt()
        this.dv = dv.toLowerCase()
    }

    fun isValid() : Boolean = calcDv(number) == dv

    fun format(format: RUTFORMAT = RUTFORMAT.FULL) : String = when(format) {
        RUTFORMAT.FULL -> "%,d-$dv".format(number).replace(",", ".")
        RUTFORMAT.ONLY_DASH -> "$number-$dv"
        RUTFORMAT.ESCAPED -> "$number$dv"
    }

    override fun compareTo(other: Rut): Int = number.compareTo(other.number)
    override fun equals(other: Any?): Boolean = if (other is Rut) other.number == number && other.dv == dv else false
    override fun toString(): String = "Rut($number, $dv)"

    operator fun component1() : Int = number
    operator fun component2() : String = dv

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + dv.hashCode()
        return result
    }

}