package wtf.metio.yosql.generator.blocks.jdbc;

final class DefaultJdbcNames implements JdbcNames {

    @Override
    public String dataSource() {
        return "dataSource";
    }

    @Override
    public String connection() {
        return "connection";
    }

    @Override
    public String statement() {
        return "statement";
    }

    @Override
    public String metaData() {
        return "metaData";
    }

    @Override
    public String resultSet() {
        return "resultSet";
    }

    @Override
    public String columnCount() {
        return "columnCount";
    }

    @Override
    public String columnLabel() {
        return "columnLabel";
    }

    @Override
    public String batch() {
        return "batch";
    }

    @Override
    public String list() {
        return "list";
    }

    @Override
    public String jdbcIndex() {
        return "jdbcIndex";
    }

    @Override
    public String index() {
        return "index";
    }

    @Override
    public String row() {
        return "row";
    }

}
