package at.porscheinformatik.common.springangular.io;

import java.util.Locale;

import org.springframework.core.io.Resource;

public interface LocalizedResourceLoader
{

	Resource getResource(String resource, Locale locale);
}
