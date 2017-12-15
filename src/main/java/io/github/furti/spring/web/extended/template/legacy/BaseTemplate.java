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
package io.github.furti.spring.web.extended.template.legacy;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import io.github.furti.spring.web.extended.io.ResourceType;

public abstract class BaseTemplate implements Template
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ResourceType type;
    protected String templateName;
    protected String location;
    protected boolean alreadyOptimized;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = lock.readLock();
    private final WriteLock writeLock = lock.writeLock();

    public BaseTemplate(ResourceType type, String templateName, boolean alreadyOptimized, String location)
    {
        this.type = type;
        this.templateName = templateName;
        this.alreadyOptimized = alreadyOptimized;
        this.location = location;
    }

    @Override
    public String render() throws IOException
    {
        logger.trace("Thread " + Thread.currentThread().getName() + " obtains readLock for template " + getName());

        readLock.lock();
        try
        {
            String template = getContent();

            if (!StringUtils.hasText(template))
            {
                return template;
            }

            return template;
        }
        finally
        {
            logger.trace("Thread " + Thread.currentThread().getName() + " releases readLock for template " + getName());
            readLock.unlock();
        }
    }

    @Override
    public boolean isChanged(Date since) throws IOException
    {
        long lastmodified = getLastModified();

        return lastmodified > since.getTime();
    }

    @Override
    public void refresh() throws IOException
    {
        logger.debug("Thread " + Thread.currentThread().getName() + " obtains writeLock for template " + getName());

        writeLock.lock();
        try
        {
            doRefresh();
        }
        finally
        {
            logger
                .debug("Thread " + Thread.currentThread().getName() + " releases writeLock for template " + getName());

            writeLock.unlock();
        }
    }

    protected abstract String getContent() throws IOException;

    protected abstract void doRefresh() throws IOException;

    protected abstract long getLastModified() throws IOException;

    @Override
    public ResourceType getType()
    {
        return type;
    }

    @Override
    public boolean isAlreadyOptimized()
    {
        return alreadyOptimized;
    }

    @Override
    public String getName()
    {
        return templateName;
    }

    @Override
    public String getLocation()
    {
        return location;
    }
}
