/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.tests.ObjectMother;

public final class GenericBlocksObjectMother extends ObjectMother {

    public static GenericBlocks genericBlocks() {
        return yoSqlComponent().genericBlocks();
    }

    public static Names names() {
        return yoSqlComponent().names();
    }

    public static Fields fields() {
        return yoSqlComponent().fields();
    }

    public static Parameters parameters() {
        return yoSqlComponent().parameters();
    }

    public static Variables variables() {
        return yoSqlComponent().variables();
    }

    public static AnnotationGenerator annotationGenerator() {
        return yoSqlComponent().annotationGenerator();
    }

    public static ControlFlows controlFlows() {
        return yoSqlComponent().controlFlows();
    }

}
