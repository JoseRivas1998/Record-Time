package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import com.tcg.recordtime.utils.Profile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by JoseR on 11/5/2016.
 */
public class ProfilesFrame extends JFrame {

    public ProfilesFrame() {
        if(Profile.loadProfiles().size() > 0) {
            setTitle("Profiles - Record Time");

            JLabel title = new JLabel("Profiles");
            title.setHorizontalAlignment(JLabel.CENTER);

            JTextField renameField = new JTextField(10);
            renameField.addActionListener(e -> renameProfile(renameField));

            JComboBox<String> profileBox = new JComboBox<>();
            java.util.List<Profile> profiles = new ArrayList<>(Profile.loadProfiles());
            profiles.sort(new Comparator<Profile>() {
                @Override
                public int compare(Profile p1, Profile p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            for (Profile profile : profiles) {
                profileBox.addItem(profile.getName());
            }
            profileBox.addActionListener(e -> {
                Profile.currentProfile = new Profile(profiles.get(profileBox.getSelectedIndex()));
                renameField.setText(Profile.currentProfile.getName());
            });
            if(Profile.currentProfile == null) {
                profileBox.setSelectedIndex(0);
            } else {
                for(int i = 0; i < profiles.size(); i++) {
                    if(profiles.get(i).getName().equals(Profile.currentProfile.getName())) {
                        profileBox.setSelectedIndex(i);
                        break;
                    }
                }
            }

            JButton viewTimes = new JButton("View Times");
            viewTimes.addActionListener(e -> {
                if(Profile.currentProfile != null) {
                    new ViewTimesFrame();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No Profile Selected. Please select a profile", "No Profile", JOptionPane.WARNING_MESSAGE);
                }
            });

            JButton deleteProfile = new JButton("Delete Profile");
            deleteProfile.addActionListener(e -> {
                if(JOptionPane.showConfirmDialog(this, String.format("Would you like to delete profile \"%s\"", Profile.currentProfile.getName()), "Delete Profile?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
                    int index = -1;
                    for(int i = 0; i < profileArr.length(); i++) {
                        String name = profileArr.getJSONObject(i).getString("name");
                        if(name.equalsIgnoreCase(Profile.currentProfile.getName())) {
                            index = i;
                            break;
                        }
                    }
                    if(index >= 0) {
                        String fileName = profileArr.getJSONObject(index).getString("file");
                        File file = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
                        file.delete();
                        profileArr.remove(index);
                        JSONObject profilesJSON = new JSONObject();
                        profilesJSON.put("profiles", profileArr);
                        FileManager.saveProfilesJSON(profilesJSON);
                        dispose();
                        Profile.currentProfile = null;
                        new ProfilesFrame();
                    } else {
                        JOptionPane.showMessageDialog(this, "That profile could not be found.", "Profile not found", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton renameButton = new JButton("Rename");
            renameButton.addActionListener(e -> renameProfile(renameField));

            JPanel renamePanel = new JPanel();
            renamePanel.add(renameField);
            renamePanel.add(renameButton);

            JPanel buttons = new JPanel();
            buttons.add(viewTimes);
            buttons.add(deleteProfile);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BorderLayout());
            controlPanel.add(renamePanel, BorderLayout.NORTH);
            controlPanel.add(buttons, BorderLayout.CENTER);

            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());
            content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            content.add(title, BorderLayout.NORTH);
            content.add(profileBox, BorderLayout.CENTER);
            content.add(controlPanel, BorderLayout.SOUTH);

            getContentPane().add(content, BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    new MainFrame();
                }
            });

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "There are no profiles, please create one.", "No profiles", JOptionPane.INFORMATION_MESSAGE);
            new CreateProfileFrame();
            dispose();
        }
    }

    private void renameProfile(JTextField renameField) {
        String newName = renameField.getText().trim();
        if(newName.length() > 0) {
            String newFileName = newName.toLowerCase().replaceAll("[^a-zA-Z0-9.-]", "_") + ".dat";
            boolean exists = false;
            JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
            for(int i = 0; i < profileArr.length(); i++) {
                if(profileArr.getJSONObject(i).getString("name").equalsIgnoreCase(newName) ||
                        profileArr.getJSONObject(i).getString("file").equalsIgnoreCase(newFileName)) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                if (JOptionPane.showConfirmDialog(this, String.format("Rename \"%s\" to \"%s\"?", Profile.currentProfile.getName(), newName), "Rename?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int profileIndex = -1;
                    for (int i = 0; i < profileArr.length(); i++) {
                        if(profileArr.getJSONObject(i).getString("name").equalsIgnoreCase(Profile.currentProfile.getName())) {
                            profileIndex = i;
                            break;
                        }
                    }
                    if(profileIndex >= 0) {
                        String oldName = profileArr.getJSONObject(profileIndex).getString("name");
                        String oldFileName = profileArr.getJSONObject(profileIndex).getString("file");
                        profileArr.getJSONObject(profileIndex).put("name", newName);
                        profileArr.getJSONObject(profileIndex).put("file", newFileName);
                        File oldFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + oldFileName);
                        File newFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + newFileName);
                        boolean copied = false;
                        try {
                            Files.copy(oldFile.toPath(), newFile.toPath(), REPLACE_EXISTING);
                            copied = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }
                        oldFile.delete();
                        Profile.currentProfile.setName(newName);
                        try {
                            Profile.save(Profile.currentProfile, newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JSONObject profiles = new JSONObject();
                        profiles.put("profiles", profileArr);
                        FileManager.saveProfilesJSON(profiles);
                        dispose();
                        new ProfilesFrame();
                    } else {
                        JOptionPane.showMessageDialog(this, "That profile could not be found.", "Profile not found", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "A profile with that name already exists.", "File Exists", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Enter a name", "No name", JOptionPane.ERROR_MESSAGE);
        }
    }

}
