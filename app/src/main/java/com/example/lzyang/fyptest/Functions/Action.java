package com.example.lzyang.fyptest.Functions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by User on 5/10/2017.
 */

public class Action {
    //Turn Hex to ASCll For example : 31 turn to 1
    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    //Turn ASCll to Hex For example : 1 turn to 31
    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }
        return hex.toString();
    }

    public static Bitmap byteToBitmap(byte[] img_byte){
        Bitmap bitmap = BitmapFactory.decodeByteArray(img_byte, 0, img_byte.length);
        return bitmap;
    }
}
