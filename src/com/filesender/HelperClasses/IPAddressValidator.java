package com.filesender.HelperClasses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for validating IP address correctness with regular expression.
 */
public class IPAddressValidator {
    private Pattern pattern;

    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     *  Constructor setting up proper {@link #pattern} value.
     */
    public IPAddressValidator(){
        pattern = Pattern.compile(IP_ADDRESS_PATTERN);
    }

    /**
     * Validate ip address with regular expression
     * @param ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public boolean validate(final String ip){
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}