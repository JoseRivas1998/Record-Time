package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by JoseR on 10/29/2016.
 */
public class CreateProfileFrame extends JFrame {

    public CreateProfileFrame() {
        this(null);
    }

    public CreateProfileFrame(Component relative) {
        JSONObject profiles = new JSONObject(FileManager.loadProfilesJSON(true).toString());
        setTitle("Create Profile - Record Time");


        JLabel title = new JLabel("Create Profile");
        title.setHorizontalAlignment(JLabel.CENTER);

        JTextField profileIn = new JTextField(10);

        JButton create = new JButton("Create");
        create.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "test", "test", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(profileIn);
        inputPanel.add(create);

        JPanel titlePanel = new JPanel();

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(title, BorderLayout.NORTH);
        contentPanel.add(inputPanel, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel);

        pack();
        setLocationRelativeTo(relative);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
