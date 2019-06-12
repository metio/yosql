/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.helpers;

import javax.lang.model.element.Modifier;

/**
 * Typical type/field/method/parameter modifiers.
 */
public final class TypicalModifiers {

    /**
     * @return The typical modifiers of an open class.
     */
    public static Modifier[] openClass() {
        return new Modifier[] { Modifier.PUBLIC };
    }

    /**
     * @return The typical modifiers of a public class.
     */
    public static Modifier[] publicClass() {
        return new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a public method.
     */
    public static Modifier[] publicMethod() {
        return new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a public constructor.
     */
    public static Modifier[] publicConstructor() {
        return new Modifier[] { Modifier.PUBLIC };
    }

    /**
     * @return The typical modifiers of a private field.
     */
    public static Modifier[] privateField() {
        return new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a protected field.
     */
    public static Modifier[] protectedField() {
        return new Modifier[] { Modifier.PROTECTED, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a constant field.
     */
    public static Modifier[] constantField() {
        return new Modifier[] { Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL };
    }

    /**
     * @return The typical modifiers of a parameter.
     */
    public static Modifier[] parmeter() {
        return new Modifier[] { Modifier.FINAL };
    }

}
