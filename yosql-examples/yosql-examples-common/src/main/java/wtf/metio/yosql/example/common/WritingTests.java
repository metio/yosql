/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.example.common;

import org.immutables.value.Value;

import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

@Value.Immutable
public interface WritingTests {

    System.Logger LOG = System.getLogger(WritingTests.class.getName());

    static ImmutableWritingTests.InsertCompanyBuildStage builder() {
        return ImmutableWritingTests.builder();
    }

    ToIntBiFunction<Integer, String> insertCompany();

    ToIntBiFunction<Object, Object> insertPerson();

    BiFunction<int[], String[], int[]> insertCompanyBatch();

    BiFunction<Object[], String[], int[]> insertPersonBatch();

    ToIntBiFunction<Object, Object> updateUser();

    ToIntBiFunction<Object, Object> insertUser();

    BiFunction<Object[], Object[], int[]> insertUserBatch();

    ToIntBiFunction<Integer, String> insertItem();

    BiFunction<int[], String[], int[]> insertItemBatch();

    @Value.Lazy
    default void runWritingTests() {
        try {
            insertCompany().applyAsInt(1, "one");
            insertCompany().applyAsInt(2, "two");
            insertCompany().applyAsInt(3, "three");

            insertPerson().applyAsInt(1, "eve");
            insertPerson().applyAsInt(2, "adam");
            insertPerson().applyAsInt(3, "bob");

            insertUser().applyAsInt(-1, "special user");
            insertUser().applyAsInt(1, "admin");
            insertUser().applyAsInt(2, "not-an-admin");
            insertUser().applyAsInt(3, "regular-user");

            insertItem().applyAsInt(1, "iPhone 47 eXtreme");
            insertItem().applyAsInt(2, "Android 49");
            insertItem().applyAsInt(3, "GenericPhone 38");

            insertCompanyBatch().apply(new int[]{4, 5, 6}, new String[]{"three", "five", "six"});
            insertPersonBatch().apply(new Object[]{4, 5, 6}, new String[]{"alice", "frank", "joe"});
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running WRITING tests", exception);
            System.exit(1);
        }
    }

}
