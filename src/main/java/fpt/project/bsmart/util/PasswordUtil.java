package fpt.project.bsmart.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {

    public static Boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

//    public static String BCryptPasswordEncoder(String password) {
//        org.springframework.security.crypto.password.PasswordEncoder encoder
//                = new BCryptPasswordEncoder();
//        String passwd = encoder.encode(password);
//        return passwd;
//    }

    public static boolean IsOldPassword(String oldRawPassword, String encodedOldPassword){
        org.springframework.security.crypto.password.PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(oldRawPassword, encodedOldPassword);
    }

}
