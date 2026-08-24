// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SwiftTestHarness",
    platforms: [.macOS(.v14)],
    dependencies: [
        .package(name: "TinyHttp", path: "../build/SPMPackage/macosArm64/Debug")
    ],
    targets: [
        .executableTarget(
            name: "SwiftTestHarnessTests",
            dependencies: [
                .product(name: "TinyHttpLibrary", package: "TinyHttp")
            ],
            path: "Tests/SwiftTestHarnessTests",
            linkerSettings: [
                .unsafeFlags([
                    "-L", "../build/swift-test",
                    "-lTinyHttp",
                ]),
            ]
        ),
    ]
)
