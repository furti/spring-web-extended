package at.porscheinformatik.common.spring.web.extended.expression;

import java.util.List;

import org.springframework.util.Assert;

import at.porscheinformatik.common.spring.web.extended.config.ApplicationConfiguration;
import at.porscheinformatik.common.spring.web.extended.template.cache.StackConfig;
import at.porscheinformatik.common.spring.web.extended.util.HtmlUtils;

public class StyleExpressionHandler extends UrlGeneratingExpressionHandler
{
    private final StackConfig styleConfig;
    private final ApplicationConfiguration config;

    public StyleExpressionHandler(StackConfig styleConfig, ApplicationConfiguration config)
    {
        super();
        this.styleConfig = styleConfig;
        this.config = config;
    }

    @Override
    public String process(String value)
    {
        Assert.notNull(styleConfig, "No stylestack defined");
        Assert.isTrue(styleConfig.hasStack(value), "StyleStack " + value + " not found");

        if (config.isOptimizeResources())
        {
            return HtmlUtils.buildStyleLink(generateUrl("style/stack", value));
        }
        else
        {
            return buildDevelopmentStyles(value);
        }
    }

    private String buildDevelopmentStyles(String stackName)
    {
        List<String> styleNames = styleConfig.getResourceNamesForStack(stackName);

        Assert.notEmpty(styleNames, "No styles defined in stack " + stackName);

        StringBuilder builder = new StringBuilder();

        for (String styleName : styleNames)
        {
            builder.append(HtmlUtils.buildStyleLink(generateUrl("style/single", stackName, styleName))).append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean valueNeeded()
    {
        return true;
    }
}
