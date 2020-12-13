import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private fun findDiffOfOneAndThree(adapters: List<Int>): Pair<Int, Int> {
    var diffOfOne = 0
    var diffOfThree = 0
    adapters.zipWithNext().forEach {
        val diff = it.second - it.first
        if (diff == 1)
            diffOfOne++
        else
            diffOfThree++
    }
    return Pair(diffOfOne, diffOfThree)
}

class Day10 {

    private val exampleAdapters1 = exampleInput1.plus(0).plus(exampleInput1.maxOrNull()!! + 3).sorted()
    private val exampleAdapters2 = exampleInput2.plus(0).plus(exampleInput2.maxOrNull()!! + 3).sorted()
    private val realAdapters = realInput.plus(0).plus(realInput.maxOrNull()!! + 3).sorted()

    @Nested
    inner class PartA {
        @Nested
        inner class `Example data` {
            @Test
            fun `find value not a sum of two entries in 5 previous values`() {
                val (diffOfOne, diffOfThree) = findDiffOfOneAndThree(exampleAdapters1)
                println(diffOfOne * diffOfThree)
            }
        }

        @Nested
        inner class `Real data` {
            @Test
            fun `find value not a sum of two entries in 5 previous values`() {
                val (diffOfOne, diffOfThree) = findDiffOfOneAndThree(realAdapters)
                println(diffOfOne * diffOfThree)
            }
        }
    }

    @Nested
    inner class PartB {
        @Nested
        inner class `Example data` {
            @Test
            fun `find count of distinct arrangements`() {

            }
        }
    }

    companion object {
        val exampleInput1 =
"""16
10
15
5
1
11
7
19
6
12
4""".lines().map { it.toInt() }

    val exampleInput2 =
"""28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3""".lines().map { it.toInt() }

        val realInput =
"""79
142
139
33
56
133
138
61
125
88
158
123
65
69
105
6
81
31
60
70
159
114
71
15
13
72
118
14
9
93
162
140
165
1
80
148
32
53
102
5
68
101
111
85
45
25
16
59
131
23
91
92
115
103
166
22
145
161
108
155
135
55
86
34
37
78
28
75
7
104
121
24
153
167
95
87
94
134
154
84
151
124
62
49
38
39
54
109
128
19
2
98
122
132
141
168
8
160
50
42
46
110
12
152""".lines().map { it.toInt() }
    }
}
