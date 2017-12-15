package io.github.furti.spring.web.extended.template.legacy.optimize;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.furti.spring.web.extended.io.ResourceType;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;

public class OptimizerChain
{
    private Map<ResourceType, List<ResourcePreProcessor>> processors;

    public OptimizerChain(OptimizerConfig config)
    {
        setupOptimizers(config);
    }

    public String optimize(ResourceType type, String uri, String input) throws IOException
    {
        if (processors == null || !processors.containsKey(type))
        {
            return input;
        }

        List<ResourcePreProcessor> typeProcessors = processors.get(type);

        String processed = input;

        for (ResourcePreProcessor processor : typeProcessors)
        {
            Resource resource = Resource.create(uri, findWroType(type));
            StringReader reader = new StringReader(processed);
            StringWriter writer = new StringWriter();

            processor.process(resource, reader, writer);

            processed = writer.toString();
        }

        return processed;
    }

    private void setupOptimizers(OptimizerConfig config)
    {
        processors = new HashMap<>();

        for (ResourceType type : ResourceType.values())
        {
            List<ResourcePreProcessor> typeProcessors = config.getProcessors(type);

            if (typeProcessors != null)
            {
                processors.put(type, typeProcessors);
            }
        }
    }

    private ro.isdc.wro.model.resource.ResourceType findWroType(ResourceType type)
    {
        switch (type)
        {
            case STYLE:
                return ro.isdc.wro.model.resource.ResourceType.CSS;
            case SCRIPT:
                return ro.isdc.wro.model.resource.ResourceType.JS;
            default:
                return null;
        }
    }
}
