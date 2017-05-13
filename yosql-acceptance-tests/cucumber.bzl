def cucumber(yep, size="small", data=[]):
  native.filegroup(
    name = "yosql-acceptance-tests-yep-1-sql-files",
    srcs = native.glob(["src/main/yosql/YEP-%s/**/*.sql" % yep]),
  )
  native.genrule(
    name = "yosql-acceptance-tests-yep-%s-generated" % yep,
    srcs = [":yosql-acceptance-tests-yep-%s-sql-files" % yep],
    outs = [
      "yosql/yep_%s/PersonRepository.java" % yep,
      "yosql/yep_%s/SchemaRepository.java" % yep,
      "yosql/yep_%s/util/ResultRow.java" % yep,
      "yosql/yep_%s/util/ResultState.java" % yep,
      "yosql/yep_%s/util/FlowState.java" % yep,
      "yosql/yep_%s/converter/ToResultRowConverter.java" % yep,
    ],
    cmd = """
      $(location //yosql-cli) \
        --inputBaseDirectory yosql-acceptance-tests/src/main/yosql/YEP-%s/ \
        --outputBaseDirectory $(@D) \
        --basePackageName yosql.yep_%s \
        --logLevel off
    """ % (yep, yep),
    tools = ["//yosql-cli"],
  )
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
      "src/main/java/de/xn__ho_hia/yosql/acceptance/Yep%sSteps.java" % yep,
      ":yosql-acceptance-tests-yep-%s-generated" % yep,
    ],
    deps = [
      "//external:junit5_platform_commons",
      "//external:junit5_jupiter_api",
      "//external:cucumber",
      "//external:cucumber8",
      "//external:cucumber_core",
      "//external:h2",
      "//external:hikaricp",
      "//external:javapoet_type_guesser",
      "//external:reactive_streams",
      "//external:rxjava2",
      "//external:slf4j_api",
      "//external:logback_classic",
      "//external:logback_core",
    ],
    data = [
      "src/main/resources/YEP-%s/YEP-%s.feature" % (yep, yep)
    ],
    runtime_deps = [
      "//external:opentest4j",
      "//external:cucumber",
      "//external:cucumber8",
      "//external:cucumber_core",
      "//external:gherkin",
      "//external:cucumber-jvm-deps",
    ],
  )
