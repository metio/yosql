/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.*;

import static wtf.metio.yosql.i18n.I18nObjectMother.testTranslator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.annotationConfig;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.variableConfiguration;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.PROCESSING_API;

public final class GenericBlocksObjectMother {

    public static GenericBlocks genericBlocks() {
        return new DefaultGenericBlocks();
    }

    public static Names names() {
        return new DefaultNames();
    }

    public static Fields fields() {
        return new DefaultFields(annotationGenerator());
    }

    public static Parameters parameters() {
        return new DefaultParameters(names());
    }

    public static Variables variables() {
        return new DefaultVariables(variableConfiguration());
    }

    public static AnnotationGenerator annotationGenerator() {
        return new DefaultAnnotationGenerator(annotationConfig(PROCESSING_API), testTranslator());
    }
    
    public static ControlFlows controlFlows() {
        return new DefaultControlFlows(variables(), names());
    }

    private GenericBlocksObjectMother() {
        // factory class
    }

}
