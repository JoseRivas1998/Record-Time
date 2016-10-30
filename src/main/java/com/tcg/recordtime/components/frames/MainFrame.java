package com.tcg.recordtime.components.frames;

import javax.swing.*;

/**
 * Created by JoseR on 10/30/2016.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Record Time");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}
