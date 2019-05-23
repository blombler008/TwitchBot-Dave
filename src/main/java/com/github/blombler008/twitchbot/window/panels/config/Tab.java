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

import javax.swing.*;
import java.awt.*;

public class Tab {

    private JPanel panel;
    private JPanel mainPanel;
    private JPanel changesPanel;

    private JButton buttonCancel;
    private JButton buttonApply;
    private JButton buttonReset;

    private String resetText = "Reset";
    private String applyText = "Apply";
    private String cancelText = "Cancel";

    public Tab() {
        panel = new JPanel();
        mainPanel = new JPanel();
        changesPanel = new JPanel();

        // Panel settings //
        changesPanel.setLayout(new FlowLayout());
        FlowLayout fl_changesPanel = (FlowLayout) changesPanel.getLayout();
        fl_changesPanel.setAlignment(FlowLayout.RIGHT);

        mainPanel.setLayout(null);

        panel.setLayout(new BorderLayout(5, 5));
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(changesPanel, BorderLayout.SOUTH);

        // Buttons //
        buttonCancel = new JButton(cancelText);
        buttonApply = new JButton(applyText);
        buttonReset = new JButton(resetText);

        Dimension defaultSize = new Dimension(90, 22);

        buttonApply.setPreferredSize(defaultSize);
        buttonCancel.setPreferredSize(defaultSize);
        buttonReset.setPreferredSize(defaultSize);

        // Adding Buttons to changesPanel //
        changesPanel.add(buttonApply);
        changesPanel.add(buttonReset);
        changesPanel.add(buttonCancel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getChangesPanel() {
        return changesPanel;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public JButton getButtonApply() {
        return buttonApply;
    }

    public JButton getButtonReset() {
        return buttonReset;
    }
}
