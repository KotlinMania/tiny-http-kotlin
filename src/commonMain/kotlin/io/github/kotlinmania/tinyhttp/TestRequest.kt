// port-lint: source test.rs
package io.github.kotlinmania.tinyhttp

/**
 * A simpler version of request parameters that is useful for testing. No data actually goes anywhere.
 *
 * By default, `TestRequest` pretends to be an insecure GET request for the server root (`/`)
 * with no headers. To create a `TestRequest` with different parameters, use the builder pattern:
 *
 * ```kotlin
 * val request = TestRequest.new()
 *     .withMethod(Method.Post)
 *     .withPath("/api/widgets")
 *     .withBody("42")
 * ```
 */
class TestRequest(
    var body: String = "",
    var remoteAddr: String = "127.0.0.1:23456",
    var secure: Boolean = false,
    var method: Method = Method.Get,
    var path: String = "/",
    var httpVersion: HTTPVersion = HTTPVersion(1, 1),
    headers: List<Header> = emptyList(),
) {
    private val mutableHeaders: MutableList<Header> = headers.toMutableList()
    val headers: List<Header> get() = mutableHeaders

    fun withBody(body: String): TestRequest {
        this.body = body
        return this
    }

    fun withRemoteAddr(remoteAddr: String): TestRequest {
        this.remoteAddr = remoteAddr
        return this
    }

    fun withHttps(): TestRequest {
        this.secure = true
        return this
    }

    fun withMethod(method: Method): TestRequest {
        this.method = method
        return this
    }

    fun withPath(path: String): TestRequest {
        this.path = path
        return this
    }

    fun withHttpVersion(version: HTTPVersion): TestRequest {
        this.httpVersion = version
        return this
    }

    fun withHeader(header: Header): TestRequest {
        this.mutableHeaders.add(header)
        return this
    }

    companion object {
        fun new(): TestRequest = TestRequest()
    }
}
