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

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Tab {

    private JTextArea jtextArea;
    private JScrollPane sp;

    public Tab() {
        jtextArea = new JTextArea();

        sp = new JScrollPane(jtextArea);
        DefaultCaret caret = (DefaultCaret)jtextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        jtextArea.setEditable(false);
        jtextArea.addMouseListener(new PopClickListener());

    }

    public JScrollPane getSP() {
        return sp;
    }

    public JTextArea getLog() {
        return jtextArea;
    }
    public void log(String s) {
        if(!s.endsWith(System.lineSeparator())) jtextArea.append(s + System.lineSeparator());
        else jtextArea.append(s);
    }

    class PopUpDemo extends JPopupMenu {

        JMenuItem anCopyItem;
        JMenuItem anCutItem;

        public PopUpDemo() {

            anCopyItem = new JMenuItem("Copy");
            anCutItem = new JMenuItem("Cut");

            add(anCopyItem);
            add(anCutItem);

            anCopyItem.addMouseListener(new MouseKeyClickListener());
            anCutItem.addMouseListener(new MouseKeyClickListener());


        }
        class MouseKeyClickListener implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getSource().equals(anCopyItem)) {
                    jtextArea.copy();
                }
                if(e.getSource().equals(anCutItem)) {
                    jtextArea.cut();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }
    }

    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                doPop(e);
            }
        }

        private void doPop(MouseEvent e) {
            PopUpDemo menu = new PopUpDemo();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }


}
