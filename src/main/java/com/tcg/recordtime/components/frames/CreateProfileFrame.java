package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JoseR on 10/29/2016.
 */
public class CreateProfileFrame extends JFrame {

    public CreateProfileFrame() throws HeadlessException {
        JSONObject profiles = new JSONObject(FileManager.loadProfilesJSON(true).toString());
        
        setSize(100, 100);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
