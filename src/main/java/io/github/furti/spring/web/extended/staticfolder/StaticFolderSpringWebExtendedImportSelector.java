/**
 *
 */
package io.github.furti.spring.web.extended.staticfolder;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Daniel Furtlehner
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class StaticFolderSpringWebExtendedImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] { StaticFolderConfiguration.class.getName() };
    }
}
