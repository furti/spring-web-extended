/**
 * 
 */
package io.github.furti.spring.web.extended.staticfolder;

import io.github.furti.spring.web.extended.ApplicationInfo;

/**
 * @author Daniel Furtlehner
 */
public class DefaultApplicationInfo implements ApplicationInfo
{
    private boolean productionMode;

    @Override
    public ApplicationInfo productionMode(boolean productionMode)
    {
        this.productionMode = productionMode;

        return this;
    }

    @Override
    public boolean isProductionMode()
    {
        return productionMode;
    }

}
