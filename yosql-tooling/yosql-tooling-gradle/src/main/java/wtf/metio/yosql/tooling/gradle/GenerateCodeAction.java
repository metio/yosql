/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskProvider;

public final class GenerateCodeAction implements Action<Task> {

    private final TaskProvider<GenerateCodeTask> generateTask;

    public GenerateCodeAction(final TaskProvider<GenerateCodeTask> generateTask) {
        this.generateTask = generateTask;
    }

    @Override
    public void execute(final Task task) {
        generateTask.get().generateCode();
    }

}
