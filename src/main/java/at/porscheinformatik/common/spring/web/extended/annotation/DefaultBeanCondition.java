/**
 * Copyright 2014 Daniel Furtlehner
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
package at.porscheinformatik.common.spring.web.extended.annotation;

import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DefaultBeanCondition implements ConfigurationCondition
{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)
    {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(DefaultBean.class.getName());

        Class<?>[] classes = (Class<?>[]) annotationAttributes.get("value");
        ConfigurableListableBeanFactory factory = context.getBeanFactory();

        for (Class<?> c : classes)
        {
            String[] beanNamesForType = factory.getBeanNamesForType(c, false, false);

            if (beanNamesForType != null)
            {
                for (String beanName : beanNamesForType)
                {
                    if (factory.containsBean(beanName))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ConfigurationPhase getConfigurationPhase()
    {
        return ConfigurationPhase.REGISTER_BEAN;
    }

}
