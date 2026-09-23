/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.systempaq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author eduardx_2
 */
public class CheckOn {
    
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    public static boolean emailCheck(String email){
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        Matcher match = pat.matcher(email);
        return match.matches();
    }
    
}
