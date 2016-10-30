package com.tcg.recordtime;

import com.tcg.recordtime.components.frames.CreateProfileFrame;
import com.tcg.recordtime.components.frames.MainFrame;
import com.tcg.recordtime.managers.FileManager;

/**
 * Created by JoseR on 10/29/2016.
 */
public class Main {

    public static void main(String[] args) {
        if(!FileManager.applicationFolderExists()) {
            FileManager.createApplicationFolder();
        }
        new MainFrame();
    }

}
