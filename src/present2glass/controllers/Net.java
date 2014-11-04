package present2glass.controllers;

import java.awt.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Net {

    /**
     * Validate the IP address
     * @param ip String representing the ip address
     * @return True if matches a valid ip address else false
     */
    public static boolean validateIP(final String ip){
        if(ip.equals("0.0.0.0")) return false;
        final String reg = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }


}
