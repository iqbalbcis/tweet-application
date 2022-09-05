package com.main.constants;

import java.time.ZoneId;

public class CommonConstant {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_LOCALE = "en-GB";
    public static final String TIME_ZONE = "Europe/London";
    public static final String BIRTHDAY_TIME_ZONE = "UTC";
    public static final String UTC_TIME_ZONE = "UTC";

    public static final String AUTHENTICATIONPATH = "/tweets/authenticate";

    public static final String ACTUATOR = "/actuator/**";
    public static final String H2CONSOLE = "/h2-console/**/**";

    public static final String TOKENHEADER = "Authorization";
    public static final String SECRETKEY = "z!t!24#@?xcmb>&*<^>15ws";

    public static final long TOKEN_TIMEOUT_IN_MINUTE = 1000L * 60 * 15; // 15 minutes
    public static final long TOKEN_TIMEOUT_IN_MINUTES = 15; // 15 minutes

    public static final String EMAIL_SENDER_HOST = "";
    public static final String EMAIL_SEND_FORM = "";
    public static final String EMAIL_SEND_PASS = "HubersPost";

    public static final String ADMIN_EMAIL_FOR_ACTIVATON_CODE = "iqbalbcis@gmail.com";
    public static final String PHONE_MESSAGE_IS_ACTIVE = "No"; // Yes

}
