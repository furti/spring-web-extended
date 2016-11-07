package at.porscheinformatik.common.spring.web.extended.template.optimize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.porscheinformatik.common.spring.web.extended.io.ResourceType;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;

public class DefaultOptimizerConfig implements OptimizerConfig
{

    private final Map<ResourceType, Map<String, ResourcePreProcessor>> processors;
    private final Map<ResourceType, List<String>> processorOrder;

    public DefaultOptimizerConfig()
    {
        processorOrder = new HashMap<>();
        processors = new HashMap<>();
    }

    @Override
    public void addOptimizer(ResourceType type, String name, ResourcePreProcessor processor)
    {
        if (!processors.containsKey(type))
        {
            processors.put(type, new HashMap<String, ResourcePreProcessor>());
        }

        if (!processorOrder.containsKey(type))
        {
            processorOrder.put(type, new ArrayList<String>());
        }

        // TODO: hier irgendwann noch sortierung machen
        processorOrder.get(type).add(name);
        processors.get(type).put(name, processor);
    }

    @Override
    public List<ResourcePreProcessor> getProcessors(ResourceType type)
    {
        if (!processors.containsKey(type) || processors.get(type).size() == 0)
        {
            return null;
        }

        ArrayList<ResourcePreProcessor> tmp = new ArrayList<>();
        Map<String, ResourcePreProcessor> typeProcessors = processors.get(type);

        for (String name : processorOrder.get(type))
        {
            tmp.add(typeProcessors.get(name));
        }

        return tmp;
    }
}
