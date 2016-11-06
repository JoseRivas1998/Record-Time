package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.components.Fonts;
import com.tcg.recordtime.managers.FileManager;
import com.tcg.recordtime.utils.Profile;
import com.tcg.recordtime.utils.StopWatch;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.GregorianCalendar;

/**
 * Created by JoseR on 11/2/2016.
 */
public class StopWatchFrame extends JFrame {

    private StopWatch stopWatch;

    private JLabel time;

    private JButton start, stop;

    public StopWatchFrame() {
        setTitle("Stop Watch - Record Time");


        JLabel title = new JLabel("Stop Watch");
        title.setHorizontalAlignment(JLabel.CENTER);

        time = new JLabel();
        time.setFont(Fonts.ARIAL_72);
        setTimeText(StopWatch.formatTime(0));

        start = new JButton("\uf04b");
        start.setFont(Fonts.FONT_AWESOME);
        start.setFocusPainted(false);
        start.setForeground(Color.GREEN);
        start.addActionListener(e -> {
            if(!stopWatch.running) {
                stopWatch.startStopWatch();
                start.setForeground(Color.GRAY);
                start.setText("\uf04c");
            } else {
                stopWatch.pauseStopWatch();
                start.setForeground(Color.GREEN);
                start.setText("\uf04b");
            }
        });
        start.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(!stopWatch.running) {
                    start.setForeground(Color.GREEN);
                } else {
                    start.setForeground(Color.GRAY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(!stopWatch.running) {
                    start.setForeground(Color.GREEN.darker());
                } else {
                    start.setForeground(Color.GRAY.darker());
                }
            }
        });

        stop = new JButton("\uf04d");
        stop.setFont(Fonts.FONT_AWESOME);
        stop.setFocusPainted(false);
        stop.setForeground(Color.RED.darker());
        stop.addActionListener(e -> {
            confirmAndSave(false);
            start.setForeground(Color.GREEN);
            start.setText("\uf04b");
        });
        stop.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                stop.setForeground(Color.RED);
            }

            @Override
            public void focusLost(FocusEvent e) {
                stop.setForeground(Color.RED.darker());
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(start);
        buttons.add(stop);

        JPanel timeButtons = new JPanel();
        timeButtons.setLayout(new BorderLayout());
        timeButtons.add(time, BorderLayout.CENTER);
        timeButtons.add(buttons, BorderLayout.SOUTH);

        JLabel profileLabel = new JLabel(String.format("Current Profile: %s", Profile.currentProfile.getName()));
        profileLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

        content.add(title, BorderLayout.NORTH);
        content.add(timeButtons, BorderLayout.CENTER);
        content.add(profileLabel, BorderLayout.SOUTH);

        getContentPane().add(content, BorderLayout.NORTH);

        stopWatch = new StopWatch(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                confirmAndSave(true);
                new MainFrame();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setTimeText(String timeText) {
        time.setText(timeText);
    }

    private void confirmAndSave(boolean closing) {
        long stopTime = 0;
        if(closing) {
            stopTime = stopWatch.kill();
        } else {
            stopWatch.stopStopWatch();
            stopTime = stopWatch.getTime();
        }
        Profile.currentProfile.getTimes().add(stopTime);
        Profile.currentProfile.getDates().add(new GregorianCalendar());
        String message = String.format("Current time is %s, would you like to save?", StopWatch.formatTime(stopTime));
        if(JOptionPane.showConfirmDialog(this, message, "Save Time?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String profileName = Profile.currentProfile.getName();
            JSONArray profileArr = FileManager.loadProfilesJSON(false).getJSONArray("profiles");
            String fileName = "";
            for(int i = 0; i < profileArr.length(); i++) {
                String name = profileArr.getJSONObject(i).getString("name");
                if(name.equalsIgnoreCase(profileName)) {
                    fileName = profileArr.getJSONObject(i).getString("file");
                    break;
                }
            }
            if(fileName.length() > 0) {
                File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
                try {
                    Profile.save(Profile.currentProfile, profileFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Unable to find profile", "Profile error", JOptionPane.ERROR_MESSAGE);
            }
        }
        stopWatch.resetTime();
    }

}
