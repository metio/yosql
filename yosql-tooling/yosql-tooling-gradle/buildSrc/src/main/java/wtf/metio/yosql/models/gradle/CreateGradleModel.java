/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.gradle;

import org.gradle.api.Action;
import org.gradle.api.Task;
import wtf.metio.yosql.model.generator.ModelGenerator;

public class CreateGradleModel implements Action<Task> {

    private final ModelGenerator generator;

    public CreateGradleModel(final ModelGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void execute(final Task task) {
        generator.createGradleModel();
    }

}
