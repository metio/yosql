##@ hacking
.PHONY: install
install: ## Build everything and install artifacts locally
	mvn verify

.PHONY: test
test: ## Test everything
	mvn verify

##@ benchmarks
.PHONY: benchmarks
benchmarks: ## Run all benchmarks
	mvn verify --activate-profiles benchmarks

.PHONY: benchmarks-codegen
benchmarks-codegen: ## Run code generation benchmarks
	mvn verify --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make --activate-profiles benchmarks

.PHONY: benchmarks-dao
benchmarks-dao: ## Run DAO benchmarks
	mvn verify --projects yosql-benchmarks/yosql-benchmarks-dao --also-make --activate-profiles benchmarks
