/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.io.IOException;

public final class ResultRowConverterDeserializer extends JsonDeserializer<ResultRowConverter> {

    @Override
    public ResultRowConverter deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context) throws IOException {
        final var converter = jsonParser.readValueAs(Converter.class);
        return ResultRowConverter.builder()
                .setMethodName(converter.methodName == null ? "" : converter.methodName)
                .setResultType(converter.resultType == null ? "" : converter.resultType)
                .setConverterType(converter.converterType == null ? "" : converter.converterType)
                .setAlias(converter.alias == null ? "" : converter.alias)
                .build();
    }

    private static final class Converter {
        public String alias;
        public String converterType;
        public String methodName;
        public String resultType;
    }

}
