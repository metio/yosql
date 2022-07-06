/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--@yosql sqlStatementSeparator: ;;

-- name: createNextPrimeFunction
CREATE ALIAS NEXT_PRIME AS '
String nextPrime(String value) {
    return new BigInteger(value).nextProbablePrime().toString();
}
';;

-- name: createRandomNumberFunction
CREATE ALIAS RANDOM_NUMBER AS '
int randomNumber() {
    return 4;
}
';;

-- name: createNamesFunction
CREATE ALIAS NAMES AS '
List<String> names() {
    return List.of("alice", "bob");
}
';;
