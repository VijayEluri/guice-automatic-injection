/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devsurf.injection.guice.enterprise;
/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */


import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.googlecode.rocoto.configuration.resolver.PropertiesResolver;

import de.devsurf.injection.guice.install.InstallationContext.BindingStage;
import de.devsurf.injection.guice.scanner.feature.BindingScannerFeature;

@Singleton
public class WebFilterBindingFeature extends BindingScannerFeature {
	@Override
	public BindingStage accept(Class<Object> annotatedClass, Map<String, Annotation> annotations) {
		if (annotations.containsKey(WebFilter.class.getName())) {
			return BindingStage.BINDING;
		}
		return BindingStage.IGNORE;
	}

	@Override
	public void process(final Class<Object> annotatedClass,
			final Map<String, Annotation> annotations) {
		final WebFilter annotation = (WebFilter) annotations.get(WebFilter.class.getName());
		String value = annotation.value()[0];
		final PropertiesResolver resolver = new PropertiesResolver(value);
		resolver.setInjector(injector);

		_binder.install(new ServletModule(){
			@Override
			protected void configureServlets() {
				String value = resolver.get();
				filter(value).through(annotatedClass.asSubclass(Filter.class));
			}
		});
	}
}