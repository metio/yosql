# http://www.gnu.org/software/make/manual/make.html
# https://www.gnu.org/prep/standards/html_node/Makefile-Basics.html#Makefile-Basics
# http://clarkgrubb.com/makefile-style-guide

############
# PROLOGUE #
############
MAKEFLAGS += --warn-undefined-variables
SHELL = /bin/bash
.SHELLFLAGS := -eu -o pipefail -c
.DEFAULT_GOAL := all
.DELETE_ON_ERROR:
.SUFFIXES:

######################
# INTERNAL VARIABLES #
######################
TIMESTAMPED_VERSION := $(shell /bin/date "+%Y.%m.%d-%H%M%S")
CURRENT_DATE := $(shell /bin/date "+%Y-%m-%d")
USERNAME := $(shell id -u -n)
USERID := $(shell id -u)
GREEN  := $(shell tput -Txterm setaf 2)
WHITE  := $(shell tput -Txterm setaf 7)
YELLOW := $(shell tput -Txterm setaf 3)
RESET  := $(shell tput -Txterm sgr0)

######################
# INTERNAL FUNCTIONS #
######################
HELP_FUN = \
    %help; \
    while(<>) { push @{$$help{$$2 // 'targets'}}, [$$1, $$3] if /^([a-zA-Z\-]+)\s*:.*\#\#(?:@([a-zA-Z\-]+))?\s(.*)$$/ }; \
    print "usage: make [target]\n\n"; \
    for (sort keys %help) { \
    print "${WHITE}$$_:${RESET}\n"; \
    for (@{$$help{$$_}}) { \
    $$sep = " " x (32 - length $$_->[0]); \
    print "  ${YELLOW}$$_->[0]${RESET}$$sep${GREEN}$$_->[1]${RESET}\n"; \
    }; \
    print "\n"; }

###############
# GOALS/RULES #
###############
.PHONY: all
all: help

help: ##@other Show this help
	@perl -e '$(HELP_FUN)' $(MAKEFILE_LIST)

.PHONY: install
build: ##@hacking Build everything
	bazel build ...

.PHONY: test
test: ##@hacking Test everything
	bazel test ...

.PHONY: bench-lc-small-sample
bench-lc-small-sample: ##@benchmark Run full codegen lifecycle against small sample (runtime is ~5min)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.full_lifecycle.YoSqlWithSmallSampleBenchmark

.PHONY: bench-lc-medium-sample
bench-lc-medium-sample: ##@benchmark Run full codegen lifecycle against medium sample (runtime is ~15min)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.full_lifecycle.YoSqlWithMediumSampleBenchmark

.PHONY: bench-lc-big-sample
bench-lc-big-sample: ##@benchmark Run full codegen lifecycle against big sample (runtime is ~30min)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.full_lifecycle.YoSqlWithBigSampleBenchmark

.PHONY: bench-lc-large-sample
bench-lc-large-sample: ##@benchmark Run full codegen lifecycle against large sample (runtime is ~1h)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.full_lifecycle.YoSqlWithBigSampleBenchmark

.PHONY: bench-parsing-each
bench-parsing-each: ##@benchmark Run file parsing benchmark against each individual .sql file (runtime is ~30min)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.parse_file.DefaultSqlFileParserParseEachFileBenchmark

.PHONY: bench-parsing-all
bench-parsing-all: ##@benchmark Run file parsing benchmark against each individual .sql file (runtime is ~5min)
	bazel run //benchmarks/jmh:yosql-jmh-benchmark -- de.xn__ho_hia.yosql.benchmark.parse_file.DefaultSqlFileParserParseAllFileBenchmark

.PHONY: sign-waiver
sign-waiver: ##@contributing Sign the WAIVER
	gpg2 --no-version --armor --sign AUTHORS/WAIVER
	mv AUTHORS/WAIVER.asc AUTHORS/WAIVER-signed-by-$(USERNAME)-$(CURRENT_DATE).asc
