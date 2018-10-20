/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.kotlin.dsl.accessors.runtime

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionAware


fun extensionOf(target: Any, extensionName: String): Any =
    (target as ExtensionAware).extensions.getByName(extensionName)


fun conventionPluginOf(target: Any, name: String) =
    conventionPluginByName(conventionOf(target), name)


fun conventionPluginByName(convention: Convention, name: String): Any =
    convention.plugins[name] ?: throw IllegalStateException("A convention named '$name' could not be found.")


fun conventionOf(target: Any): Convention = when (target) {
    is Project -> target.convention
    is HasConvention -> target.convention
    else -> throw IllegalStateException("Object `$target` doesn't support conventions!")
}


fun addDependencyTo(
    dependencies: DependencyHandler,
    configuration: String,
    dependencyNotation: Any,
    configurationAction: Action<in Any>
): Dependency = dependencies.run {
    create(dependencyNotation).also { dependency ->
        configurationAction.execute(dependency)
        add(configuration, dependency)
    }
}