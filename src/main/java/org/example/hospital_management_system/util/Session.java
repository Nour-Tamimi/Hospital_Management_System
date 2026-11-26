package org.example.hospital_management_system.util;

import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.model.User;

public class Session {
    private static Integer userId;
    private static String username;
    private static String fullName;
    private static Role role;
    private static Integer doctorId;


    private Session(){}
    public static void start(User user){
        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException("Invalid user");
        }

        if (user.getRole() == Role.DOCTOR && user.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor account is not linked");
        }
        userId = user.getUserId();
        username = user.getUsername();
        role = user.getRole();
        doctorId = user.getDoctorId();
        fullName=user.getFullName();
    }

    public static boolean isLoggedIn(){

        return userId!=null;
    }

    public static Role getCurrentRole(){
        if(!isLoggedIn()){throw new IllegalStateException("No User logged in");
        }
        return role;
    }

    public static Integer getDoctorId(){
        if(!isLoggedIn()){throw new IllegalStateException("No User logged in");
        }
        return doctorId;}

    public static int getCurrentUserId(){
        if(!isLoggedIn()){throw new IllegalStateException("No User logged in");
        }
        return userId;
    }

    public static void clear(){
        userId=null;
        role=null;
        doctorId=null;
        fullName=null;
        username=null;
    }


}
