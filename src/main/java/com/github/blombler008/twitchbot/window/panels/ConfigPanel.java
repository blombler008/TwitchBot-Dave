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
import com.github.blombler008.twitchbot.window.panels.config.TabTimer;
import com.github.blombler008.twitchbot.window.panels.config.TabTwitch;
import com.github.blombler008.twitchbot.window.panels.config.TabWeb;
import com.github.blombler008.twitchbot.window.panels.config.TabWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ConfigPanel {


    private String tabWindowText = "Window";
    private String tabWebText = "Web";
    private String tabTwitchText = "Twitch";
    private String tabTimerText = "Timer";

    private JTabbedPane tab;

    private JPanel webPanel;
    private JPanel timerPanel;
    private JPanel twitchPanel;
    private JPanel windowPanel;

    private JPanel panel;

    private TabWindow tabWindow;
    private TabWeb tabWeb;
    private TabTwitch tabTwitch;
    private TabTimer tabTimer;

    public ConfigPanel(GUIGraphicsWindow frame) {

        panel = new JPanel(); // config view panel
        tab = new JTabbedPane(JTabbedPane.TOP);  // config view tab -> config view panel

        // config panel //
        panel.setLayout(new BorderLayout(5, 5));

        // Tabs //
        tabWindow = new TabWindow(frame);
        tabWeb = new TabWeb();
        tabTimer = new TabTimer();
        tabTwitch = new TabTwitch();

        // Tab configs //

        // Listener //
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                tabWindow.updatePosition(((GUIGraphicsWindow) e.getSource()).getLocationOnScreen());
            }

            @Override
            public void componentResized(ComponentEvent e) {
                tabWindow.updateSize(((GUIGraphicsWindow) e.getSource()).getSize());
            }
        });


        // Tab Panels //
        windowPanel = tabWindow.getPanel();
        webPanel = tabWeb.get();
        timerPanel = tabTimer.get();
        twitchPanel = tabTwitch.get();

/*
        webPanel.setLayout(null);
        timerPanel.setLayout(null);
        twitchPanel.setLayout(null);
*/

        // Adding all log panels to the tab View //
        tab.addTab(tabWindowText, null, windowPanel, null); // Selected Tab
        //tab.addTab(tabTimerText, null, timerPanel, null);
        //tab.addTab(tabWebText, null, webPanel, null);
        tab.addTab(tabTwitchText, null, twitchPanel, null);


        // Adding tab View to the config view panel //
        panel.add(tab);

    }

    public TabWindow getTabWindow() {
        return tabWindow;
    }

    public TabWeb getTabWeb() {
        return tabWeb;
    }

    public TabTwitch getTabTwitch() {
        return tabTwitch;
    }

    public TabTimer getTabTimer() {
        return tabTimer;
    }

    public JPanel get() {
        return panel;
    }

    public static class Entry {
        int method;
        String label;

        public Entry(String label, int method) {
            this.label = label;
            this.method = method;
        }

        @Override
        public String toString() {
            return label;
        }

        public int getMethod() {
            return method;
        }
    }

}
