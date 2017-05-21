/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

@SuppressWarnings({ "nls", "javadoc" })
public class TypicalNames {

    public static final String DATA_SOURCE           = "dataSource";
    public static final String CONNECTION            = "connection";
    public static final String SQL_STATEMENT         = "sqlStatement";
    public static final String STATEMENT             = "statement";
    public static final String RESULT_SET            = "resultSet";
    public static final String PATTERN               = "pattern";
    public static final String MATCHER               = "matcher";
    public static final String EXCEPTION             = "exception";
    public static final String META_DATA             = "metaData";
    public static final String COLUMN_COUNT          = "columnCount";
    public static final String NAME                  = "name";
    public static final String STATE                 = "state";
    public static final String EMITTER               = "emitter";
    public static final String VALUE                 = "value";
    public static final String INDEX                 = "index";
    public static final String INDICES               = "indices";
    public static final String JDBC_INDEX            = "jdbcIndex";
    public static final String BATCH                 = "batch";
    public static final String ROW                   = "row";
    public static final String RESULT                = "result";
    public static final String RESULT_LIST           = "resultList";
    public static final String LIST                  = "list";
    public static final String ACTION                = "action";
    public static final String DATABASE_PRODUCT_NAME = "databaseProductName";
    public static final String QUERY                 = "query";
    public static final String RAW_QUERY             = "rawQuery";
    public static final String EXECUTED_QUERY        = "executedQuery";
    public static final String COLUMN_LABEL          = "columnLabel";
    public static final String LOGGER                = "LOG";

    public static final String getPackageName(final String repositoryName) {
        return repositoryName.substring(0, repositoryName.lastIndexOf('.'));
    }

    public static final String getClassName(final String repositoryName) {
        return repositoryName.substring(repositoryName.lastIndexOf('.') + 1,
                repositoryName.length());
    }

}
