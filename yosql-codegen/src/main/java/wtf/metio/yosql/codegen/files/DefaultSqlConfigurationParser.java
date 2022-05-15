/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;

/**
 * Default implementation of a {@link SqlConfigurationParser} that works with YAML based configs.
 */
public final class DefaultSqlConfigurationParser implements SqlConfigurationParser {

    @Override
    public SqlConfiguration parseConfig(final String yaml) {
        SqlConfiguration configuration = null;
        try {
            if (!Strings.isBlank(yaml)) {
                final var yoSqlModule = new SimpleModule();
                yoSqlModule.addDeserializer(SqlParameter.class, new SqlParameterDeserializer());
                yoSqlModule.addDeserializer(ResultRowConverter.class, new ResultRowConverterDeserializer());
                final var yamlMapper = YAMLMapper.builder()
                        .addModule(new Jdk8Module())
                        .addModule(yoSqlModule)
                        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                        .build();
                configuration = yamlMapper.readValue(yaml, SqlConfiguration.class);
            }
        } catch (final Exception exception) {
            // TODO: add exception to execution errors
            throw new RuntimeException(exception);
        }
        return Optional.ofNullable(configuration)
                .orElseGet(() -> SqlConfiguration.usingDefaults().build());
    }

}
