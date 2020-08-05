# This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
# directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
# including this file, may be copied, modified, propagated, or distributed except according to the terms contained
# in the LICENSE file.

genrule(
    name = "build_info",
    outs = ["BuildInfo.java"],
    cmd = """
    cat > $@ <<EOF
package wtf.metio.yosql.cli;
public class BuildInfo {
  public static final String VERSION = "$(RELEASE_VERSION)";
  public static final String BUILD = "$(BUILD_NUMBER)";
  public static final String REVISION = "$(GIT_REVISION)";
  public static final String BUILD_BY = "$(BUILD_BY)";
  public static final String BUILD_AT = "$(BUILD_AT)";
}
EOF
""",
)

java_library(
    name = "build",
    srcs = [":build_info"],
)
