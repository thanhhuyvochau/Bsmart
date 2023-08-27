package fpt.project.bsmart.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static String format(String template, Map<String, String> parameters) {
        StringBuilder newTemplate = new StringBuilder(template);
        List<Object> valueList = new ArrayList<>();

        Matcher matcher = Pattern.compile("[$][{](\\w+)}").matcher(template);

        while (matcher.find()) {
            String key = matcher.group(1);

            String paramName = "${" + key + "}";
            int index = newTemplate.indexOf(paramName);
            if (index != -1) {
                newTemplate.replace(index, index + paramName.length(), "%s");
                valueList.add(parameters.get(key));
            }
        }
        return String.format(newTemplate.toString(), valueList.toArray());
    }

    public static String extractBaseUrl(String urlString) {
        int protocolEndIndex = urlString.indexOf("://");
        if (protocolEndIndex == -1) {
            return null;
        }

        int pathStartIndex = urlString.indexOf("/", protocolEndIndex + 3);
        if (pathStartIndex == -1) {
            return urlString;
        }

        String baseUrl = urlString.substring(0, pathStartIndex);
        if (baseUrl.startsWith("www.")) {
            baseUrl = baseUrl.substring(4);
        }

        return baseUrl;
    }
}
