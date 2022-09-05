package com.main.constants;

public class RegexConstant {

    // email: (?i) =  case insensitive // w = a-zA-Z0-9_ // . has special meanings need \.
    // last part is optional - need to keep whole ()?
    public static final String REGEX_STRING_EMAIL =
            "^(?i)([\\w\\.-]+)@([a-z0-9-]+)\\.([a-z]{2,8})(\\.[a-z]{2,6})?$";

    // password:
    // ?= positive look ahead
    public static final String REGEX_STRING_PASSWORD =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[\\w!^]{5,8}$";

    public static final String REGEX_INTEGER = "^[0-9]*$";

    public static final String REGEX_DOUBLE = "^[0-9.,]*$";

    public static final String REGEX_BOOLEAN = "^(0)|(1)|(true)|(false)$";
}
