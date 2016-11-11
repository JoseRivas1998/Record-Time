package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.managers.FileManager;
import com.tcg.recordtime.utils.Profile;
import com.tcg.recordtime.utils.StopWatch;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by JoseR on 11/7/2016.
 */
public class ManageProfileFrame extends JFrame {

    public ManageProfileFrame() {
        this(0);
    }

    public ManageProfileFrame(int index) {
        if(Profile.currentProfile != null) {
            setTitle("Manage Profile - Record Time");

            JLabel title = new JLabel(String.format("Manage \"%s\"", Profile.currentProfile.getName()));
            title.setHorizontalAlignment(JLabel.CENTER);

            JComboBox<String> timeComboBox = new JComboBox<>();
            for(int i = 0; i < Profile.currentProfile.getTimes().size(); i++) {
                Calendar calendar = Profile.currentProfile.getDates().get(i);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                String ampm = "AM";
                if(hour > 12) {
                    hour -= 12;
                    ampm = "PM";
                }
                timeComboBox.addItem(String.format("%d/%d/%d %d:%d %s - %s", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), hour, calendar.get(Calendar.MINUTE), ampm, StopWatch.formatTime(Profile.currentProfile.getTimes().get(i))));
            }

            JTextField uHourField = new JTextField(2);
            JTextField uMinuteField = new JTextField(2);
            JTextField uSecondField = new JTextField(2);
            JTextField uMilliField = new JTextField(3);

            timeComboBox.addActionListener(e -> {
                long time = Profile.currentProfile.getTimes().get(timeComboBox.getSelectedIndex());
                uHourField.setText(String.format("%02d", StopWatch.hours(time)));
                uMinuteField.setText(String.format("%02d", StopWatch.minutes(time)));
                uSecondField.setText(String.format("%02d", (long) StopWatch.seconds(time)));
                uMilliField.setText(String.format("%d", StopWatch.millis(time)));
            });
            timeComboBox.setSelectedIndex(index);

            JButton updateTime = new JButton("Update Time");
            updateTime.addActionListener(e -> {
                try {
                    int hours = Math.abs(Integer.parseInt(uHourField.getText().trim()));
                    int minutes = Math.abs(Integer.parseInt(uMinuteField.getText().trim()) + (hours * 60));
                    int seconds = Math.abs(Integer.parseInt(uSecondField.getText().trim()) + (minutes * 60));
                    long time = Math.abs(Long.parseLong(uMilliField.getText().trim()) + (seconds * 1000));
                    if (JOptionPane.showConfirmDialog(this, String.format("Update time to %s?", StopWatch.formatTime(time)), "Update Time?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Profile.currentProfile.getTimes().set(timeComboBox.getSelectedIndex(), time);
                        String profileName = Profile.currentProfile.getName();
                        JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
                        String fileName = "";
                        for (int i = 0; i < profileArr.length(); i++) {
                            String name = profileArr.getJSONObject(i).getString("name");
                            if (name.equalsIgnoreCase(profileName)) {
                                fileName = profileArr.getJSONObject(i).getString("file");
                                break;
                            }
                        }
                        if (fileName.length() > 0) {
                            File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
                            try {
                                Profile.save(Profile.currentProfile, profileFile);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(this, e1.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Unable to find profile", "Profile error", JOptionPane.ERROR_MESSAGE);
                        }
                        dispose();
                        new ManageProfileFrame(timeComboBox.getSelectedIndex());
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), e1.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                }
            });

            JPanel updatePanel = new JPanel();
            updatePanel.add(uHourField);
            updatePanel.add(new JLabel(":"));
            updatePanel.add(uMinuteField);
            updatePanel.add(new JLabel(":"));
            updatePanel.add(uSecondField);
            updatePanel.add(new JLabel("."));
            updatePanel.add(uMilliField);
            updatePanel.add(updateTime);

            JTextField aHourField = new JTextField(2);
            aHourField.setText("00");
            JTextField aMinuteField = new JTextField(2);
            aMinuteField.setText("00");
            JTextField aSecondField = new JTextField(2);
            aSecondField.setText("00");
            JTextField aMilliField = new JTextField(3);
            aMilliField.setText("000");

            JButton addTime = new JButton("Add Time");
            addTime.addActionListener(e -> {
                try {
                    int hours = Math.abs(Integer.parseInt(aHourField.getText().trim()));
                    int minutes = Math.abs(Integer.parseInt(aMinuteField.getText().trim()) + (hours * 60));
                    int seconds = Math.abs(Integer.parseInt(aSecondField.getText().trim()) + (minutes * 60));
                    long time = Math.abs(Long.parseLong(aMilliField.getText().trim()) + (seconds * 1000));
                    if (JOptionPane.showConfirmDialog(this, String.format("Add %s to \"%s\"?", StopWatch.formatTime(time), Profile.currentProfile.getName()), "Add Time?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Profile.currentProfile.getDates().add(new GregorianCalendar());
                        Profile.currentProfile.getTimes().add(time);
                        String profileName = Profile.currentProfile.getName();
                        JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
                        String fileName = "";
                        for (int i = 0; i < profileArr.length(); i++) {
                            String name = profileArr.getJSONObject(i).getString("name");
                            if (name.equalsIgnoreCase(profileName)) {
                                fileName = profileArr.getJSONObject(i).getString("file");
                                break;
                            }
                        }
                        if (fileName.length() > 0) {
                            File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
                            try {
                                Profile.save(Profile.currentProfile, profileFile);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(this, e1.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Unable to find profile", "Profile error", JOptionPane.ERROR_MESSAGE);
                        }
                        dispose();
                        new ManageProfileFrame(Profile.currentProfile.getTimes().size() - 1);
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), e1.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton viewTimes = new JButton("View Times");
            viewTimes.addActionListener(e -> {
                dispose();
                new ViewTimesFrame();
            });

            JButton deleteTime = new JButton("Delete Time");
            deleteTime.addActionListener(e -> {
                Calendar calendar = Profile.currentProfile.getDates().get(timeComboBox.getSelectedIndex());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                String ampm = "AM";
                if(hour > 12) {
                    hour -= 12;
                    ampm = "PM";
                }
                String timeString = String.format("%d/%d/%d %d:%d %s - %s", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), hour, calendar.get(Calendar.MINUTE), ampm, StopWatch.formatTime(Profile.currentProfile.getTimes().get(timeComboBox.getSelectedIndex())));
                if(JOptionPane.showConfirmDialog(this, String.format("Delete time \"%s\"?", timeString), "Delete time?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int delete = timeComboBox.getSelectedIndex();
                    Profile.currentProfile.getTimes().remove(delete);
                    Profile.currentProfile.getDates().remove(delete);
                    String profileName = Profile.currentProfile.getName();
                    JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
                    String fileName = "";
                    for (int i = 0; i < profileArr.length(); i++) {
                        String name = profileArr.getJSONObject(i).getString("name");
                        if (name.equalsIgnoreCase(profileName)) {
                            fileName = profileArr.getJSONObject(i).getString("file");
                            break;
                        }
                    }
                    if (fileName.length() > 0) {
                        File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
                        try {
                            Profile.save(Profile.currentProfile, profileFile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(this, e1.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Unable to find profile", "Profile error", JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                    new ManageProfileFrame(Math.min(delete, Profile.currentProfile.getDates().size()- 1));
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(viewTimes);
            buttonPanel.add(deleteTime);

            JPanel addPanel = new JPanel();
            addPanel.add(aHourField);
            addPanel.add(new JLabel(":"));
            addPanel.add(aMinuteField);
            addPanel.add(new JLabel(":"));
            addPanel.add(aSecondField);
            addPanel.add(new JLabel("."));
            addPanel.add(aMilliField);
            addPanel.add(addTime);

            JPanel updateAddPanel = new JPanel();
            updateAddPanel.setLayout(new BorderLayout());
            updateAddPanel.add(updatePanel, BorderLayout.NORTH);
            updateAddPanel.add(addPanel, BorderLayout.SOUTH);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BorderLayout());
            controlPanel.add(timeComboBox, BorderLayout.NORTH);
            controlPanel.add(updateAddPanel, BorderLayout.CENTER);
            controlPanel.add(buttonPanel, BorderLayout.SOUTH);

            JPanel content = new JPanel();
            content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            content.setLayout(new BorderLayout());
            content.add(title, BorderLayout.NORTH);
            content.add(controlPanel, BorderLayout.CENTER);

            getContentPane().add(content, BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    new MainFrame();
                }
            });

            pack();
            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Profile Selected. Please select a profile", "No Profile", JOptionPane.WARNING_MESSAGE);
            new MainFrame();
            dispose();
        }



    }

}
