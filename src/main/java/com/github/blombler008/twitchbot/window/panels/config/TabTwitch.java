package com.github.blombler008.twitchbot.window.panels.config;/*
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
import com.github.blombler008.twitchbot.window.panels.config.twitch.TabCatch;
import com.github.blombler008.twitchbot.window.panels.config.twitch.TabGeneral;

import javax.swing.*;
import java.awt.*;

public class TabTwitch extends Tab {

    private TabCatch tabCatch;
    private TabGeneral tabGeneral;
    private JPanel panel;
    private JPanel general;
    private JPanel katch;
    private JTabbedPane tab;

    private String generalTabText = "General";
    private String catchTabText = "Catch";

    public TabTwitch(GUIGraphicsWindow frame) {
        panel = new JPanel();

        tab = new JTabbedPane(JTabbedPane.LEFT);

        // Tabs //
        tabGeneral = new TabGeneral();
        tabCatch = new TabCatch();

        // panel setings //
        panel.setLayout(new BorderLayout(10, 10));


        // Panels //
        general = tabGeneral.getPanel();
        katch = tabCatch.getPanel();

        // adding panels to twitch //
        tab.addTab(generalTabText, null, general, null);
        tab.addTab(catchTabText, null, katch, null);

        // adding tab to panel //
        panel.add(tab);
    }

    public JPanel get() {
        return panel;
    }
}
