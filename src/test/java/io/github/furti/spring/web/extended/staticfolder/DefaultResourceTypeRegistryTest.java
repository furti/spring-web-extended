/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.springframework.util.MimeType;
import org.testng.annotations.Test;

import io.github.furti.spring.web.extended.template.StringResource;

/**
 * @author Daniel Furtlehner
 */
public class DefaultResourceTypeRegistryTest
{

    @Test
    public void testEmptyRegistry()
    {
        ResourceTypeRegistry registry = new DefaultResourceTypeRegistry();

        ResourceType resourceType =
            registry.getResourceType(new StringResource("test.ico", ""), MimeType.valueOf("image/x-icon"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.BINARY));
    }

    @Test
    public void testFallbackToDefault()
    {
        ResourceTypeRegistry registry = buildRegistry();

        ResourceType resourceType =
            registry.getResourceType(new StringResource("test.ico", ""), MimeType.valueOf("image/x-icon"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.BINARY));
    }

    @Test
    public void testHtmlMimeType()
    {
        ResourceTypeRegistry registry = buildRegistry();

        ResourceType resourceType =
            registry.getResourceType(new StringResource("test.html", ""), MimeType.valueOf("text/html"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.TEMPLATE));
    }

    @Test
    public void testJavascriptMimeType()
    {
        ResourceTypeRegistry registry = buildRegistry();

        ResourceType resourceType =
            registry.getResourceType(new StringResource("test.js", ""), MimeType.valueOf("application/javascript"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.TEMPLATE));
    }

    @Test
    public void testJsonFile()
    {
        ResourceTypeRegistry registry = buildRegistry();

        ResourceType resourceType =
            registry.getResourceType(new StringResource("test.json", ""), MimeType.valueOf("application/json"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.TEMPLATE));
    }

    @Test
    public void testBinaryFile()
    {
        ResourceTypeRegistry registry = buildRegistry();

        ResourceType resourceType = registry.getResourceType(new StringResource("/some/path/binary.js", ""),
            MimeType.valueOf("application/javascript"));

        assertThat(resourceType, CoreMatchers.equalTo(ResourceType.BINARY));
    }

    private ResourceTypeRegistry buildRegistry()
    {
        DefaultResourceTypeRegistry registry = new DefaultResourceTypeRegistry();

        registry.resourceTypeByMimeType("text/.*", ResourceType.TEMPLATE);
        registry.resourceTypeByMimeType("application/javascript", ResourceType.TEMPLATE);

        registry.resourceTypeByName(".*test\\.json", ResourceType.TEMPLATE);
        registry.resourceTypeByName(".*binary\\.js", ResourceType.BINARY);

        return registry;
    }
}
