package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.components.Fonts;
import com.tcg.recordtime.components.TimesTableModel;
import com.tcg.recordtime.utils.Profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by JoseR on 11/1/2016.
 */
public class ViewTimesFrame extends JFrame {

    private String profileName;
    private JComboBox<String> sortBox;
    private JButton order;
    private JTable timesTable;
    boolean asc;

    public ViewTimesFrame() {
        if(Profile.currentProfile != null) {
            profileName = Profile.currentProfile.getName();

            setTitle(String.format("View Times for \"%s\" - Record Time", profileName));

            JLabel title = new JLabel(String.format("View Times for \"%s\"", profileName));
            title.setHorizontalAlignment(JLabel.CENTER);

            timesTable = new JTable();
            timesTable.setModel(new TimesTableModel(Profile.currentProfile));

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(timesTable);

            asc = true;

            sortBox = new JComboBox<>();
            sortBox.addItem("Date");
            sortBox.addItem("Time");
            sortBox.setSelectedIndex(0);
            sortBox.addActionListener(e -> sort() );

            order = new JButton("\uf0de");
            order.setFont(Fonts.FONT_AWESOME);
            order.setFocusPainted(false);
            order.addActionListener(e -> {
                asc = !asc;
                sort();
                if(asc) {
                    order.setText("\uf0de");
                } else {
                    order.setText("\uf0dd");
                }
            });

            JLabel sortLabel = new JLabel("Sort Options");
            sortLabel.setHorizontalAlignment(JLabel.CENTER);

            JPanel sortPanel = new JPanel();
            sortPanel.setLayout(new BorderLayout());
            sortPanel.add(sortLabel, BorderLayout.NORTH);
            sortPanel.add(sortBox, BorderLayout.CENTER);
            sortPanel.add(order, BorderLayout.EAST);

            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());
            content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            content.add(title, BorderLayout.NORTH);
            content.add(scrollPane, BorderLayout.CENTER);
            content.add(sortPanel, BorderLayout.SOUTH);

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

    private void sort() {
        switch (sortBox.getSelectedIndex()) {
            case 0:
                Profile.currentProfile.sortByDate(asc);
                break;
            case 1:
                Profile.currentProfile.sortByTime(asc);
                break;
            default:
                break;
        }
        timesTable.setModel(new TimesTableModel(Profile.currentProfile));
    }

}
