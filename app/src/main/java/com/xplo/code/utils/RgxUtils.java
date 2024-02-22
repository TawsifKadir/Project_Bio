package com.xplo.code.utils;

/**
 * Copyright 2018 (C) Xplo
 * <p>
 * Created  : 9/8/2018
 * Author   : xplo
 * Version  : 1.0
 * Desc     : Regx jlime.utils
 * Comment  : Need improvement
 * <p>
 * https://www.regextester.com/
 */


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RgxUtils {


    //leap year include
    final public static String RGX_DATE_DMY = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)" +
            "(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?" +
            "(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)" +
            "(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    //final public static String RGX_TIME_24H= "(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]";
    final public static String RGX_TIME_24H = "^([01]\\d|2[0-3]):([0-5]\\d)$";


    //there some bug exist in reg num sec
    final public static String RGX_POS_NUM = "^[+]?[0-9].?[0-9]*";
    final public static String RGX_NEG_NUM = "^[-][0-9].?[0-9]*";
    final public static String RGX_NUM = "^[-+]?[0-9].?[0-9]*";

    //there some bug exist in reg int sec
    final public static String RGX_INT = "^([-+]?[1-9]+\\d*)$|^0$";
    final public static String RGX_POS_INT = "^([+]?[0-9]\\d*|0)$";
    final public static String RGX_NEG_INT = "^([-][1-9]\\d*|0)$";


    //final public static String RGX_URL= "((http|https)://)?[a-zA-Z]\\w*(\\.\\w+)+(/\\w*(\\.\\w+)*)*(\\?.+)*";
    final public static String RGX_URL_NO_FTP = "(^(http[s]?://)?([w]{3}[.])?([a-z0-9]+[.])+com(((/[a-z0-9]+)*(/[a-z0-9]+/))*" +
            "([a-z0-9]+[.](html|php|gif|png))?)$)|(^([.]/)?((([a-z0-9]+)/?)+|(([a-z0-9]+)/)+([a-z0-9]+[.](html|php|gif|png)))?$)";
    final public static String RGX_URL_SIMPLE = "((http|https)://)?[a-zA-Z]\\w*(\\.\\w+)+(/\\w*(\\.\\w+)*)*(\\?.+)*";


    final public static String RGX_IPV4_ADDRESS = "/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/";

    final public static String RGX_HEX_COLOR_VALUE = "";

    final public static String RGX_ = "";


    final public static String RGX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    final public static String RGX_PHONE_NO_INTERNATIONAL = "^[+]?[0-9]{10,13}$";
    //final public static String RGX_PHONE_NO_INTERNATIONAL = "^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?";


    //final public static String RGX_PHONE_NO_BD = "^[+8]?[0-9]{10,13}$";
    //final public static String RGX_PHONE_NO_BD = "^(?:\\+?88)?01[13-9]\\d{8}$";
    final public static String RGX_PHONE_NO_BD = "^(?:\\+?88)?01[1345-9]\\d{8}$";
    final public static String RGX_PHONE_NO_MV = "^(?:\\+?96)?[1345-9]\\d{7}$";
    //final public static String RGX_PHONE_NO_BD = "/(^(\\+8801|8801|01|008801))[1|5-9]{1}(\\d){8}$/";


    final public static String RGX_PHONE_NO_KSA = "^((?:[+?0?0?966]+)(?:\\s?\\d{2})(?:\\s?\\d{7}))$";


    final public static String RGX_USERNAME = "^[a-zA-Z][a-zA-Z0-9._-]{2,}$";
    final public static String RGX_FULL_NAME_OR_FIRST_NAME = "^[\\p{L} .'-]+$";
    final public static String RGX_LAST_NAME = "^[\\p{L}]+$";

    final public static String RGX_YEAR = "^\\d{4}$";            //1900 2099
    //final public static String RGX_YEAR_1900_2099 = "(?:(?:19|20)[0-9]{2})";    //1900 2099
    final public static String RGX_YEAR_1900_2099 = "^(19|20)\\d{2}$";    //1900 2099
    final public static String RGX_YEAR_1000_2999 = "^[12][0-9]{3}$";    //1000 2999


    /**
     * Check wheather a string match with regex
     *
     * @param s
     * @param regx
     * @return
     */
    public static boolean isMatch(String s, String regx) {
        //return Pattern.compile(regx).matcher(s).matches();

        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(s);

        return m.matches();

    }


    /**
     * Check wheather a string match with regex
     * Case not sensative
     *
     * @param s
     * @param regx
     * @return
     */
    public static boolean isMatchNotCaseSensative(String s, String regx) {
        //return Pattern.compile(regx).matcher(s).matches();

        Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);

        return m.matches();

    }


    /**
     * Check wheather a string find with regex
     *
     * @param s
     * @param regx
     * @return
     */
    public static boolean isFind(String s, String regx) {
        //return Pattern.compile(regx).matcher(s).matches();

        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(s);

        return m.matches();

    }


}
