java_plugin(
    name = "dagger_plugin",
    generates_api = True,
    processor_class = "dagger.internal.codegen.ComponentProcessor",
    deps = [
        "@maven//:com_google_dagger_dagger",
        "@maven//:com_google_dagger_dagger_compiler",
        "@maven//:com_google_dagger_dagger_producers",
    ],
)

java_library(
    name = "dagger",
    exported_plugins = [":dagger_plugin"],
    visibility = ["//visibility:public"],
    exports = [
        "@maven//:com_google_dagger_dagger",
        "@maven//:com_google_dagger_dagger_compiler",
        "@maven//:com_google_dagger_dagger_producers",
    ],
)
