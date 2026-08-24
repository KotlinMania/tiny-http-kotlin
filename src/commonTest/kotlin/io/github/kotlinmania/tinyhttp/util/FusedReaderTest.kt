// port-lint: source util/fused_reader.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FusedReaderTest {
    @Test
    fun testFusedReaderLifecycle() {
        val reader = FusedReader.new("sample payload")
        assertTrue(reader.hasInner())
        assertEquals("sample payload", reader.intoInner())
        reader.release()
        assertFalse(reader.hasInner())
        assertNull(reader.intoInner())
    }
}
