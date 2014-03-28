package at.porscheinformatik.common.spring.web.extended.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.util.StringUtils;

public final class ResourceUtils
{

	private ResourceUtils()
	{

	}

	/**
	 * @param resource
	 * @return
	 */
	public static List<String> localizedResources(String resource,
			Locale locale)
	{
		if (resource == null)
		{
			return null;
		}

		if (locale == null)
		{
			return Arrays.asList(resource);
		}

		List<String> names = new ArrayList<>();

		String full = localize(resource, locale.getLanguage(),
				locale.getCountry(), locale.getVariant());

		names.add(full);

		String languageCountry = localize(resource, locale.getLanguage(),
				locale.getCountry(), null);

		if (!names.contains(languageCountry))
		{
			names.add(languageCountry);
		}

		String language = localize(resource, locale.getLanguage(), null,
				null);

		if (!names.contains(language))
		{
			names.add(language);
		}
		// At last add the templateName if not added yet
		if (!names.contains(resource))
		{
			names.add(resource);
		}

		return names;
	}

	private static String localize(String baseName, String language,
			String country,
			String variant)
	{
		String[] nameAndEnding = getNameAndEnding(baseName);
		StringBuilder localized = new StringBuilder(nameAndEnding[0]);

		if (StringUtils.hasText(language))
		{
			localized.append("_").append(language.toLowerCase());
		}

		if (StringUtils.hasText(country))
		{
			localized.append("_").append(country.toLowerCase());
		}

		if (StringUtils.hasText(variant))
		{
			localized.append("_").append(variant.toLowerCase());
		}

		if (nameAndEnding[1] != null)
		{
			localized.append(nameAndEnding[1]);
		}

		return localized.toString();
	}

	public static String[] getNameAndEnding(String baseName)
	{
		if (baseName == null)
		{
			return null;
		}

		int pointIndex = baseName.lastIndexOf(".");

		if (pointIndex == -1)
		{
			return new String[] { baseName, null };
		}

		int slashIndex = baseName.lastIndexOf("/");

		// If the last slash is after the last point, the point does not
		// separate the fileending
		if (slashIndex > pointIndex)
		{
			return new String[] { baseName, null };
		}

		return new String[] {
				baseName.substring(0, pointIndex),
				baseName.substring(pointIndex) };
	}

	public static String[] pathAndFile(String location)
	{
		if (location == null)
		{
			return null;
		}

		int index = location.lastIndexOf("/");

		if (index == -1)
		{
			return new String[] { "", location };
		}

		return new String[] {
				location.substring(0, index + 1),
				location.substring(index + 1)
		};
	}

	public static String getLocaleFromName(String nameWithoutEnding)
	{
		String[] pathAndFile = pathAndFile(nameWithoutEnding);

		if (pathAndFile == null)
		{
			return null;
		}

		String fileName = pathAndFile[1];

		int index = fileName.indexOf("_");

		if (index == -1)
		{
			return null;
		}

		return fileName.substring(0, index);
	}

	public static String unlocalize(String resource)
	{
		String[] nameAndEnding = getNameAndEnding(resource);

		if (nameAndEnding == null)
		{
			return null;
		}

		int index = nameAndEnding[0].indexOf("_");

		if (index == -1)
		{
			return resource;
		}

		StringBuilder builder = new StringBuilder();

		builder.append(nameAndEnding[0].substring(0, index));

		if (nameAndEnding[1] != null)
		{
			builder.append(".").append(nameAndEnding[1]);
		}

		return builder.toString();
	}
}
