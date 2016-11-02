package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.components.TimesTableModel;
import com.tcg.recordtime.utils.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.GregorianCalendar;

/**
 * Created by JoseR on 11/1/2016.
 */
public class ViewTimesFrame extends JFrame {

   private String profileName;

    public ViewTimesFrame() {
        if(Profile.currentProfile != null) {
            profileName = Profile.currentProfile.getName();

            setTitle(String.format("View Times for \"%s\" | Record Time", profileName));

            JLabel title = new JLabel(String.format("View Times for \"%s\"", profileName));
            title.setHorizontalAlignment(JLabel.CENTER);

            JTable timesTable = new JTable();
            timesTable.setModel(new TimesTableModel(Profile.currentProfile));

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(timesTable);

            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());
            content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            content.add(title, BorderLayout.NORTH);
            content.add(scrollPane, BorderLayout.CENTER);

            getContentPane().add(content, BorderLayout.CENTER);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    new MainFrame();
                }
            });

        } else {
            JOptionPane.showMessageDialog(this, "No Profile Selected. Please select a profile", "No Profile", JOptionPane.WARNING_MESSAGE);
            new MainFrame();
            dispose();
        }
    }


}
