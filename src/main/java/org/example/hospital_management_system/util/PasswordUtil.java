package org.example.hospital_management_system.util;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String password){
        String hashedPass = BCrypt.hashpw(password,BCrypt.gensalt());
        return hashedPass;
    }

    public static boolean verifyPassword(String password, String storedHash){
     boolean checkedPass = BCrypt.checkpw(password, storedHash);
     return checkedPass;
    }

}
