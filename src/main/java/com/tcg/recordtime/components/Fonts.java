package com.tcg.recordtime.components;

import com.tcg.recordtime.managers.FileManager;

import java.awt.*;
import java.io.*;

/**
 * Created by JoseR on 11/5/2016.
 */
public class Fonts {

    public static Font ARIAL_72;
    public static Font FONT_AWESOME;

    static {
        ARIAL_72 = new Font("Arial", Font.BOLD, 72);
        try {
            File faFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + "fontawesome.ttf");
            InputStream in = new BufferedInputStream(new FileInputStream(faFile));
            FONT_AWESOME = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(12f);
            in.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}
