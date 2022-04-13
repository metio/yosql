/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.logging;

/**
 * Enumeration of known loggers.
 */
public enum Loggers {

  /**
   * Logger for file writers.
   */
  WRITER("yosql.writer"),

  /**
   * Logger for file parsers.
   */
  PARSER("yosql.parser"),

  /**
   * Logger for file readers.
   */
  READER("yosql.reader"),

  /**
   * Logger for file generators.
   */
  GENERATOR("yosql.generator"),

  /**
   * Logger for timers.
   */
  TIMER("yosql.timer"),

  /**
   * Logger for utilities.
   */
  UTILITIES("yosql.utilities"),

  /**
   * Logger for executions
   */
  EXECUTIONS("yosql.executions");

  public final String loggerName;

  Loggers(String loggerName) {
    this.loggerName = loggerName;
  }

}
