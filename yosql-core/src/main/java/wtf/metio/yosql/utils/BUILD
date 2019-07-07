java_library(
    name = "named_thread_factory",
    srcs = [
        "NamedForkJoinWorkerThreadFactory.java",
    ],
    deps = [
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:i18n",
    ],
)

java_library(
    name = "thread_pool_factory",
    srcs = [
        "ThreadPoolFactory.java",
    ],
    deps = [
        ":named_thread_factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:execution",
    ],
)

java_library(
    name = "timer",
    srcs = [
        "Timer.java",
    ],
    visibility = [
        "//yosql-benchmarks/src/main/java/wtf/metio/yosql/benchmark:__subpackages__",
        "//yosql-core/src/main/java/wtf/metio/yosql:__pkg__",
    ],
    deps = [
        ":named_thread_factory",
        "//yosql-core/src/main/java/wtf/metio/yosql/model:events",
        "//yosql-core/src/main/java/wtf/metio/yosql/model/annotations:loggers",
        "@maven//:ch_qos_cal10n_cal10n_api",
        "@maven//:javax_inject_javax_inject",
        "@maven//:org_slf4j_slf4j_ext",
    ],
)
