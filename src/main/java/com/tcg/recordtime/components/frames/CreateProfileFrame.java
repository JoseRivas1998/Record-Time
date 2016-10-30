package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import com.tcg.recordtime.utils.Profile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by JoseR on 10/29/2016.
 */
public class CreateProfileFrame extends JFrame {

    private JSONObject profiles;

    public CreateProfileFrame() {
        this(null);
    }

    public CreateProfileFrame(Component relative) {
        profiles = new JSONObject(FileManager.loadProfilesJSON(true).toString());
        setTitle("Create Profile - Record Time");


        JLabel title = new JLabel("Create Profile");
        title.setHorizontalAlignment(JLabel.CENTER);

        JTextField profileIn = new JTextField(10);
        profileIn.addActionListener(e -> createProfile(profileIn));

        JButton create = new JButton("Create");
        create.addActionListener(e -> createProfile(profileIn));

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

    private void createProfile(JTextField profileIn) {
        if(profileIn.getText().trim().length() > 0) {
            String profileName = profileIn.getText();
            String fileName = profileName.trim().toLowerCase().replaceAll("[^a-zA-Z0-9.-]", "_") + ".dat";
            File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
            boolean fileExists = profileFile.exists();
            JSONArray profilesArr = profiles.getJSONArray("profiles");
            for(int i = 0; i < profilesArr.length(); i++) {
                JSONObject jsonObject = profilesArr.getJSONObject(i);
                String file = jsonObject.getString("file");
                if(fileName.equalsIgnoreCase(file)) {
                    fileExists = true;
                    break;
                }
            }
            if(!fileExists) {
                JSONObject newProfile = new JSONObject();
                newProfile.put("name", profileName);
                newProfile.put("file", fileName);
                profilesArr.put(newProfile);
                Profile profile = new Profile(profileName);
                try {
                    Profile.save(profile, profileFile);
                    FileManager.saveProfilesJSON(profiles);
                    JOptionPane.showMessageDialog(null, String.format("Profile \"%s\" created.", profileName), "Profile Created", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "A profile with that name already exists.", "File Exists", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a profile image.", "Enter a Profile", JOptionPane.ERROR_MESSAGE);
        }
    }

}
