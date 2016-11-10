package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import com.tcg.recordtime.utils.Profile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JoseR on 10/30/2016.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        if(Profile.loadProfiles().size() > 0) {
            setTitle("Record Time");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JLabel title = new JLabel("Record Time");
            title.setHorizontalAlignment(JLabel.CENTER);

            JButton createProfile = new JButton("Create Profile");
            createProfile.addActionListener(e -> {
                new CreateProfileFrame();
                dispose();
            });

            JButton stopWatch = new JButton("Stop Watch");
            stopWatch.addActionListener(e -> {
                new StopWatchFrame();
                dispose();
            });

            JButton viewTimes = new JButton("View Times");
            viewTimes.addActionListener(e -> {
                if(Profile.currentProfile != null) {
                    new ViewTimesFrame();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No Profile Selected. Please select a profile", "No Profile", JOptionPane.WARNING_MESSAGE);
                }
            });

            JPanel bRow1 = new JPanel();
            bRow1.add(createProfile);
            bRow1.add(stopWatch);
            bRow1.add(viewTimes);

            JButton manageProfiles = new JButton("Profiles");
            manageProfiles.addActionListener(e -> {
                new ProfilesFrame();
                dispose();
            });

            JButton manageProfile = new JButton("Manage Profile");
            manageProfile.addActionListener(e -> {
                new ManageProfileFrame();
                dispose();
            });

            JButton about = new JButton("About");
            about.addActionListener(e -> {
                new AboutFrame();
                dispose();
            });

            JPanel bRow2 = new JPanel();
            bRow2.add(manageProfiles);
            bRow2.add(manageProfile);
            bRow2.add(about);

            JPanel buttons = new JPanel();
            buttons.setLayout(new BorderLayout());
            buttons.add(bRow1, BorderLayout.NORTH);
            buttons.add(bRow2, BorderLayout.SOUTH);

            JComboBox<String> profileBox = new JComboBox<>();
            List<Profile> profiles = new ArrayList<>(Profile.loadProfiles());
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

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());

            bottomPanel.add(profileBox, BorderLayout.SOUTH);

            JPanel contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPanel.setLayout(new BorderLayout());

            contentPanel.add(title, BorderLayout.NORTH);
            contentPanel.add(buttons, BorderLayout.CENTER);
            contentPanel.add(bottomPanel, BorderLayout.SOUTH);

            getContentPane().add(contentPanel);
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



}
