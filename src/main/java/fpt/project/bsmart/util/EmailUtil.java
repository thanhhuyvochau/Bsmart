package fpt.project.bsmart.util;


public class EmailUtil {

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        boolean result = email.matches(regex);
        if (result) {
            return true;
        } else {
            return false;
        }
    }


}
