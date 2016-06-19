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

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

class GradleGroovyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.apply plugin: 'groovy'
        project.apply plugin: 'maven'
        project.apply plugin: 'com.jfrog.bintray'
        project.apply plugin: 'com.jfrog.artifactory'

        project.sourceCompatibility = JavaVersion.VERSION_1_7
        project.targetCompatibility = JavaVersion.VERSION_1_7

        def sourcesJar = project.tasks.create("sourcesJar", Jar) {
            from project.sourceSets.main.allSource.srcDirs
            classifier = 'sources'
        }

        def javadoc = project.tasks.getByName('javadoc')
        def javadocJar = project.tasks.create("javadocJar", Jar) { Jar task ->
            task.dependsOn javadoc
            task.classifier = 'javadoc'
            task.from javadoc.destinationDir
            task.encoding = 'UTF-8'
        }

        def groovydoc = project.tasks.getByName('groovydoc')
        def groovydocJar = project.tasks.create("groovydocJar", Jar) { Jar task ->
            task.dependsOn groovydoc
            task.classifier = 'groovydoc'
            task.from groovydoc.destinationDir
            task.encoding = 'UTF-8'
        }

        project.artifacts {
            it.archives sourcesJar
            it.archives javadocJar
            it.archives groovydocJar
        }
    }
}
