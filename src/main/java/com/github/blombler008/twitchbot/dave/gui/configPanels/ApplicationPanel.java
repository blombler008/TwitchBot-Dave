package com.github.blombler008.twitchbot.dave.gui.configPanels;

import com.github.blombler008.twitchbot.dave.gui.ConfigPanel;
import com.github.blombler008.twitchbot.dave.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ApplicationPanel extends ConfigPanel {


    private JCheckBox showTreeLines;
    private JCheckBox systemTrayEnable;
    private JPanel root;
    private JCheckBox resizeable;
    private JTextField titleField;
    private JLabel titleLabel;
    private JLabel iconLabel;
    private JTextField iconField;
    private JButton aboutButton;
    private JCheckBox isLightTheme;
    private JButton chooserButton;
    private JLabel iconPreview;
    private GUI gui;

    public ApplicationPanel(GUI gui) {
        this.gui = gui;
        $$$setupUI$$$();
        setIcon();
        titleField.addKeyListener(applyOnEnter());
        iconField.addKeyListener(applyOnEnter());
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        this.add(root);
    }

    private KeyListener applyOnEnter() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    apply();
                }
            }
        };
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        root.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        root.setAutoscrolls(true);
        root.setDoubleBuffered(true);
        root.setEnabled(true);
        root.setFocusCycleRoot(false);
        root.setFocusTraversalPolicyProvider(false);
        root.putClientProperty("html.disable", Boolean.FALSE);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(5, 5));
        panel1.setMaximumSize(new Dimension(2147483647, 30));
        root.add(panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        titleLabel = new JLabel();
        titleLabel.setEnabled(true);
        titleLabel.setText("Window Title: ");
        panel1.add(titleLabel, BorderLayout.WEST);
        titleField.setMinimumSize(new Dimension(80, 22));
        titleField.setPreferredSize(new Dimension(80, 22));
        panel1.add(titleField, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(5, 5));
        panel2.setMaximumSize(new Dimension(2147483647, 30));
        root.add(panel2);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        iconLabel = new JLabel();
        iconLabel.setText("Window Icon: ");
        panel2.add(iconLabel, BorderLayout.WEST);
        iconField = new JTextField();
        iconField.setMinimumSize(new Dimension(80, 22));
        iconField.setPreferredSize(new Dimension(80, 22));
        panel2.add(iconField, BorderLayout.CENTER);
        chooserButton = new JButton();
        chooserButton.setHorizontalAlignment(0);
        chooserButton.setHorizontalTextPosition(0);
        chooserButton.setPreferredSize(new Dimension(22, 22));
        chooserButton.setText("...");
        panel2.add(chooserButton, BorderLayout.EAST);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setMaximumSize(new Dimension(32767, 120));
        root.add(panel3);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        showTreeLines.setSelected(true);
        showTreeLines.setText("Show Tree Lines");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(showTreeLines, gbc);
        aboutButton = new JButton();
        aboutButton.setText("About");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(aboutButton, gbc);
        systemTrayEnable.setEnabled(true);
        systemTrayEnable.setSelected(false);
        systemTrayEnable.setText("Enable System Tray");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(systemTrayEnable, gbc);
        resizeable.setSelected(true);
        resizeable.setText("Resizeable");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(resizeable, gbc);
        isLightTheme.setEnabled(true);
        isLightTheme.setSelected(false);
        isLightTheme.setText("Light Theme");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(isLightTheme, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }


    private void setIcon() {
        File faviconEntry = new File(gui.getLoad().getWorkingDirectory(), "favicon-16.png");
        setIcon(faviconEntry);
    }

    private void setIcon(String path) {
        setIcon(new File(path));
    }

    private void setIcon(File favicon) {
        try {
            iconLabel.setIcon(getScaledImage(ImageIO.read(favicon), 16, 16));

            iconField.setText(favicon.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }

    private void createUIComponents() {
        root = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        root.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        showTreeLines = new JCheckBox();
        systemTrayEnable = new JCheckBox();
        resizeable = new JCheckBox();
        isLightTheme = new JCheckBox();
        titleField = new JTextField(gui.getDefaultTitle());


        iconPreview = new JLabel();
        iconPreview.setIcon(UIManager.getIcon("FileView.directoryIcon"));

    }

    @Override
    public void apply() {
        gui.setResizable(resizeable.isSelected());
        gui.setShowLines(showTreeLines.isSelected());
        gui.setSystemTray(systemTrayEnable.isSelected());
        gui.setLightTheme(isLightTheme.isSelected());
        gui.setTitle(titleField.getText());

        File favicon = new File(iconField.getText());
        if (favicon.exists()) {
            try {
                gui.setIconImage(ImageIO.read(favicon));
                setIcon(favicon);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gui.getApplicationConfig().setLightTheme(isLightTheme.isSelected());
        gui.getApplicationConfig().setTitle(titleField.getText());
        gui.getApplicationConfig().setResizeable(resizeable.isSelected());
        gui.getApplicationConfig().setShowTreeLines(showTreeLines.isSelected());
        gui.getApplicationConfig().setSystemTrayEnable(systemTrayEnable.isSelected());
    }

}
