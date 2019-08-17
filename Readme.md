# Kotlin Chilean RUT

Librería para facilitar la validación y aplicación del número
de identificación utilizado en Chile (RUT).

## Índice

* [Instalación](#instalacin)
    - [Versión](#versin)
    - [Gradle](#gradle)
* [Uso](#uso)
    - [Instanciar RUT](#instanciar-rut)
    - [Validar RUT](#validar-rut)
    - [Calcular DV](#calcular-dv)
    - [Formatear RUT](#formatear-rut)
    - [Destructuración](#destructuracin)
    - [Comparación](#comparacin)
    - [Generación Aleatoria](#generacin-aleatoria)
    - [toString](#tostring)
* [Licencia](#licencia)

## Instalación

### Versión

La versión corresponde a: `'com.mrcoto:chilean-rut:1.0'`.

### Gradle

~~~gradle
repositories {
    jcenter()
}

dependencies {
    ...
    compile 'com.mrcoto:chilean-rut:1.0'
}
~~~ 

## Uso

Los RUT se dividen en **parte numérica** y **dígito verificador (dv)**.
- La **parte numérica** debe contener entre **1 y 8 dígitos** (sin comenzar con 0)
- El **dv** debe ser un dígito (0-9), k o K

Por defecto, al generarse la instancia el dv se convierte a minúsculas.

### Instanciar RUT

Hay dos formas de instanciar el RUT; constructor y parse.

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut1 = Rut("12345678", "k")
    val rut2 = Rut("12345678", "K")
    val rut3 = Rut.parse(12345678) // Automáticamente cálcula el 'dv'
    val rut4 = Rut.parse("12345678k")
    val rut5 = Rut.parse("12345678K")
    val rut6 = Rut.parse("12345678-k")
    val rut7 = Rut.parse("12.345.678-k")
}
~~~

### Validar RUT

Se utiliza el método `isValid()` a una instancia ya creada.

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut1 = Rut.parse("12345678k")
    val rut2 = Rut.parse("123456785")
    println(rut1.isValid()) // false
    println(rut2.isValid()) // true
}
~~~

### Calcular DV

Dada una parte numérica se puede calcular el dv.

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val dv = Rut.calcDv(12345678)
    println(dv) // 5
}
~~~

### Formatear RUT

Se puede formatear de forma **completa**, **solo guión** o **sin puntos y guión**

~~~kotlin
import com.mrcoto.chilean_rut.RUTFORMAT
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut = Rut.parse("12345678k")
    println(rut.format()) // 12.345.678-k
    println(rut.format(RUTFORMAT.FULL)) // 12.345.678-k
    println(rut.format(RUTFORMAT.ONLY_DASH)) // 12345678-k
    println(rut.format(RUTFORMAT.ESCAPED)) // 12345678k
}
~~~

### Destructuración

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut = Rut.parse("12345678k")
    val (num, dv) = rut // (12345678, k)
    val (number, _) = rut // (12345678, _)
    val (_, dv2) = rut // (_, k)
}
~~~

### Comparación

Se pueden comparar (menor, mayor) dos RUT distintos,
en base a su parte numérica.

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut1 = Rut.parse("12345678k")
    val rut2 = Rut.parse("12345677k")
    println(rut2 < rut1) // true
}
~~~

### Generación Aleatoria

Se puede generar un RUT aleatorio, una lista de RUT aleatorio, o 
una lista única de RUT aleatorio.

Como los RUT son comparables, se puede ordenar la 
lista resultante.

**Nota:** Por defecto, se genera un RUT entre 4.000.000 a 80.000.000,
se deben cambiar los parámetros min y max, para generar entre otro rango.

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut = Rut.random()
    val rut2 = Rut.random(min=100, max=200, seed=42)
    val rut3 = Rut.random(min=100, max=200, seed=42)
    println(rut.format())
    println(rut2.format())
    println(rut3.format())
    println(rut3 == rut2) // true

    val ruts = Rut.randoms(n=5) // 5 ruts a generar
    val sortedRuts = Rut.randoms(n=5).sorted()
    val uniqueRuts = Rut.uniques(n=5)

    println(ruts)
    println(sortedRuts)
    println(uniqueRuts)
}
~~~

### toString

Del ejemplo anterior, se puede observar la representación a String
de un RUT

~~~kotlin
import com.mrcoto.chilean_rut.Rut

fun main() {
    val rut = Rut.parse("12345678-k")
    println(rut) // Rut(12345678, k)
}
~~~

## Licencia

Esta librería se distribuye bajo la licencia MIT.