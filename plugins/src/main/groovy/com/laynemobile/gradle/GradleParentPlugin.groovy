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

class GradleParentPlugin implements Plugin<Project> {
    static def isReleaseBuild(def gradle) {
        // brittle but works
        return gradle.startParameter.taskNames.contains('bintrayUpload')
    }

    @Override
    void apply(Project target) {
        def String artify = 'com.jfrog.artifactory'
        if (!target.plugins.findPlugin(artify)) {
            target.apply plugin: artify
        }

        target.artifactory {
            contextUrl = 'https://oss.jfrog.org'

            publish {
                it.repository {
                    it.repoKey = 'oss-snapshot-local'

                    if (target.hasProperty('bintrayUser')) {
                        it.username = target.property('bintrayUser')
                        it.password = target.property('bintrayKey')
                    }
                }

                it.defaults {
                    it.publishConfigs('archives')
                }
            }
            resolve {
                it.repoKey = 'jcenter'
            }
        }

        def GROUP = target.GROUP
        def VERSION_NAME = target.VERSION_NAME
        def isReleaseBuild = isReleaseBuild(target.gradle)

        target.allprojects {
            group = GROUP
            version = isReleaseBuild ? VERSION_NAME : "${VERSION_NAME}-SNAPSHOT"
            logger.info("Project ${it.name} using version=${it.version}")

            repositories {
                it.jcenter()
                it.mavenLocal()
            }
        }
    }
}
