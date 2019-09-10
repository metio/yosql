package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public interface JdbcParameters {

    ParameterSpec dataSource();

    ParameterSpec connection();

    ParameterSpec preparedStatement();

    ParameterSpec resultSet();

    ParameterSpec metaData();

    ParameterSpec columnCount();

    ParameterSpec index();

    ParameterSpec columnLabel();

}
