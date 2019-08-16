package com.mrcoto.chilean_rut

import kotlin.random.Random

/**
 * Formatos de un Rut
 *
 * - [FULL]: Formato con puntos y guión (12.345.678-9)
 * - [ONLY_DASH]: Formato solo sin puntos y con guión (12345678-9)
 * - [ESCAPED]: Formato sin puntos ni guión (123456789)
 */
enum class RUTFORMAT { FULL, ONLY_DASH, ESCAPED }

/**
 * Rut Chileno
 *
 * @param number Parte númerica de un rut (de 123-k, la parte númerica es "123")
 * @param dv Dígito verificador de un rut
 */
class Rut(number: String, dv: String) : Comparable<Rut> {

    /**
     * @property number Parte numérica del RUT
     */
    val number: Int

    /**
     * @property dv Dígito verificador del RUT
     */
    val dv: String

    /**
     * Métodos útiles del Rut
     */
    companion object Util {
        private const val numberRgx = """^([1-9]\d?(\.?\d{3}){0,2}|[1-9]\d{0,2}(\.?\d{3})?)$"""
        private const val dvRgx = """^([0-9]|k|K)$"""
        private const val ZERO = "0"
        private const val K = "k"
        private const val ELEVEN = 11
        private const val TEN = 10
        /**
         * Serie numérica para calcular el dígito verificador
         */
        private val SERIE = intArrayOf(2, 3, 4, 5, 6, 7)
        /**
         * Rango mínimo - máximo que retornerá un RUT aleatorio
         */
        private const val MIN_RANGE = 4_000_000
        private const val MAX_RANGE = 80_000_000

        /**
         * Transforma un RUT, dada la parte numérica y su dv
         *
         * Ejemplo formatos aceptados:
         *
         * - 12345678-k
         * - 12345678k
         * - 12.345.678-k
         * - 12.345.678k
         * - 12.345.678K
         *
         * @param rut Rut con su parte númerica y dv
         *
         * @return Instancia de un RUT
         */
        fun parse(rut: String) : Rut {
            if (rut.isEmpty() || rut == ZERO)
                return Rut("", "")
            val dv = if (rut.isNotEmpty()) rut[rut.length - 1].toString() else ""
            val sub = rut.substring(0, rut.length - 1)
            val number =  if (sub[sub.length - 1] == '-') sub.substring(0, sub.length - 1) else sub
            return Rut(number, dv)
        }

        /**
         * Transforma una parte numérica a un RUT
         *
         * @param number Parte numérica del RUT (Ej si es 123-k, sería 123)
         *
         * @return RUT
         */
        fun parse(number: Int) : Rut = Rut(number.toString(), calcDv(number))

        /**
         * Calcula el digito verificador
         *
         * @param number Parte númerica del rut
         *
         * @return Dígito verificador
         */
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

        /**
         * Mapea el resultado del cálculo al dígito verificador
         *
         * @param result Resultado del cálculo del dígito verificador
         *
         * @return Dígito verificador
         */
        private fun dvFromResult(result: Int) : String = when(result) {
            ELEVEN -> ZERO
            TEN -> K
            else -> result.toString()
        }

        /**
         * Genera un RUT de forma aleatoria
         *
         * @param min Valor mínimo que podría tener la parte numérica del RUT
         * @param max Valor máximo que podría tener la parte numérica del RUT
         * @param seed Indica si el RUT se generará mediante un seed dado. null si no se debe aplicar.
         *
         * @return RUT aleatorio
         */
        fun random(min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): Rut {
            val number = (if (seed == null) Random else Random(seed)).nextInt(min, max)
            return Rut(number.toString(), calcDv(number))
        }

        /**
         * Genera una lista de RUT's de tamaño [n] de forma aleatoria
         *
         * @param n Tamaño de la lista
         * @param min Valor mínimo que podría tener la parte numérica del RUT
         * @param max Valor máximo que podría tener la parte numérica del RUT
         * @param seed Indica si el RUT se generará mediante un seed dado. null si no se debe aplicar.
         *
         * @return Lista de tamaño [n] que contiene RUT's de forma aleatoria
         */
        fun randoms(n: Int = 1, min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): List<Rut> {
            val list = mutableListOf<Rut>()
            repeat(n) { list.add(random(min, max, seed)) }
            return list
        }

        /**
         * Genera una lista única de RUT's de tamaño [n] de forma aleatoria
         *
         * @param n Tamaño de la lista
         * @param min Valor mínimo que podría tener la parte numérica del RUT
         * @param max Valor máximo que podría tener la parte numérica del RUT
         * @param seed Indica si el RUT se generará mediante un seed dado. null si no se debe aplicar.
         *
         * @return Lista única de tamaño [n] que contiene RUT's de forma aleatoria
         */
        fun uniques(n: Int = 1, min: Int = MIN_RANGE, max: Int = MAX_RANGE, seed: Int? = null): List<Rut> {
            val set = mutableSetOf<Rut>()
            while(set.size < n) { set.add(random(min, max, seed)) }
            return set.toList()
        }

    }

    /**
     * Inicializador del RUT
     * - Verifica la parte numérica
     * - Verifica el dígito verificador
     */
    init {
        require(number.matches(numberRgx.toRegex())){ "Formato Inválido" }
        require(dv.matches(dvRgx.toRegex())) { "Dígito Verificador inválido" }
        this.number = number.replace(".", "").toInt()
        this.dv = dv.toLowerCase()
    }

    /**
     * Valida un RUT
     *
     * @return Verdadero si el RUT es válido
     */
    fun isValid() : Boolean = calcDv(number) == dv

    /**
     * Formatea el RUT
     *
     * - [RUTFORMAT.FULL] -> RUT con puntos y guión (12.345.678-k)
     * - [RUTFORMAT.ONLY_DASH] -> RUT sin puntos y con guión (12345678-k)
     * - [RUTFORMAT.ESCAPED] -> RUT sin puntos y guión (12345678k)
     *
     * @param format Formato del RUT
     * @return RUT formateado
     */
    fun format(format: RUTFORMAT = RUTFORMAT.FULL) : String = when(format) {
        RUTFORMAT.FULL -> "%,d-$dv".format(number).replace(",", ".")
        RUTFORMAT.ONLY_DASH -> "$number-$dv"
        RUTFORMAT.ESCAPED -> "$number$dv"
    }

    /**
     * Compara esta instancia de RUT con otra
     *
     * @param other La otra instancia de RUT
     *
     * @return Valor de comparación
     */
    override fun compareTo(other: Rut): Int = number.compareTo(other.number)

    /**
     * Igualdad estructural
     *
     * @param other Otra Instancia (o null)
     */
    override fun equals(other: Any?): Boolean = if (other is Rut) other.number == number && other.dv == dv else false

    /**
     * @return Representación de cadena de caracteres
     */
    override fun toString(): String = "Rut($number, $dv)"

    /**
     * Primer componente de la destructuración
     *
     * @return Primer componente de la destructuración (number, _)
     */
    operator fun component1() : Int = number

    /**
     * Segundo componente de la destructuración
     *
     * @return Segundo componente de la destructuración (_, dv)
     */
    operator fun component2() : String = dv

    /**
     * Hash Code
     *
     * @return Valor hash
     */
    override fun hashCode(): Int {
        var result = number
        result = 31 * result + dv.hashCode()
        return result
    }

}