def cucumber(yep, size="small", data=[]):
  native.java_test(
    name = "YEP-%s" % yep,
    main_class = "cucumber.api.cli.Main",
    use_testrunner = False,
    size = size,
    args = [
      "--glue de.xn__ho_hia.yosql.acceptance",
      "yosql-acceptance-tests/src/main/resources/YEP-%s/YEP-%s.feature" % (yep, yep)
    ],
    srcs = [
      "src/main/java/de/xn__ho_hia/yosql/acceptance/Yep%sSteps.java" % yep
    ],
    deps = [
      "//external:junit5_jupiter_api",
      "//external:cucumber",
      "//external:cucumber8",
      "//external:cucumber_core",
    ],
    data = [
      "src/main/resources/YEP-%s/YEP-%s.feature" % (yep, yep)
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
