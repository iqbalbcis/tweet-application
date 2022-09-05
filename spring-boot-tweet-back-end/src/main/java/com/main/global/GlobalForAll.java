package com.main.global;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class GlobalForAll {

    public static final String generateSecureToken() {

        try {

            String alphaNumerical = "abcdefghijklmnopqrstuvwxyz";

            SecureRandom random = new SecureRandom(); // threadsafe
            byte bytes[] = new byte[24]; // 24 will produce 32 digits in length
            random.nextBytes(bytes);
            //return Base64.getEncoder().encodeToString(bytes);
            String output = Base64.getEncoder().withoutPadding().encodeToString(bytes); // +, /

            //String output = Base64.getUrlEncoder().encodeToString(bytes); // -, _

            if(output.contains("/")) {
                output = output.replace('/', alphaNumerical.charAt(random.nextInt(25)));
            }

            if(output.contains("+")) {
                output = output.replace('+', alphaNumerical.charAt(random.nextInt(25)));
            }

            System.out.println(output +"  : "+output.length());

            return output;

            // getUrlEncoder() - [a-zA-Z0-9_-]threadsafe but [a-zA-Z0-9+/=]getEncoder() not
        }
        catch(Exception e) {
            log.info("error: {}", e.getMessage(), e);
            return null;
        }
    }

    public static final String generateSmsToken() {
        int result = ThreadLocalRandom.current().nextInt(100000, 999999);
        //System.out.println(i);
        return String.format("%06d", result);
        // 6 means length= 6  0= if short then will be fill by 0
    }
}
