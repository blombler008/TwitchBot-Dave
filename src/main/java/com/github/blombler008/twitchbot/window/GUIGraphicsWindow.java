package com.github.blombler008.twitchbot.window;/*
 *
 * MIT License
 *
 * Copyright (c) 2019 blombler008
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.github.blombler008.twitchbot.TwitchBot;
import com.github.blombler008.twitchbot.window.panels.ConfigPanel;
import com.github.blombler008.twitchbot.window.panels.ConsolePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUIGraphicsWindow extends JFrame {

    private static final long serialVersionUID = -8779506564512780698L;

    private final ButtonGroup viewButtonGroup = new ButtonGroup();
    private final ButtonGroup menuBarGroup = new ButtonGroup();
    private final AtomicBoolean isConsolePanelSelected = new AtomicBoolean(true);
    private final AtomicBoolean isConfigPanelSelected = new AtomicBoolean(false);
    private final AtomicBoolean isStatusPanelSelected = new AtomicBoolean(false);
    private final int defaultExitOpteration = EXIT_ON_CLOSE;
    private final Dimension defaultSize = new Dimension(1024, 576); // 16:9 a 1024:576 Resolution
    private final Point defaultLocation = new Point(50, 50);
    private final String defaultTitle = "Dave's - TwitchBot Manager";
    private JMenuBar menuBar;
    private JMenuItem applicationExitMenuItem;
    private JMenu applicationMenu;
    private JMenu viewMenu;
    private JRadioButtonMenuItem viewRadioConfig;
    private JRadioButtonMenuItem viewRadioConsole;
    private JPanel contentPane;
    private JPanel consolePanel;
    private JPanel configPanel;
    private ConsolePanel pConsolePanel;
    private ConfigPanel pConfigPanel;
    private String viewText = "View";
    private String applicationText = "Application";
    private String exitText = "Exit (CTRL + Q)";
    private String configText = "Config (CTRL + W)";
    private String consoleText = "Console (CTRL + S)";
    private boolean finishedInitializing = false;

    public void initialize() {

        // Initialize components //
        contentPane = new JPanel(); // Root pane

        menuBar = new JMenuBar(); // Menu bar

        applicationMenu = new JMenu(applicationText); // Application menu
        viewMenu = new JMenu(viewText); // View menu

        viewRadioConsole = new JRadioButtonMenuItem(consoleText); // console radio button -> view menu
        viewRadioConfig = new JRadioButtonMenuItem(configText); // config radio button -> view menu

        applicationExitMenuItem = new JMenuItem(exitText); // exit button -> application menu


        // Console panel //
        pConsolePanel = new ConsolePanel(this);
        consolePanel = pConsolePanel.get();

        // Config panel //
        pConfigPanel = new ConfigPanel(this);
        configPanel = pConfigPanel.get();

        // Listener //

        applicationExitMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });

        viewRadioConfig.addActionListener(e -> {
            if (viewRadioConfig.isSelected()) {
                isConsolePanelSelected.set(false);
                isStatusPanelSelected.set(false);
                isConfigPanelSelected.set(true);
                updatePanels();
            }
        });
/*
        viewRadioStatus.addActionListener(e -> {
            if(viewRadioConsole.isSelected()) {
                isConsolePanelSelected.set(false);
                isStatusPanelSelected.set(true);
                isConfigPanelSelected.set(false);
                updatePanels();
            }
        });*/

        viewRadioConsole.addActionListener(e -> {
            if (viewRadioConsole.isSelected()) {
                isConsolePanelSelected.set(true);
                isStatusPanelSelected.set(false);
                isConfigPanelSelected.set(false);
                updatePanels();
            }
        });

        // Changes //
        // Radio view buttons //
        viewRadioConsole.setSelected(true);

        // Root pane //
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        // Menu Bar //
        menuBar.add(applicationMenu);
        menuBar.add(viewMenu);

        applicationMenu.add(applicationExitMenuItem);

        viewMenu.add(viewRadioConfig);
        viewMenu.add(viewRadioConsole);

        // Groups //
        // View Radio Buttons //
        viewButtonGroup.add(viewRadioConfig);
        viewButtonGroup.add(viewRadioConsole);
        // Menu Bar //
        menuBarGroup.add(applicationMenu);
        menuBarGroup.add(viewMenu);


        // Adding views to root pane //
        updatePanels();

        // setting frame options //
        setJMenuBar(menuBar); // Menu bar
        //setContentPane(contentPane); // Root pane


        // Key Listener //
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            TwitchBot.getPrintStream().logWindow(e.paramString());
            if (e.getID() == KeyEvent.KEY_RELEASED) {

                if (e.isControlDown()) {

                    if (!e.isShiftDown()) {
                        if (!e.isAltDown()) {
                            if (e.getKeyCode() == KeyEvent.VK_Q) {
                                System.exit(0);
                            }
                            if (e.getKeyCode() == KeyEvent.VK_W) {
                                viewRadioConfig.setSelected(true);
                                viewRadioConfig.getActionListeners()[0].actionPerformed(new ActionEvent(e, 0, "changedToConfigView"));
                            }
                            if (e.getKeyCode() == KeyEvent.VK_S) {
                                viewRadioConsole.setSelected(true);
                                viewRadioConsole.getActionListeners()[0].actionPerformed(new ActionEvent(e, 0, "changedToCoonsoleView"));
                            }
                        }
                    }
                }
            }
            if (e.getID() == KeyEvent.KEY_TYPED) {

            }
            if (e.getID() == KeyEvent.KEY_PRESSED) {

            }
            return false;
        });
        finishedInitializing = true;
    }

    public void center() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        this.setLocation(x, y);
    }

    public ConsolePanel getConsolePanel() {
        return pConsolePanel;
    }

    public ConfigPanel getConfigPanel() {
        return pConfigPanel;
    }

    public void updatePanels() {
        contentPane.removeAll();
        contentPane.revalidate();
        if (isConsolePanelSelected.get()) {
            contentPane.add(consolePanel);
        }

        if (isConfigPanelSelected.get()) {
            contentPane.add(configPanel);
        }

        if (isStatusPanelSelected.get()) {
            //setContentPane(statusPanel);
            //contentPane.add(statusPanel);
        }
        setContentPane(contentPane);
        // statusPanel.setVisible(isStatusPanelSelected.get());
        // statusPanel.setFocusable(isStatusPanelSelected.get());
    }

    public boolean isInitialized() {
        while (!finishedInitializing) {

        }
        return finishedInitializing;
    }

    public int getDefaultExitOperation() {
        return defaultExitOpteration;
    }

    public Dimension getDefaultSize() {
        return defaultSize;
    }

    public Point getDefaultLocation() {
        return defaultLocation;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }
}
