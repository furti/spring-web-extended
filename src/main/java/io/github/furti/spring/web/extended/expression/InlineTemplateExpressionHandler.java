package io.github.furti.spring.web.extended.expression;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import io.github.furti.spring.web.extended.io.ResourceUtils;
import io.github.furti.spring.web.extended.template.cache.html.HtmlStacks;

public class InlineTemplateExpressionHandler extends BaseExpressionHandler implements BeanFactoryAware
{
    private final Pattern PATTERN = Pattern.compile("(\r\n)|([\r\n\f\t]+)");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HtmlStacks stacks;
    private BeanFactory beanFactory;

    public InlineTemplateExpressionHandler()
    {
        super(true);
    }

    @Override
    public String doProcess(String value)
    {
        logger.info("Rendering inlinetemplate " + value);

        HtmlStacks htmlStacks = getStacks();

        String templateName = value.toLowerCase() + ".html";

        if (isTemplate("", templateName, htmlStacks))
        {
            return prepareResult(htmlStacks.get("").renderTemplate(templateName));
        }

        String[] pathAndFile = ResourceUtils.pathAndFile(templateName);

        if (isTemplate(pathAndFile[0], pathAndFile[1], htmlStacks))
        {
            return prepareResult(htmlStacks.get(pathAndFile[0]).renderTemplate(pathAndFile[1]));
        }

        throw new IllegalArgumentException("Template " + value + " not found");
    }

    private boolean isTemplate(String stackName, String templateName, HtmlStacks htmlStacks)
    {
        return htmlStacks.hasStack(stackName) && htmlStacks.get(stackName).hasTemplate(templateName);
    }

    private String prepareResult(String template)
    {
        if (template == null)
        {
            return null;
        }

        return PATTERN.matcher(template).replaceAll(" ").trim();
    }

    private HtmlStacks getStacks()
    {
        // We need this because of the circular dependency between handler and
        // stack
        if (stacks == null)
        {
            stacks = beanFactory.getBean(HtmlStacks.class);
        }

        return stacks;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }
}
