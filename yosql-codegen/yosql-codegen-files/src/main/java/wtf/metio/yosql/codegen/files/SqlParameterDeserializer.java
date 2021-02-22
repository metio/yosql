/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import wtf.metio.yosql.models.sql.SqlParameter;

import java.io.IOException;

public final class SqlParameterDeserializer extends JsonDeserializer<SqlParameter> {

    @Override
    public SqlParameter deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context) throws IOException {
        final var parameter = jsonParser.readValueAs(Parameter.class);
        return SqlParameter.builder()
                .setConverter(parameter.converter == null ? "" : parameter.converter)
                .setName(parameter.name == null ? "" : parameter.name)
                .setType(parameter.type == null ? "" : parameter.type)
                .setIndices(parameter.indices == null ? new int[0] : parameter.indices)
                .build();
    }

    private static final class Parameter {
        public String name;
        public String type;
        public String converter;
        public int[] indices;
    }

}
