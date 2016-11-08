package com.tcg.recordtime;

import com.tcg.recordtime.components.frames.CreateProfileFrame;
import com.tcg.recordtime.components.frames.MainFrame;
import com.tcg.recordtime.managers.FileManager;

import java.io.File;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by JoseR on 10/29/2016.
 */
public class Main {

    public static void main(String[] args) {
        if(!FileManager.applicationFolderExists()) {
            FileManager.createApplicationFolder();
        }
        File faFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + "fontawesome.ttf");
        if(!faFile.exists()) {
            FileManager.downloadToFile("https://tinycountrygames.com/fontawesome.ttf", faFile, REPLACE_EXISTING);
        }
        new MainFrame();
    }

}
