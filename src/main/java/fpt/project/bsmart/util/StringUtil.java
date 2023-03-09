package fpt.project.bsmart.util;

import org.springframework.security.core.parameters.P;

import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern FACEBOOK_REGEX_PATTERN = Pattern.compile("(https?://)?(www\\.)?facebook\\.com/.+");
    private static final Pattern INSTAGRAM_REGEX_PATTERN = Pattern.compile("(https?://)?(www\\.)?instagram\\.com/.+");
    private static final Pattern TWITTER_REGEX_PATTERN = Pattern.compile("(https?://)?(www\\.)?twitter\\.com/.+");

    //This regex pattern use RFC standard( version RFC-5322)
    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern VIETNAMESE_MOBILE_PHONE_NUMBER_PATTERN = Pattern.compile("(?:03|05|07|08|09|01[2689])[0-9]{8}\\b");
    /**
     * Checks if a string is null or empty.
     *
     * @param s the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param s the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotNullOrEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Trims a string and returns an empty string if the input is null.
     *
     * @param s the string to trim
     * @return the trimmed string, or an empty string if the input is null
     */
    public static String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Truncates a string to a maximum length.
     *
     * @param s the string to truncate
     * @param maxLength the maximum length of the string
     * @return the truncated string, or the original string if it is shorter than maxLength
     */
    public static String truncate(String s, int maxLength) {
        if (s == null || s.length() <= maxLength) {
            return s;
        } else {
            return s.substring(0, maxLength);
        }
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param s the string to capitalize
     * @return the capitalized string
     */
    public static String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }

    public static boolean isValidFacebookLink(String url){return FACEBOOK_REGEX_PATTERN.matcher(url).matches();}

    public static boolean isValidInstagramLink(String url){
        return INSTAGRAM_REGEX_PATTERN.matcher(url).matches();
    }

    public static boolean isValidTwitterLink(String url){
        return TWITTER_REGEX_PATTERN.matcher(url).matches();
    }

    public static boolean isValidEmailAddress(String url){
        return EMAIL_REGEX_PATTERN.matcher(url).matches();
    }
    public static boolean isValidVietnameseMobilePhoneNumber(String phoneNumber){return VIETNAMESE_MOBILE_PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();}
}
