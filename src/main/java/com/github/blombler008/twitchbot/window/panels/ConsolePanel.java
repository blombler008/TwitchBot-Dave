package com.github.blombler008.twitchbot.window.panels;/*
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

import com.github.blombler008.twitchbot.window.GUIGraphicsWindow;
import com.github.blombler008.twitchbot.window.panels.console.*;

import javax.swing.*;
import java.awt.*;

public class ConsolePanel {

    private JPanel panel;

    private JPanel allPanel;
    private JPanel timerPanel;
    private JPanel twitchPanel;
    private JPanel webPanel;
    private JPanel windowPanel;

    private TabAll tabAll;
    private TabTimer tabTimer;
    private TabTwitch tabTwitch;
    private TabWeb tabWeb;
    private TabWindow tabWindow;

    private JTabbedPane tab;

    private String sendText = "Send";

    private String tabAllText = "All";
    private String tabWindowText = "Application";
    private String tabTwitchText = "Twitch";
    private String tabTimerText = "Timer";
    private String tabWebText = "Web";

    public ConsolePanel(GUIGraphicsWindow window) {

        panel = new JPanel(); // console view panel
        tab = new JTabbedPane(JTabbedPane.TOP); // console view tab -> console view panel

        // console panel //
        panel.setLayout(new BorderLayout(5, 5));

        // Tabs //
        tabAll = new TabAll();
        tabTimer = new TabTimer(window);
        tabTwitch = new TabTwitch(window);
        tabWeb = new TabWeb(window);
        tabWindow = new TabWindow(window);

        // Tab configs //
        tabTimer.setSendButtonText(sendText);
        tabTwitch.setSendButtonText(sendText);
        tabWeb.setSendButtonText(sendText);
        tabAll.setSendButtonText(sendText);

        // Tab panels //
        timerPanel = tabTimer.get();
        twitchPanel = tabTwitch.get();
        windowPanel = tabWindow.get();
        webPanel = tabWeb.get();
        allPanel = tabAll.get();

        // Adding all log panels to the tab View //
        tab.addTab(tabAllText, null, allPanel, null); // Selected Tab
        tab.addTab(tabWindowText, null, windowPanel, null);
        tab.addTab(tabWebText, null, webPanel, null);
        tab.addTab(tabTwitchText, null, twitchPanel, null);
        tab.addTab(tabTimerText, null, timerPanel, null);

        // Adding tab View to the console view panel //
        panel.add(tab);
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

    public TabTimer getTabTimer() {
        return tabTimer;
    }

    public TabTwitch getTabTwitch() {
        return tabTwitch;
    }

    public TabWeb getTabWeb() {
        return tabWeb;
    }

    public TabWindow getTabWindow() {
        return tabWindow;
    }

    public JTabbedPane getTab() {
        return tab;
    }

    public String getSendText() {
        return sendText;
    }

    public String getTabWindowText() {
        return tabWindowText;
    }

    public String getTabTwitchText() {
        return tabTwitchText;
    }

    public String getTabTimerText() {
        return tabTimerText;
    }

    public String getTabWebText() {
        return tabWebText;
    }

    public JPanel get() {
        return panel;
    }

    public TabAll getTabAll() {
        return tabAll;
    }

    public void setTabAll(TabAll tabAll) {
        this.tabAll = tabAll;
    }
}
