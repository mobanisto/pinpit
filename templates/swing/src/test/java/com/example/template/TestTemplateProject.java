package com.example.template;

import de.topobyte.shared.preferences.SharedPreferences;
import de.topobyte.swing.util.SwingUtils;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TestTemplateProject {

    public static void main(String[] oArgs) {
        double factor =  1;
        if (SharedPreferences.isUIScalePresent()) {
            SwingUtils.setUiScale(SharedPreferences.getUIScale());
            factor = SharedPreferences.getUIScale();
        }

        JFrame frame = new JFrame();
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("icon.png")) {
            BufferedImage image = ImageIO.read(input);
            frame.setIconImage(image);
        } catch (IOException e) {
            // ignore, continue without icon
        }

        String version = Version.getVersion();
        frame.add(new MainPanel(version));

        frame.setTitle("Template Project");
        frame.setMinimumSize(new Dimension((int) (800 * factor), (int) (600 * factor)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
