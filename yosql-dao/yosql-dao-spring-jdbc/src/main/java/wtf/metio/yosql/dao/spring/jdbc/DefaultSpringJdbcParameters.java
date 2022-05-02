/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

public final class DefaultSpringJdbcParameters implements SpringJdbcParameters {

    private final Parameters parameters;
    private final NamesConfiguration names;

    public DefaultSpringJdbcParameters(final Parameters parameters, final NamesConfiguration names) {
        this.parameters = parameters;
        this.names = names;
    }

    @Override
    public ParameterSpec namedParameterJdbcTemplate() {
        return parameters.parameter(NamedParameterJdbcTemplate.class, names.jdbcTemplate());
    }

    @Override
    public ParameterSpec converter(final ResultRowConverter converter) {
        return parameters.parameter(ClassName.bestGuess(converter.converterType()), converter.alias());
    }

}
