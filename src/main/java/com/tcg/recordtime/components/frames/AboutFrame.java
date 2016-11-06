package com.tcg.recordtime.components.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoseR on 11/5/2016.
 */
public class AboutFrame extends JFrame {

    private List<JLabel> labelList;

    public static final String VERSION = "1.0.0";

    public AboutFrame() {
        setTitle("About - Record Time");

        labelList = new ArrayList<>();

        addLabel("Record Time");
        addLabel("Author:");
        addLabel("Jos\u00E9 Rodriguez-Rivas");
        addLabel(AboutFrame.VERSION);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(labelList.size(), 1, 10, 10));

        for (JLabel jLabel : labelList) {
            textPanel.add(jLabel);
        }

        JButton tinycountrygames = new JButton("Tiny Country Games");
        tinycountrygames.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL("http://tinycountrygames.com").toURI());
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        JButton github = new JButton("GitHub");
        github.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/JoseRivas1998/Record-Time").toURI());
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(tinycountrygames);
        buttons.add(github);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        content.add(textPanel, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        getContentPane().add(content, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                new MainFrame();
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addLabel(String text) {
        labelList.add(new JLabel(text, SwingConstants.CENTER));
    }

}
