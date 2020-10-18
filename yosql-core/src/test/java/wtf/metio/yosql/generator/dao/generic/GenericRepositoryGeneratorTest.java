/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.dao.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlObjectMother;
import wtf.metio.yosql.test.ObjectMother;

@DisplayName("GenericRepositoryGenerator")
class GenericRepositoryGeneratorTest {

    private GenericRepositoryGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new GenericRepositoryGenerator(
                ObjectMother.generatorLocLogger(),
                ObjectMother.annotationGenerator(),
                ObjectMother.classes(),
                ObjectMother.javadoc(),
                ObjectMother.jdbcFieldsGenerator(),
                ObjectMother.jdbcMethodsGenerator());
    }

    @Test
    void generateRepository() {
        Assertions.assertEquals("""
                /**
                 * Generated based on the following file(s):
                 * <ul>
                 * <li>/some/path/query.sql</li>
                 * </ul>
                 */
                @javax.annotation.processing.Generated(
                    value = "YoSQL",
                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                )
                public final class Test {
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                                
                  /**
                   * Generated based on the following file:
                   * <ul>
                   * <li>/some/path/query.sql</li>
                   * </ul>
                   */
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                                
                  /**
                   * Generated based on the following file:
                   * <ul>
                   * <li>/some/path/query.sql</li>
                   * </ul>
                   */
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                                
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                                
                  static {
                    QUERY_TEST_INDEX.put("test", new int[] { 0 });
                  }
                                
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  private final javax.sql.DataSource dataSource;
                                
                  @javax.annotation.processing.Generated(
                      value = "YoSQL",
                      comments = "DO NOT MODIFY - automatically generated by YoSQL"
                  )
                  public Test(final javax.sql.DataSource dataSource) {
                    this.dataSource = dataSource;
                  }
                }
                """, generator.generateRepository("Test", SqlObjectMother.sqlStatements()).getType().toString());
    }

}