filegroup(
  name = "yosql-acceptance-tests-raw-sources",
  srcs = glob(["src/main/java/**/*.java"]),
)
filegroup(
  name = "feature-files",
  srcs = glob(["src/main/resources/**/*.feature"]),
)

java_test(
    name = "yosql-acceptance-tests",
    main_class = "cucumber.api.cli.Main",
    use_testrunner = False,
    size = "small",
    args = [
      "--glue de.xn__ho_hia.yosql.acceptance",
      "yosql-acceptance-tests/src/main/resources/YEP-1/YEP-1.feature"
    ],
    srcs = [
      ":yosql-acceptance-tests-raw-sources"
    ],
    deps = [
      "//external:junit5_jupiter_api",
      "//external:cucumber",
      "//external:cucumber8",
      "//external:cucumber_core",
    ],
    data = [
      ":feature-files"
    ],
    runtime_deps = [
      "//external:junit5_jupiter_engine",
      "//external:junit5_platform_console",
      "//external:junit5_platform_commons",
      "//external:junit5_platform_engine",
      "//external:junit5_platform_launcher",
      "//external:opentest4j",
      "//external:cucumber",
      "//external:cucumber8",
      "//external:cucumber_core",
      "//external:gherkin",
      "//external:cucumber-jvm-deps",
    ],
)