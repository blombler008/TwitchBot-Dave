package com.github.blombler008.twitchbot.window.panels.console;/*
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

import com.github.blombler008.twitchbot.GuiFrame;
import com.github.blombler008.twitchbot.TwitchBot;
import com.github.blombler008.twitchbot.window.GUIGraphicsWindow;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class TabWindow {

    private GUIGraphicsWindow window;

    private JPanel panel;
    private JTextArea log;

    public TabWindow(GUIGraphicsWindow window) {

        this.window = window;
        panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));

        log = new JTextArea();
        log.setEditable(false);
        JScrollPane sp = new JScrollPane(log);
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        panel.add(sp);
    }


    public void log(String s) {
        window.getConsolePanel().getTabAll().log(s);
        if(!s.endsWith(System.lineSeparator())) log.append(s + System.lineSeparator());
        else log.append(s);
    }

    public JPanel get() {
        return panel;
    }

}
