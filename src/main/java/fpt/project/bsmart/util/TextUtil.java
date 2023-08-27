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
        String debug = newTemplate.toString();
        return String.format(newTemplate.toString(), valueList.toArray());
    }
}
