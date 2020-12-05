import org.junit.jupiter.api.Nested
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.test.Test
import kotlin.test.assertEquals

val rowsRange = 0..127
val columnsRange = 0..7

fun IntRange.takeNextRange(dir: String): IntRange {
    val divider = ((last-first) / 2) + 1
    val next = when(dir) {
        "F" -> this.chunked(divider).first()
        "B" -> this.chunked(divider).last()
        "L" -> this.chunked(divider).first()
        "R" -> this.chunked(divider).last()
        else -> throw IllegalArgumentException("Wrong input direction: $dir")
    }
    return IntRange(next.first(), next.last())
}

private fun getRow(line: String): Int {
    var range = rowsRange
    line.forEach { direction ->
        range = range.takeNextRange(direction.toString())
    }
    return range.single()
}

private fun getColumn(line: String): Int {
    var range = columnsRange
    line.forEach { direction ->
        range = range.takeNextRange(direction.toString())
    }
    return range.single()
}

private fun getAllSeatId(realInput: String): MutableList<Int> {
    val list = mutableListOf<Int>()
    realInput.lines().forEach { line ->
        val row = getRow(line.substring(0..6))
        val column = getColumn(line.substring(7..9))

        val uniqueSeatId = calculSeatId(row, column)
        list += uniqueSeatId
    }
    return list
}

private fun calculSeatId(row: Int, column: Int) = row * 8 + column

class Day5 {

    @Test
    fun `assert take front`() {
        val nextRange = rowsRange.takeNextRange("F")
        assertEquals(0..63, nextRange)
    }

    @Test
    fun `assert take back`() {
        val nextRange = rowsRange.takeNextRange("B")
        assertEquals(64..127, nextRange)
    }

    @Test
    fun `assert take front then back`() {
        var nextRange = rowsRange.takeNextRange("F")
        nextRange = nextRange.takeNextRange("B")
        assertEquals(32..63, nextRange)
    }

    @Test
    fun `assert manually FBFBBFF is row 44`() {
        var nextRange = rowsRange.takeNextRange("F")
        nextRange = nextRange.takeNextRange("B")
        nextRange = nextRange.takeNextRange("F")
        nextRange = nextRange.takeNextRange("B")
        nextRange = nextRange.takeNextRange("B")
        nextRange = nextRange.takeNextRange("F")
        nextRange = nextRange.takeNextRange("F")
        assertEquals(44, nextRange.single())
    }

    @Test
    fun `assert auto looping FBFBBFF is row 44`() {
        val line = exampleInput.lines().first().substringBefore("RLR")
        val row = getRow(line)
        assertEquals(44, row)
    }

    @Test
    fun `assert take left`() {
        val nextFrontRange = columnsRange.takeNextRange("L")
        assertEquals(0..3, nextFrontRange)
    }

    @Test
    fun `assert take right`() {
        val nextFrontRange = columnsRange.takeNextRange("R")
        assertEquals(4..7, nextFrontRange)
    }

    @Test
    fun `assert manually RLR is column 5`() {
        var nextRange = columnsRange.takeNextRange("R")
        nextRange = nextRange.takeNextRange("L")
        nextRange = nextRange.takeNextRange("R")
        assertEquals(5, nextRange.single())
    }

    @Test
    fun `assert auto looping RLR is column 5`() {
        val line = exampleInput.lines().first().substringAfter("FBFBBFF")
        val column = getColumn(line)
        assertEquals(5, column)
    }

    @Nested
    inner class PartA {

        @Nested
        inner class `Example data` {
            @Test
            fun `assert highest seat id is 820`() {
                val ids = getAllSeatId(exampleInput)
                assertEquals(820, ids.maxOrNull())
            }
        }

        @Nested
        inner class `Real data` {
            @Test
            fun `find highest seat id`() {
                val ids = getAllSeatId(realInput)
                println("Part A: ${ids.maxOrNull()}")
            }
        }
    }

    @Nested
    inner class PartB {
        @Test
        fun `assert list of all seat id`() {
            val ids = getAllSeatId(realInput)
            ids.sort()
            var mySeat = 0
            for (i in 0..ids.size) {
//            println("$i ${ids[i]}")
                if (ids[i+1] != ids[i] + 1) {
                    mySeat = ids[i] + 1
                    break
                }
            }

            println("Part B: $mySeat")
        }
    }


    // row 70, column 7, seat ID 567.
// row 14, column 7, seat ID 119.
// row 102, column 4, seat ID 820.
    val exampleInput =
"""FBFBBFFRLR
BFFFBBFRRR
FFFBBBFRRR
BBFFBBFRLL"""

    val realInput =
"""BFBFBBBRLL
BFFBBBBRRR
FFBBFBFLLL
FFBBBBBRLR
BFFBFBFRRL
BFFBFFBRLL
FBBFBFBRLL
FFBFFBBLLR
BFBBBFFRRR
FBFFFFBLLL
FFFFFFBRRR
FBBFBFFRRR
FFBBBBFRLL
BFFBBFBLRR
BFFFFBFRRL
FFFFFBFRLL
FBFFBFFRRR
BFBFBBBLLR
FBFFBBBLRR
FBBBBBFRLR
FFBFFBBRLR
FBBFFFFRRL
FFFFFBBRRL
FFBBFBFRLL
BFFBBBBLLL
FFFFBFFRRL
FBBFFFFLLR
FFBBBBBLLR
FBFBBFBRRR
FBFFBBFRRL
BFBFBFBRLL
BFFBBBBLLR
FBBBBFFLLR
FFBFBBFRLL
BBFFFFBRRL
FBFFFBBLLL
FFFFBBBLLR
FFBBFFFRLL
BFBFFBBRLL
FFBBBFBLLR
FFBBFFFRRR
BFFFBBBRLL
FBFBBFFRLR
FBBFBBBRRR
FBBBBBBRRR
BFFBFFFRRR
BFFBBBFLLL
BFBFFFBLLL
BFFBFBFLLR
FBFFFFBLRL
BFFBBFFLRR
BFBFBFFLLL
BBFFFBFLRL
BFFFBBBLLL
FFBBBBBLLL
BFFFFFFRLL
FFBBBBBLRL
FBFBBBBLLR
FFBBFBBLRL
FBBFBFBLRL
FBBFBBBLRR
BFFBFBBRRR
BFFBBFFLRL
FBFBBFFLLR
BFBBBFFLRR
FBBBFFBLRL
FBFBBBBRLL
FFFFBFFLLL
BBFFFBFRRR
BFBFBBFLRR
FBBFBFFLLL
FBBBBFBRRR
FFFFBBFRRR
FBBFFBFLLL
FBBBFFBRRR
BFFBBBBLRL
FFFFBFBRLL
FBBFFFBRRR
FBFBBFFRLL
BFFFFBFRRR
FFBBBFFLRR
FBBFBBFLLR
BBFFFFBRRR
BFFBFBFLLL
FFFBBBFLLR
FFBFFBFLRL
FFBFBBBLRL
FBFFFBBRRL
BFFBFBBLLR
BFBBFBBRLR
BFBBFFFRRL
FBFBFFFLLR
FFBBBFFLLR
BFBBBFFLLR
BFBFBFBLLR
BBFFFBFLLL
FFBBFBFRRR
BFFFBBFRRR
FBFFBBFRLR
BFBBBBFRLL
BBFFFFBLLR
FFFBFBBLLR
FBFFBFBRRR
FFBFBFBRRR
FFFFFBBRLL
BFFFFBFLLR
FBBFBBFLRR
FBFFFFFRRL
FBBFBFBLLR
BFFFFBBLLL
FBBBBBFLLL
FBBFFBFRRR
BBFFBFBLRL
BFFFBFFRRL
BFFFFFBLLR
FFFBBFBRLR
FBFFBFFRLR
BFBFBFFLLR
BFFFBBFLRL
FFFFBFFLLR
BFFBFFFRLR
FBFBFBFLRR
FBBBBBBRLR
BFBFBFFLRL
BFFFBFBLLR
BBFFBFFLLR
BFFBBBBLRR
FFFBBFFRRR
FBBFBBBLRL
FFBBBFBRLR
FBBBFBFRLR
FBBBBFFLRL
FFBFBFFRLR
BBFFFFFLRL
FFFBBBBLRL
FBFBBBFLRL
BFFBFFFRRL
FFBFFBFRLR
FBFBFFFLRR
FBFBFFBRRL
FBFBBBBLRL
BFBBBBBLLL
BBFFFFBLRR
BFFFBFFLRR
FBBBBBFRRR
FBFFBBBLRL
BBFFFBBRLL
BFBFBFBRRR
BFFBFBBLRR
FFBBFBBRLL
FFFFFBBRLR
BFFFBFBRRL
FFFBFBBLLL
FFFFBFBRRR
FBBFFBBLRR
BFFFFBBLRR
FFBFFFBLRL
FFBFBFBLRR
FBBBBFFRRR
FBFFBFBLRL
BFBFFBBLLL
FBBFFFFLLL
FBBFFBBLLR
FFBFBFFRLL
FBFBBBFRRR
BFFFBFFRLL
FFBFBFBRLL
FFBBFBFLRL
FFBBFBBLLR
FBFBBFBLRL
BBFFFFFRRR
FBBFFBBLRL
BFBFFBBLLR
FFFFFBBLLL
FFBBBBFRRL
FFBBFFFLLL
FFFBFFBRLL
FFBFBBFLLR
BFFFBBBRLR
FBFFBBFRRR
FBBFFFFRLL
FFFBBBFRLR
FFBBFFBRRR
BFBFFFFLRR
BBFFBBFLLR
BFFBFFBLRL
BBFFBFFLRR
FFFBFFBRRR
BFFFBBBLRR
BFBFFBFRLR
FFFBFFFRRR
FFBFBBBRLR
BFBFBBFRLR
BBFFBFFRRL
BFBFBFFRLR
FBBFFFFRLR
FBFBFBBRLL
FFBBBFBRLL
FFBFFFFRRR
BFFBFBBLRL
FFFBFBFRLR
FBBFFFBLRL
FFBFFFFLRL
BBFFBBFRLL
FFFFFFBRRL
BFFBFFBLRR
BFFFFFFRRL
BFFBBFBLRL
FBBFFFFLRL
FFFBBFFRLR
BFBBFFFRLR
FFBBFBBLRR
FFBFBBBLLR
FFBBBBFLRR
FFBFBFFLLL
FFFBFFFLLL
FFBFBFFLRL
BFBFFFBLRL
FBFFFFFLRR
BFBBBBFRRR
FBBFBBFLLL
FFFBBFFLLR
FFBFBBBLLL
BBFFBFFLLL
FBFFBFBRLR
BFBBBBFLRR
FBBBFBFRRL
BFFBBBBRRL
BFBBFFFLLL
BFBBFBFRRR
BFFBFBBLLL
FBBBBBFRRL
FFFBFBFRRL
FFBBBBFLLL
FBBFBBBLLL
FBFFBFFLLR
BFBBBBBLRR
FBFFFFFLLL
BFFFBBBRRL
FBFFFBBRLL
BFBBFBBLRR
FBBBFBBRLR
BBFFBFBLLR
BBFFFFBRLL
BFBBFBFLLR
FBBFFBFLRL
BBFFFBFRRL
BFFFBBBLRL
FBBBBFBRRL
BFBFBFBRRL
FFBFFBBRRR
FBBFFBBLLL
BFFBFFFRLL
FFFFBFFRLR
FFBFFBBRLL
FBBBFFFLRL
FFBFBFBRRL
BFBBBFFLLL
FBBBBFBLLL
FFFBBBBLRR
FBFFFFBRLR
FFBBBBFLRL
BFBBBBFRRL
FFFFBBFLLL
BFFFBBBLLR
FFFBBBBRRL
FBBBFBFLLL
BFFFFFBLRL
FFFBFBBRRL
BFFBBFBRLL
FBFBBFBLRR
FBBBFFFLLL
BFBFFFFRRL
FBBBBFFRLL
BFBFFBBLRL
FBBBBFBRLL
FBBFFBBRLR
FFBBBFBRRL
FFFBBFFLRL
BFBBFBFRLL
FBBFBBFRLL
FBFBBBFLLR
FBFFBFBRLL
BFFFBFFRRR
BFBFBBFLRL
FBBBBBFLRR
FFFFBBFRRL
BFBBFFBRRR
FBBFFFBLRR
BFBBBFBLLR
FBBBBFBRLR
BFBFFFFRLL
FFFBBBFLLL
BFFFBFFLLL
FBFBFFBRLL
BBFFFFFLLL
FFFFFBBLRR
BFFFFFFLRR
BFFBFBFLRR
FFFBFFBLLR
BFFFFFFLLR
FBFFBBBRRR
FFFFBFBLLR
BFFBBFFLLL
FFFFFBBLLR
BFBBBFBRRL
FBFFBBBRLR
BBFFFFBLRL
FFBFFBFLLL
FBFFFFBLRR
FFFBFBFLLR
FFBFFFFLLR
FFFFFBFLRL
BBFFFBBLLL
BFFBFBFRRR
FBBBFFFLLR
BFBBBFBLLL
FBFFFFFRRR
FBFFBBFLRL
BFFBBFBLLR
FBBFFBFRLR
FFFFFBFRRL
FFBFFBBLLL
BFBFFBFRLL
BFBBFFFRLL
FBBBFBFRLL
FBFBFFFLLL
BBFFFBBLRR
FFFBFBFLLL
BBFFBFBLRR
FFBBFBFLRR
BFFBBFFRLR
FBFBFBBRLR
FBBFFBBRRR
FFBFFFBRRR
BFBBFBBLRL
FFFBFFFRLL
BFBBFBFLRL
BFBFBBBLLL
FBFBBFFLRL
FFBFBBBRRL
FFFBFFBRRL
FFBFFFFRLR
FFFFBBBRRR
FFFFBFBLLL
FFBFFBFRRR
FFBFFBFLRR
FFFBBFFRLL
FBBBFFFRLR
FFBFFFBRLL
FBBBFBFRRR
FBFFFBFLRR
FBBBFBFLRL
FBFBFFFRRL
BFBFFFFLRL
FFBBFFBLRL
BBFFBFBLLL
FBFBBBBRRR
FBBFFFBLLR
FBBFBFFRRL
FFBFBFBLLR
BFFBBBFLRR
FBFBFFBLRL
FBFFBBBLLL
FFBFBBFRRL
BFFBFFFLLR
FBBBFBBLRL
BFFBBFBRLR
BFFFBFBRLL
BFFFFBBRRR
FBFFBBFLLR
FFBFFFBRRL
FFBBBBBRRL
BFFBBBBRLR
BFFFBFBRLR
FBBBBBBLLL
BFFFBFBRRR
FFBBBFFRLR
FBBBFBFLLR
FBFBFBFRRR
FBFBBBBLLL
BFBFBFBRLR
FFFFBFFLRR
BFFBFBBRLL
BFBFFFBLLR
BFBBFBBLLL
FBFFBBBLLR
FBBFBBFRRR
FFFBFFFLLR
FFFBFBBRLL
BFBFFBFLLR
BFFFFBBLRL
FBFBBFBLLL
FBBFFBFRLL
BFBBBBFRLR
BFBFFFBRLL
BBFFFBFRLR
FFFFBBBLRL
BFBBFFBLRL
BBFFBBFRRL
FFBBBFBLLL
FBFBFBFLRL
FBBBBBBRRL
FFFFFBBRRR
FFFFFBFRRR
FBFFFBBRRR
BFFBBFBRRL
BBFFFFFRLL
FFFBFFBRLR
FFBBFFFRRL
FBFBFFFRLR
BFFBBBFRLR
BFBBBBFLRL
FBFFBFBRRL
FFBFBBBRRR
FFFFBBFRLL
FFFFBBBRLL
FBBFFFBLLL
BFBBFBBRRR
BFFBBBFRLL
BFFFBFFRLR
FBFBFFBLLL
BFFFFBBRLL
BBFFBFFLRL
BFBBBBBLRL
BFFFFBBRRL
BFFBFFFLRL
BBFFBBFRLR
BFBBFBFLLL
BBFFBFFRLR
FBBFBBBRLL
FFFBFBFLRR
BFBFFBFLLL
FFFFBBFLRR
FFFBBBBLLL
BFBFFFFRRR
FBFBBBFRLL
FFBBFFFLRR
FBFBBBFRLR
BFFFBBFLLR
BFBBBBBLLR
BFFBFFBRRR
FBBBFFBRLR
BFBFBBFLLL
FBFFFFFRLR
BFFFFFFLLL
BFFBFFBRLR
BBFFFFBRLR
BBFFBFBRLR
FBFBFBBLRR
BFFFBBFLRR
FBFFBBBRLL
FFFFBBFLRL
FFBFFBBLRL
FBFFBBFLLL
FBFBBFFLLL
FFBBFFBRLR
BFFFBBBRRR
BBFFFBBLLR
BFFBBBFLRL
BBFFFBBRLR
FBBBBBBLLR
FBFBBFFRRR
FBFFBBFRLL
FFFBBFBRLL
FFFBFBBRLR
FBFFBFFLLL
FFBBBBBLRR
FBFFFFFLLR
FBFFFBBLRL
FBFFBFBLRR
FFBBBBFRRR
FBBBFFFRRL
FFFFBBFLLR
FFFFFBFLRR
BBFFBBFLLL
FBFBFBBLLL
FBFBFFBRRR
FBFFBFFLRR
FBBFFFFLRR
FBBFBFFLLR
BFFBBBFRRR
FFBBBBBRRR
BFFFFFBRLR
BFFBFBFRLR
FFFFBFFRLL
BFBFFBBRLR
BFBBFFFLRL
FBFFFFBLLR
FFBFFBFRLL
BBFFFBBLRL
FBBFFBFLRR
BFFFFBBRLR
BFBBFBBRRL
FBBFBFBRRR
BFFFBFFLRL
FFBBFBBRRL
FFFBFFBLRR
FBBFBFBRRL
BFBFBBFLLR
BBFFFBBRRL
FFBFBBFLRL
FFBFFFBLRR
FBFFBFBLLR
BFBFFBBRRL
BFFBFFFLRR
BFBBBFBRRR
FFBFBBBLRR
BFFFFFBRRL
BFBFBFFRRL
FFBFFBBRRL
FBBBBFBLRL
BFBFFFFRLR
FBBBFFFRRR
BBFFBBFLRR
FFBFBFBRLR
FBBBFBBLRR
FFBFBBBRLL
BFFBBFFRRL
BBFFBFBRLL
FBBBFBBRRR
FBFFFFFLRL
FFBBFFBLLR
FBBFBBBRRL
FFFFBBBRLR
FBBFBBBRLR
FBBBBFBLLR
FFBFBFBLLL
BFFFBBFRRL
FBFFFBFRRL
BFBFBBBRRR
FBFBFBBLRL
BFBFBFFRRR
FFBBBBBRLL
FFFBBBFRRL
FFBBFFBLRR
BFBBBFBLRR
FBBBBBBLRR
FBFFFBBLLR
FBBFBFFLRL
FFBBBFFLRL
FFBFFBFLLR
FFFBBFFRRL
FFFFBFBRRL
FBFBBFBRRL
BFBBFFBLLL
BFFBFBBRRL
FFFFBBBRRL
BBFFFFFRRL
BFFBFFBRRL
FFFBBBFRLL
FFBFBBFRLR
BBFFFBFRLL
BFBBFBBRLL
FFFBFBBRRR
FBBFFFBRRL
FBFBFBFRLL
FBBFFFFRRR
BFBFFBFRRL
FBBBFBFLRR
FFBFBFBLRL
FFBBBBFLLR
FFFFFBFLLR
FFBFFFBLLL
BBFFBFFRLL
FFFBBBBRRR
FFBFFBBLRR
BFFFFFFLRL
FFFBBBFLRL
FBFFFBFLLR
BBFFFBFLRR
BFBFFFBRLR
BFFFBBFRLR
BFBFBBFRRL
BFBFFBBRRR
FFBFBBFLLL
FFFBBFBLLL
FFBBFFBRRL
FBFBBBFLLL
BFFFFFFRLR
BBFFFBFLLR
FBBFBFFRLL
FBFBFFBLRR
FBBFBFBRLR
FBFFBBBRRL
BFFBBFFRLL
BFFBFFBLLL
BFBBFBFRRL
FFFBFBFLRL
FFBBFBBRLR
FBBBBBFLRL
BBFFBFBRRL
FFFFBFBLRR
FFBFBFFRRL
FBFBBBBLRR
FFFFBFFRRR
BBFFBBFLRL
BFBBBBFLLL
FBBFBFBLRR
FBBBBBBRLL
FFFBBBBRLR
BBFFFFFRLR
FBFFFFFRLL
BFBBFFBLLR
BFFFFBFRLR
FFBBBFFRRL
FFBBBFFRLL
BFFBBFBLLL
FFFBBBFRRR
BFFFFFBRRR
FBFFFBFRLL
FBBFBFFLRR
BFFFFFBLRR
BFBFBFBLRL
BFBBBFFLRL
FFBFFFFRLL
FBFBFBFRLR
BFFFBBFRLL
FFBBFBFRLR
FBFBBFBLLR
FBFFBFFRRL
FBBBBFFLRR
BFBFBBBRLR
BFBBBFFRRL
FFFBFBBLRL
FBBBFFBLRR
BFBFFBFLRL
BFFFFFBRLL
BFFFBFFLLR
BFBBBFBRLR
FFFBFFFLRR
FBFBFBFRRL
FBFBFFFLRL
FBBFBBBLLR
BFBFFFFLLR
FBFFFBFLLL
FBBFBBFRLR
FBBFFFBRLL
FFFBFFBLRL
FBFBBFBRLR
BFBBFFFRRR
BFBFBFFRLL
FBFBFBBRRL
FFFBFBFRLL
FBBFFBBRLL
BFFBFBFLRL
BFFFFBBLLR
BFFFFFFRRR
BFBBBFBLRL
FFFBBFFLLL
FBBBFBBRLL
FFFBFFBLLL
FBFFFBFRRR
BFBFFBFLRR
FFBFFFFLRR
BFBBBFFRLR
FBFBFFBLLR
FFBFBBFRRR
FFBBFFBLLL
FFBFFFFLLL
BFFBBFBRRR
FFFBBBFLRR
FFBBFFBRLL
FFBBFBBLLL
BFFFFBFLLL
FFFBBFBRRR
FFBFFFFRRL
FFFBBFBRRL
BFFBBBFRRL
FBBBFFBRLL
FBFBFBBLLR
FBBBFBBRRL
BFBFBBBRRL
BFBFBFFLRR
BFFFFFBLLL
BFBFBBFRLL
FBBBBBFRLL
BFFFBFBLRL
FBFBBBBRLR
FBFFFBFLRL
FFBFBFFRRR
BFBFBFBLLL
BFBBFFBRLL
FBBBBBBLRL
BFFFBBFLLL
FBBFBBFLRL
FFFFBBFRLR
BFBFFFBRRR
FFBFBFFLRR
FFFFFBFLLL
FBFBFFFRRR
FBFFFBFRLR
FBFBBFBRLL
BFBFFBFRRR
BFBBFFBRLR
FBBBBFFRRL
BFBFFFFLLL
BFBFFFBLRR
FBFBBBBRRL
BFFFBFBLRR
FFBBFBFLLR
FBBBFFBRRL
FFFFFBFRLR
FFFBBFBLLR
FBFFBFFLRL
FFFBFBFRRR
BBFFBFBRRR
BFBBBBFLLR
BFBBBBBRLR
FFBBFFFLRL
FBBBFFBLLR
BFBFBFBLRR
FFBFBFFLLR
FBFFBFFRLL
FFBFFFBLLR
BFBBFBFRLR
FBBBFBBLLL
BFBBBBBRLL
FBFBFFBRLR
FFBBBFBLRR
FBBFFBFLLR
FFFBBFBLRL
FBFBBBFLRR
FBBBFFFLRR
FBFFFFBRRR
FFFFFBBLRL
FFBBBBFRLR
BFFBFBFRLL
BFFFFBFRLL
BFFFBFBLLL
BFBBBFBRLL
FFFBFFFRLR
FBBBFFBLLL
FBFBBFFLRR
FFFBFBBLRR
BFBFFFBRRL
FFBBBFFRRR
FBFFFFBRLL
FFFBBFBLRR
FFFFFFBRLR
FFFBBBBLLR
FFFBFFFRRL
FBBFFBBRRL
FBBBBFBLRR
FBFFBFBLLL
FFBFFFBRLR
BFFBBBFLLR
FFFBBBBRLL
FFFFBBBLRR
FFFBBFFLRR
BBFFFFFLLR
FFFFBFBLRL
BFBFBBBLRR
FBBFFFBRLR
FBFFFBBRLR
FBFFBBFLRR
BFBBFBFLRR
FBFBFFFRLL
BBFFBFFRRR
BFFBBBBRLL
BFBBFFBRRL
BFFBBFFLLR
BFBFBBFRRR
FBFBFBFLLL
FBBBFFFRLL
FFBBFBBRRR
FFBBBFFLLL
BFFBFFFLLL
BFBFFBBLRR
FFFFBFBRLR
FBFBBBFRRL
FFFFBBBLLL
FBBBBBFLLR
BFBFBBBLRL
BBFFFFFLRR
BFFBFFBLLR
BFBBFFBLRR
FBBBFBBLLR
FFBFBBFLRR
FBFBFBFLLR
FBFBFBBRRR
BFFFFBFLRL
FFFBFFFLRL
BBFFFBBRRR
BFBBFFFLRR
FFFFBFFLRL
BFFBBFFRRR
BFFBFBBRLR
FFBBBFBRRR
FFBBFBFRRL
FBBFFBFRRL
FBBFBBFRRL
FBBFBFBLLL
BFBBBBBRRL
FFBBFFFLLR
FFBBBFBLRL
FFBBFFFRLR
BFBBFBBLLR
FFBFFBFRRL
FBBBBFFLLL
BFFFFBFLRR
BFBBBFFRLL
BFBBBBBRRR
BBFFFFBLLL
FBFFFBBLRR
FBBBBFFRLR
FBBFBFFRLR
FBFBBFFRRL
FBFFFFBRRL"""
}
