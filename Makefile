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
GREEN  := $(shell tput -Txterm setaf 2)
WHITE  := $(shell tput -Txterm setaf 7)
YELLOW := $(shell tput -Txterm setaf 3)
RESET  := $(shell tput -Txterm sgr0)

######################
# INTERNAL FUNCTIONS #
######################
HELP_FUN = \
	%help; \
	while(<>) { push @{$$help{$$2 // 'targets'}}, [$$1, $$3] if /^([a-zA-Z0-9\-]+)\s*:.*\#\#(?:@([a-zA-Z\-]+))?\s(.*)$$/ }; \
	print "usage: make [target]\n\n"; \
	for (sort keys %help) { \
	print "${WHITE}$$_:${RESET}\n"; \
	for (@{$$help{$$_}}) { \
	$$sep = " " x (32 - length $$_->[0]); \
	print "  ${YELLOW}$$_->[0]${RESET}$$sep${GREEN}$$_->[1]${RESET}\n"; \
	}; \
	print "\n"; }

###############
# GOALS/RULES/TARGETS #
###############
.PHONY: all
all: help

.PHONY: help
help: ##@other Show this help
	@perl -e '$(HELP_FUN)' $(MAKEFILE_LIST)

.PHONY: build
build: ##@hacking Build everything
	mvn verify

.PHONY: watch
watch: ##@hacking Watch for changes and build everything
	ag -l | entr mvn verify

.PHONY: test
test: ##@hacking Test everything
	mvn verify

.PHONY: benchmarks
benchmarks: ##@benchmarks Run all benchmarks
	mvn verify --activate-profiles benchmarks

.PHONY: benchmarks-codegen
benchmarks-codegen: ##@benchmarks Run code generation benchmarks
	mvn verify --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make --activate-profiles benchmarks

.PHONY: benchmarks-dao
benchmarks-dao: ##@benchmarks Run DAO benchmarks
	mvn verify --projects yosql-benchmarks/yosql-benchmarks-dao --also-make --activate-profiles benchmarks
