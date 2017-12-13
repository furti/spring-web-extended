package io.github.furti.spring.web.extended.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public final class ResourceUtils
{
    private static final Pattern RELATIVE = Pattern.compile("^(\\.\\./)*(.*)$");

    private ResourceUtils()
    {

    }

    /**
     * @param resource the name of the resource
     * @param locale the locale
     * @return the list of resources
     */
    public static List<String> localizedResources(String resource, Locale locale)
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

        String full = localize(resource, locale.getLanguage(), locale.getCountry());

        names.add(full);

        String languageCountry = localize(resource, locale.getLanguage(), locale.getCountry());

        if (!names.contains(languageCountry))
        {
            names.add(languageCountry);
        }

        String language = localize(resource, locale.getLanguage(), null);

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

    private static String localize(String baseName, String language, String country)
    {
        String[] nameAndEnding = getNameAndEnding(baseName);
        StringBuilder localized = new StringBuilder(nameAndEnding[0]);

        if (StringUtils.hasText(language))
        {
            localized.append("_").append(language.toLowerCase());
        }

        if (StringUtils.hasText(country))
        {
            localized.append("-").append(country.toLowerCase());
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
            return new String[]{baseName, null};
        }

        int slashIndex = baseName.lastIndexOf("/");

        // If the last slash is after the last point, the point does not
        // separate the fileending
        if (slashIndex > pointIndex)
        {
            return new String[]{baseName, null};
        }

        return new String[]{baseName.substring(0, pointIndex), baseName.substring(pointIndex)};
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
            return new String[]{"", location};
        }

        return new String[]{location.substring(0, index + 1), location.substring(index + 1)};
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

        return fileName.substring(index + 1);
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

    public static String normalize(String base, String relativeUrl)
    {
        if (StringUtils.isEmpty(base) || StringUtils.isEmpty(relativeUrl))
        {
            return null;
        }

        Matcher m = RELATIVE.matcher(relativeUrl);

        if (!m.matches())
        {
            return relativeUrl;
        }

        int upCount = m.end(1) / 3;
        String path = m.group(2);
        String basePath = base;

        // Strip the file portion if present
        if (basePath.indexOf('.', basePath.lastIndexOf("/")) > -1)
        {
            upCount++;
        }

        for (int i = 0; i < upCount; i++)
        {
            int index = basePath.lastIndexOf("/");

            if (index >= 0)
            {
                basePath = basePath.substring(0, index);
            }
            else
            {
                basePath = "";
            }
        }

        return basePath + "/" + path;
    }
}
