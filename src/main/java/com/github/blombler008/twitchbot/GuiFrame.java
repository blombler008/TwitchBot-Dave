package com.github.blombler008.twitchbot;/*
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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GuiFrame extends JFrame {

    public JTextArea text;
    public JTextField field;

    public GuiFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Dave's - TwitchBot Manager");
        setMinimumSize(new Dimension(1024, 576));
        setResizable(false);
        setLayout(new BorderLayout(5,5));

        JPanel root = new JPanel(new BorderLayout(5,5));
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        text = new JTextArea();
        field = new JTextField();

        text.setEditable(false);

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        TwitchBot.send(field.getText());
                        text.append(field.getText() + System.lineSeparator());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    field.setText("");
                }
            }
        });


        panel.add(text, BorderLayout.CENTER);
        panel.add(field, BorderLayout.SOUTH);

        root.add(panel);
        setContentPane(root);

        try {
            setIconImage(ImageIO.read(this.getClass().getResource("/favicon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        new Thread(() -> {
            boolean bs = true;
            while(bs) {
                try {

                    if(isFocused()) {
                        field.requestFocusInWindow();
                    }
                    Thread.yield();
                } catch (Exception ignore) {
                    bs = false;
                }
            }
        }).start();
    }
}
