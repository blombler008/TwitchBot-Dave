package com.github.blombler008.twitchbot.window.panels.config.twitch;/*
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

import com.github.blombler008.twitchbot.window.panels.Tab;

import javax.swing.*;

public class TabGeneral extends Tab {

    private JTextField textFieldChannel;
    private JTextField textFieldNickname;
    private JPasswordField passwordField;

    private JLabel labelNickname;
    private JLabel labelChannel;
    private JLabel labelPassword;


    private String labelPasswordText = "Password:";
    private String labelChannelText = "Channel:";
    private String labelNicknameText = "Nickname:";

    private JPanel panel;

    public TabGeneral() {
        panel = getMainPanel();
        // Buttons //

        // Labels //
        labelPassword = new JLabel(labelPasswordText);
        labelChannel = new JLabel(labelChannelText);
        labelNickname = new JLabel(labelNicknameText);

        // Text Fields //
        passwordField = new JPasswordField();

        textFieldChannel = new JTextField();
        textFieldNickname = new JTextField();

        // Setting components //
        // Labels //
        labelPassword.setHorizontalAlignment(SwingConstants.RIGHT);
        labelChannel.setHorizontalAlignment(SwingConstants.RIGHT);
        labelNickname.setHorizontalAlignment(SwingConstants.RIGHT);

        // Text fields //
        passwordField.setColumns(10);
        textFieldChannel.setColumns(10);
        textFieldNickname.setColumns(10);

        // Setting components Bounds //
        // Button //

        // Labels //
        labelPassword.setBounds(10, 13, 118, 22);
        labelChannel.setBounds(10, 46, 118, 22);
        labelNickname.setBounds(10, 79, 118, 22);

        // Text Fields //
        passwordField.setBounds(138, 13, 262, 22);
        textFieldChannel.setBounds(138, 46, 262, 22);
        textFieldNickname.setBounds(138, 79, 262, 22);


        // Adding Components to panel //
        panel.add(labelNickname);
        panel.add(labelChannel);
        panel.add(labelPassword);

        panel.add(passwordField);

        panel.add(textFieldNickname);
        panel.add(textFieldChannel);

    }

    public JPanel get() {
        return panel;
    }
}
