package org.example.hospital_management_system.util;

public class TestPasswordUtil {
    public static void main(String[] args){
        String password ="TestAdmin123!";
        String hash = PasswordUtil.hashPassword(password);



        System.out.println("Hash"+ hash);

        boolean correct = PasswordUtil.verifyPassword("Test1234!",hash);

        if(correct) System.out.println("correct");
        else System.out.println("false");
    }
}
