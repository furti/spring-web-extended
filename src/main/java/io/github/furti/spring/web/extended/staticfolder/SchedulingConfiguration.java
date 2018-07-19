/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author Daniel Furtlehner
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer
{
    @Autowired
    private StaticFolderConfigurerConfiguration configurerConfiguration;

    @Autowired
    private StaticFolderCache staticFolderCache;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        long interval = configurerConfiguration.getStaticFolderRegistry().getResourceRefreshInterval();

        if (interval > 0)
        {
            taskRegistrar.addFixedDelayTask(() -> staticFolderCache.refreshFolders(false), interval * 1000);
        }
    }
}
