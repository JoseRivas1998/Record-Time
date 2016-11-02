package com.tcg.recordtime.components.frames;

import com.tcg.recordtime.utils.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JoseR on 11/2/2016.
 */
public class StopWatchFrame extends JFrame {

    private StopWatch stopWatch;

    private JLabel time;

    private Font labelFont, fontAwesome;

    private JButton start, pause, stop;

    public StopWatchFrame() {
        setTitle("Stop Watch - Record Time");
        labelFont = new Font("Arial", Font.BOLD, 72);
        InputStream in = getClass().getResourceAsStream("fontawesome.ttf");
        try {
            fontAwesome = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(12f);
            in.close();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        JLabel title = new JLabel("Stop Watch");
        title.setHorizontalAlignment(JLabel.CENTER);

        time = new JLabel();
        time.setFont(labelFont);
        setTimeText(StopWatch.formatTime(0));

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

        content.add(title, BorderLayout.NORTH);
        content.add(time, BorderLayout.CENTER);

        getContentPane().add(content, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setTimeText(String timeText) {
        time.setText(timeText);
    }

}
