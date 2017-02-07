package com.github.sebhoss.yosql.generator.helpers;

import javax.lang.model.element.Modifier;

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
