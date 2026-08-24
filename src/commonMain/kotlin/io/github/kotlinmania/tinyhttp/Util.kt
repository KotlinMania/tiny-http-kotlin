// port-lint: source util/mod.rs
package io.github.kotlinmania.tinyhttp

/**
 * Parses the value of a header.
 * Suitable for `Accept-*`, `TE`, etc.
 *
 * For example with `text/plain, image/png; q=1.5` this function returns
 * `[("text/plain", 1.0f), ("image/png", 1.5f)]`
 */
fun parseHeaderValue(input: String): List<Pair<String, Float>> {
    val results = ArrayList<Pair<String, Float>>()
    for (elem in input.split(',')) {
        val params = elem.split(';')
        if (params.isEmpty()) continue
        val t = params[0].trim()
        if (t.isEmpty()) continue

        var value = 1.0f
        for (i in 1 until params.size) {
            val p = params[i].trimStart()
            if (p.startsWith("q=")) {
                val qStr = p.substring(2).trim()
                val parsed = qStr.toFloatOrNull()
                if (parsed != null) {
                    value = parsed
                    break
                }
            }
        }
        results.add(Pair(t, value))
    }
    return results
}
