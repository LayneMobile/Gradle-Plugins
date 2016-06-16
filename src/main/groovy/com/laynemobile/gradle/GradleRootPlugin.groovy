/*
 * Copyright 2016 Layne Mobile, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laynemobile.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class GradleRootPlugin implements Plugin<Project> {
    static def isReleaseBuild(def gradle) {
        // brittle but works
        return gradle.startParameter.taskNames.contains('bintrayUpload')
    }

    @Override
    void apply(Project target) {
        def GROUP = target.GROUP
        def VERSION_NAME = target.VERSION_NAME
        def isReleaseBuild = isReleaseBuild(target.gradle)

        target.subprojects {
            group = GROUP
            version = isReleaseBuild ? VERSION_NAME : "${VERSION_NAME}-SNAPSHOT"
            logger.info("Project ${it.name} using version=${it.version}")
        }
    }
}
