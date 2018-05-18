package com.samosys.paperai.activity.utils;

import android.util.Log;

/**
 * Created by samosys on 18/7/16.
 */
public class First_Char_Capital {
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }


    public static String capitalizeChracters(String name) {
        String newString = "";
        try {


            name = name.trim().replaceAll(" +", " ");
            String[] nameArray = name.trim().split(" ");



            Log.e("New String-----", "" + nameArray.length);

            for (int i = 0; i < nameArray.length; i++) {

                if (newString.length() == 0) {
                    newString = nameArray[i].substring(0, 1).toUpperCase() + nameArray[i].substring(1).toLowerCase();
                } else {
                    newString += " " + nameArray[i].substring(0, 1).toUpperCase() + nameArray[i].substring(1).toLowerCase();

                }

            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return newString;
    }

}
