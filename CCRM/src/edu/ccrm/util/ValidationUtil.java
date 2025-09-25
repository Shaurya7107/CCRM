package edu.ccrm.util;

import java.util.function.Predicate;

public class ValidationUtil {

    public static final Predicate<String> EMAIL_PREDICATE =
            email -> email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");

    public static final Predicate<String> ID_PREDICATE =
            id -> id != null && id.matches("^[A-Za-z0-9]{1,10}$");

    public static final Predicate<String> NAME_PREDICATE =
            name -> name != null && name.matches("^[A-Za-z\\s]{2,50}$");

    public static boolean validateEmail(String email) {
        return EMAIL_PREDICATE.test(email);
    }

    public static boolean validateStudentId(String id) {
        return ID_PREDICATE.test(id);
    }

    public static boolean validateName(String name) {
        return NAME_PREDICATE.test(name);
    }
}