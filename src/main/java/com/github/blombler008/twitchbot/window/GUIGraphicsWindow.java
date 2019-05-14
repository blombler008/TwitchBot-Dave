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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUIGraphicsWindow extends JFrame {

    private static final long serialVersionUID = -8779506564512780698L;

    private final ButtonGroup viewButtonGroup = new ButtonGroup();
    private final ButtonGroup menuBarGroup = new ButtonGroup();

    private JTextField twitchInput;
    private JTextField webInput;
    private JTextField timerInput;

    private JMenuBar menuBar;

    private JMenuItem applicationExitMenuItem;

    private JMenu applicationMenu;
    private JMenu viewMenu;

    private JRadioButtonMenuItem viewRadioConfig;
    private JRadioButtonMenuItem viewRadioConsole;

    private JPanel contentPane;
    private JPanel consolePanel;

    private JPanel timerPanel;
    private JPanel twitchPanel;
    private JPanel webPanel;
    private JPanel windowPanel;

    private JPanel webInputPanel;
    private JPanel timerInputPanel;
    private JPanel twitchInputPanel;

    private JTextPane twitchLog;
    private JTextPane webLog;
    private JTextPane timerLog;
    private JTextPane windowLog;

    private JTabbedPane consoleViewTab;

    private JButton webInputButton;
    private JButton twitchInputButton;
    private JButton timerInputButton;

    private String viewText = "View";
    private String applicationText = "Application";

    private String exitText = "Exit (CRTL + Q)";
    private String configText = "Config (CRTL + W)";
    private String consoleText = "Console (CRTL + S)";

    private String sendText = "Send";

    private String viewConsoleTabWindowText = "Application";
    private String viewConsoleTabTwitchText = "Twitch";
    private String viewConsoleTabTimerText = "Timer";
    private String viewConsoleTabWebText = "Web";

    private boolean finishedInitializing = false;

    private final AtomicBoolean isConsolePanelSelected = new AtomicBoolean(true);
    private final AtomicBoolean isConfigPanelSelected = new AtomicBoolean(false);
    private final AtomicBoolean isStatusPanelSelected = new AtomicBoolean(false);


    public void initialize() {

        // Initialize components //
        menuBar = new JMenuBar(); // Menu bar

        applicationMenu = new JMenu(applicationText); // Application menu
        viewMenu = new JMenu(viewText); // View menu

        viewRadioConsole = new JRadioButtonMenuItem(consoleText); // console radio button -> view menu
        viewRadioConfig = new JRadioButtonMenuItem(configText); // config radio button -> view menu

        applicationExitMenuItem = new JMenuItem(exitText); // exit button -> application menu

        contentPane = new JPanel(); // Root pane
        consolePanel = new JPanel(); // console view panel
        consoleViewTab = new JTabbedPane(JTabbedPane.TOP); // console view tab -> console view panel

        // Listener //
        applicationExitMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(0);
            }
        });

        viewRadioConfig.addActionListener(e -> {
            if(viewRadioConfig.isSelected()) {
                isConsolePanelSelected.set(false);
                isStatusPanelSelected.set(false);
                isConfigPanelSelected.set(true);

                consolePanel.setVisible(isConsolePanelSelected.get());
                consolePanel.setFocusable(isConsolePanelSelected.get());

//                configPanel.setVisible(isConfigPanelSelected.get());
//                configPanel.setFocusable(isConfigPanelSelected.get());

//                statusPanel.setVisible(isStatusPanelSelected.get());
//                statusPanel.setFocusable(isStatusPanelSelected.get());

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
            if(viewRadioConsole.isSelected()) {
                isConsolePanelSelected.set(true);
                isStatusPanelSelected.set(false);
                isConfigPanelSelected.set(false);

                consolePanel.setVisible(isConsolePanelSelected.get());
                consolePanel.setFocusable(isConsolePanelSelected.get());

//                configPanel.setVisible(isConfigPanelSelected.get());
//                configPanel.setFocusable(isConfigPanelSelected.get());

//                statusPanel.setVisible(isStatusPanelSelected.get());
//                statusPanel.setFocusable(isStatusPanelSelected.get());

            }
        });

        // Changes //
        // Radio view buttons //
        viewRadioConsole.setSelected(true);

        // Root pane //
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        // console panel //
        consolePanel.setLayout(new BorderLayout(0, 0));
        consolePanel.setVisible(isConsolePanelSelected.get());
        consolePanel.setFocusable(isConsolePanelSelected.get());
        consolePanel.setOpaque(isConsolePanelSelected.get());

        // config panel //

        // Timer Tab //
        timerPanel = new JPanel();
        timerPanel.setLayout(new BorderLayout(5, 5));

        timerLog = new JTextPane();
        timerLog.setEditable(false);

        timerInputPanel = new JPanel();
        timerInputPanel.setLayout(new BorderLayout(5, 5));

        timerInput = new JTextField();
        timerInput.setColumns(10);

        timerInputButton = new JButton(sendText);
        timerInputButton.setPreferredSize(new Dimension(70, 23));

        // Twitch Tab //
        twitchPanel = new JPanel();
        twitchPanel.setLayout(new BorderLayout(5, 5));

        twitchLog = new JTextPane();
        twitchLog.setEditable(false);

        twitchInputPanel = new JPanel();
        twitchInputPanel.setLayout(new BorderLayout(5, 5));

        twitchInput = new JTextField();
        twitchInput.setColumns(10);

        twitchInputButton = new JButton(sendText);
        twitchInputButton.setPreferredSize(new Dimension(70, 23));

        // Web Tab //
        webPanel = new JPanel();
        webPanel.setLayout(new BorderLayout(5, 5));

        webLog = new JTextPane();
        webLog.setEditable(false);

        webInputPanel = new JPanel();
        webInputPanel.setLayout(new BorderLayout(5, 5));

        webInput = new JTextField();
        webInput.setColumns(10);

        webInputButton = new JButton(sendText);
        webInputButton.setPreferredSize(new Dimension(70, 23));

        // Application Tab //
        windowPanel = new JPanel();
        windowPanel.setLayout(new BorderLayout(0, 0));

        windowLog = new JTextPane();
        windowLog.setEditable(false);

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

        // Putting panels together //
        // Web //
        webInputPanel.add(webInput);
        webInputPanel.add(webInputButton, BorderLayout.EAST);

        webPanel.add(webLog);
        webPanel.add(webInputPanel, BorderLayout.SOUTH);

        // Timer //
        timerInputPanel.add(timerInput);
        timerInputPanel.add(timerInputButton, BorderLayout.EAST);

        timerPanel.add(timerLog);
        timerPanel.add(timerInputPanel, BorderLayout.SOUTH);

        // Twitch //
        twitchInputPanel.add(twitchInput);
        twitchInputPanel.add(twitchInputButton, BorderLayout.EAST);

        twitchPanel.add(twitchLog);
        twitchPanel.add(twitchInputPanel, BorderLayout.SOUTH);

        // Window //
        windowPanel.add(windowLog);

        // Adding all log panels to the tab View //
        consoleViewTab.addTab(viewConsoleTabWindowText, null, windowPanel, null);
        consoleViewTab.addTab(viewConsoleTabWebText, null, webPanel, null);
        consoleViewTab.addTab(viewConsoleTabTwitchText, null, twitchPanel, null);
        consoleViewTab.addTab(viewConsoleTabTimerText, null, timerPanel, null);

        // Adding tab View to the console view panel //
        consolePanel.add(consoleViewTab);

        // Adding views to root pane //
        contentPane.add(consolePanel);

        // setting frame options //
        setJMenuBar(menuBar); // Menu bar
        setContentPane(contentPane); // Root pane
        setSize(new Dimension(1024, 576)); // 16:9 a 1024:576 Resolution
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exits the program when hit the Red X


        // Key Listener //
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            System.out.println(e.paramString());
            if(e.getID() == KeyEvent.KEY_RELEASED) {

                if(e.isControlDown()) {

                    if(!e.isShiftDown()) {
                        if(!e.isAltDown()) {
                            if(e.getKeyCode() == KeyEvent.VK_Q) {
                                System.exit(0);
                            }
                            if(e.getKeyCode() == KeyEvent.VK_W) {
                                viewRadioConfig.setSelected(true);
                                viewRadioConfig.getActionListeners()[0].actionPerformed(new ActionEvent(e, 0, "changedToConfigView"));
                            }
                            if(e.getKeyCode() == KeyEvent.VK_S) {
                                viewRadioConsole.setSelected(true);
                                viewRadioConsole.getActionListeners()[0].actionPerformed(new ActionEvent(e, 0, "changedToCoonsoleView"));
                            }
                        }
                    }
                }
            }
            if(e.getID() == KeyEvent.KEY_TYPED) {

            }
            if(e.getID() == KeyEvent.KEY_PRESSED) {

            }
            return false;
        });
        finishedInitializing = true;
    }

    public void updatePanels() {
        consolePanel.setVisible(isConsolePanelSelected.get());
        consolePanel.setFocusable(isConsolePanelSelected.get());

        // configPanel.setVisible(isConfigPanelSelected.get());
        // configPanel.setFocusable(isConfigPanelSelected.get());

        // statusPanel.setVisible(isStatusPanelSelected.get());
        // statusPanel.setFocusable(isStatusPanelSelected.get());
    }


    public ButtonGroup getViewButtonGroup() {
        return viewButtonGroup;
    }

    public ButtonGroup getMenuBarGroup() {
        return menuBarGroup;
    }

    public JTextField getTwitchInput() {
        return twitchInput;
    }

    public JTextField getWebInput() {
        return webInput;
    }

    public JTextField getTimerInput() {
        return timerInput;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    public JMenuItem getApplicationExitMenuItem() {
        return applicationExitMenuItem;
    }

    public JMenu getApplicationMenu() {
        return applicationMenu;
    }

    public JMenu getViewMenu() {
        return viewMenu;
    }

    public JRadioButtonMenuItem getViewRadioConfig() {
        return viewRadioConfig;
    }

    public JRadioButtonMenuItem getViewRadioConsole() {
        return viewRadioConsole;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JPanel getConsolePanel() {
        return consolePanel;
    }

    public JPanel getTimerPanel() {
        return timerPanel;
    }

    public JPanel getTwitchPanel() {
        return twitchPanel;
    }

    public JPanel getWebPanel() {
        return webPanel;
    }

    public JPanel getWindowPanel() {
        return windowPanel;
    }

    public JPanel getWebInputPanel() {
        return webInputPanel;
    }

    public JPanel getTimerInputPanel() {
        return timerInputPanel;
    }

    public JPanel getTwitchInputPanel() {
        return twitchInputPanel;
    }

    public JTextPane getTwitchLog() {
        return twitchLog;
    }

    public JTextPane getWebLog() {
        return webLog;
    }

    public JTextPane getTimerLog() {
        return timerLog;
    }

    public JTextPane getWindowLog() {
        return windowLog;
    }

    public JTabbedPane getConsoleViewTab() {
        return consoleViewTab;
    }

    public JButton getWebInputButton() {
        return webInputButton;
    }

    public JButton getTwitchInputButton() {
        return twitchInputButton;
    }

    public JButton getTimerInputButton() {
        return timerInputButton;
    }

    public String getView() {
        return viewText;
    }

    public String getApplicationText() {
        return applicationText;
    }

    public String getExitText() {
        return exitText;
    }

    public String getConfigText() {
        return configText;
    }

    public String getConsoleText() {
        return consoleText;
    }

    public String getSendText() {
        return sendText;
    }

    public String getViewConsoleTabWindowText() {
        return viewConsoleTabWindowText;
    }

    public String getViewConsoleTabTwitchText() {
        return viewConsoleTabTwitchText;
    }

    public String getViewConsoleTabTimerText() {
        return viewConsoleTabTimerText;
    }

    public String getViewConsoleTabWebText() {
        return viewConsoleTabWebText;
    }


    public boolean isInitialized() {
        while(!finishedInitializing) {

        }
        return finishedInitializing;
    }

    public boolean isConsolePanelSelected() {
        return isConsolePanelSelected.get();
    }

    public boolean isConfigPanelSelected() {
        return isConfigPanelSelected.get();
    }

    public boolean isStatusPanelSelected() {
        return isStatusPanelSelected.get();
    }
}
