/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

-- generateBatchApi: true
-- parameters:
--   - name: name
--     type: java.lang.String
--   - name: date
--     type: java.lang.Long
INSERT INTO projects (NAME, DATESTARTED)
VALUES (:name, :date)
;
