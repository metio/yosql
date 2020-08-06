package wtf.metio.yosql.generator.blocks.generic;

import wtf.metio.yosql.generator.blocks.api.Names;

final class DefaultNames implements Names {

    // TODO: read from configuration

    @Override
    public String logger() {
        return "LOG";
    }

    @Override
    public String query() {
        return "query";
    }

    @Override
    public String rawQuery() {
        return "rawQuery";
    }

    @Override
    public String executedQuery() {
        return "executedQuery";
    }

    @Override
    public String databaseProductName() {
        return "databaseProductName";
    }

    @Override
    public String action() {
        return "action";
    }

    @Override
    public String result() {
        return "result";
    }

    @Override
    public String value() {
        return "value";
    }

    @Override
    public String emitter() {
        return "emitter";
    }

    @Override
    public String name() {
        return "name";
    }

    @Override
    public String state() {
        return "state";
    }

    @Override
    public String exception() {
        return "exception";
    }

}
