package presenttoglass.controllers;

import java.awt.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Net {

    public static String getIPAddress(boolean useIPv4) {
        try {
            return useIPv4 ? Inet4Address.getLocalHost().getHostAddress() : Inet6Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

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

    public static void open(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void open(URL url) {
        try {
            open(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void open(String url){
        try {
            open(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
