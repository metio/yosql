def cucumber(yep, size="small", data=[]):
  native.filegroup(
    name = "yosql-acceptance-tests-yep-%s-sql-files" % yep,
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
      "@junit_platform_commons//jar",
      "@junit_jupiter_api//jar",
      "@info_cukes_cucumber_java//jar",
      "@info_cukes_cucumber_java8//jar",
      "@info_cukes_cucumber_core//jar",
      "@com_h2database_h2//jar",
      "@com_zaxxer_hikaricp//jar",
      "@javapoet_type_guesser//jar",
      "@org_reactivestreams_reactive_streams//jar",
      "@io_reactivex_rxjava2_rxjava2//jar",
      "@slf4j_api//jar",
      "@logback_classic//jar",
      "@logback_core//jar",
    ],
    data = [
      "src/main/resources/YEP-%s/YEP-%s.feature" % (yep, yep)
    ],
    runtime_deps = [
      "@opentest4j//jar",
      "@info_cukes_cucumber_java//jar",
      "@info_cukes_cucumber_java8//jar",
      "@info_cukes_cucumber_core//jar",
      "@info_cukes_gherkin//jar",
      "@info_cukes_cucumber_jvm_deps//jar",
    ],
  )
