/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import javax.lang.model.element.Modifier;

@SuppressWarnings({ "javadoc" })
public class TypicalModifiers {

    public static final Modifier[] OPEN_CLASS         = new Modifier[] { Modifier.PUBLIC };
    public static final Modifier[] PUBLIC_CLASS       = new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    public static final Modifier[] PUBLIC_METHOD      = new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    public static final Modifier[] PUBLIC_CONSTRUCTOR = new Modifier[] { Modifier.PUBLIC };
    public static final Modifier[] PRIVATE_METHOD     = new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
    public static final Modifier[] PRIVATE_FIELD      = new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
    public static final Modifier[] PROTECTED_FIELD    = new Modifier[] { Modifier.PROTECTED, Modifier.FINAL };
    public static final Modifier[] PUBLIC_FIELD       = new Modifier[] { Modifier.PUBLIC };
    public static final Modifier[] CONSTANT_FIELD     = new Modifier[] { Modifier.PRIVATE, Modifier.STATIC,
            Modifier.FINAL };
    public static final Modifier[] PARAMETER          = new Modifier[] { Modifier.FINAL };

}
