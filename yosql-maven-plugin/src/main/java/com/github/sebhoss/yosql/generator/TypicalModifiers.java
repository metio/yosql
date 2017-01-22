package com.github.sebhoss.yosql.generator;

import javax.lang.model.element.Modifier;

public class TypicalModifiers {

    public static final Modifier[] PUBLIC_CLASS   = new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    public static final Modifier[] PUBLIC_METHOD  = new Modifier[] { Modifier.PUBLIC, Modifier.FINAL };
    public static final Modifier[] PRIVATE_METHOD = new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
    public static final Modifier[] CONSTANT_FIELD = new Modifier[] { Modifier.PRIVATE, Modifier.STATIC,
            Modifier.FINAL };
    public static final Modifier[] PARAMETER      = new Modifier[] { Modifier.FINAL };

}
