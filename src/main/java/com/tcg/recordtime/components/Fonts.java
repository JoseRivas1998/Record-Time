package com.tcg.recordtime.components;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JoseR on 11/5/2016.
 */
public class Fonts {

    public static Font ARIAL_72;
    public static Font FONT_AWESOME;

    static {
        ARIAL_72 = new Font("Arial", Font.BOLD, 72);
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("fontawesome.ttf"));
            FONT_AWESOME = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(12f);
            in.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}
