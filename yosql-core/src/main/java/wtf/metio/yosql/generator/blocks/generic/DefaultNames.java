/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

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
