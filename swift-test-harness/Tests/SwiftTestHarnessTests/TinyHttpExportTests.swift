import Testing
import TinyHttp

@Suite("TinyHttp Swift Export Tests")
struct TinyHttpExportTests {
    @Test("HTTP dates round-trip through the Kotlin export")
    func epochDateUsesHttpFormat() {
        #expect(formatHttpDate(epochSeconds: 0) == "Thu, 01 Jan 1970 00:00:00 GMT")
    }
}
