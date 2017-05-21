/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import javax.lang.model.element.Modifier;

/**
 * Typical type/field/method/parameter modifiers.
 */
public class TypicalModifiers {

    /**
     * @return The typical modifiers of an open class.
     */
    public static final Modifier[] openClass() {
        return new Modifier[] { Modifier.PUBLIC };
    }

    /**
     * @return The typical modifiers of a public class.
     */
    public static final Modifier[] publicClass() {
        return new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a public method.
     */
    public static final Modifier[] publicMethod() {
        return new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a public constructor.
     */
    public static final Modifier[] publicConstructor() {
        return new Modifier[] { Modifier.PUBLIC };
    }

    /**
     * @return The typical modifiers of a private field.
     */
    public static final Modifier[] privateField() {
        return new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a protected field.
     */
    public static final Modifier[] protectedField() {
        return new Modifier[] { Modifier.PROTECTED, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a constant field.
     */
    public static final Modifier[] constantField() {
        return new Modifier[] { Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a parameter.
     */
    public static final Modifier[] parmeter() {
        return new Modifier[] { Modifier.FINAL };
    }

}
