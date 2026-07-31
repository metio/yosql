/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
