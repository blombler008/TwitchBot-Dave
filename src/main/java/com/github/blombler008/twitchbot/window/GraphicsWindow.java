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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GraphicsWindow {

    private final TwitchBot instance;
    private final GUIGraphicsWindow frame;

    public GraphicsWindow(TwitchBot instance) {
        this.instance = instance;

        frame = new GUIGraphicsWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Dave's - TwitchBot Manager");
                try {
            this.frame.setIconImage(ImageIO.read(instance.getClass().getResource("/favicon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean start() {
        try {
            String s = setLookAndFeel();
            if (s != null) {
                UIManager.setLookAndFeel(s);
            }
            frame.initialize();
            if(frame.isInitialized()) {
                frame.setVisible(true);
                frame.getConfigPanel().getTabWindow().resetAll();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public String setLookAndFeel() {
        try {
            UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
            for(UIManager.LookAndFeelInfo info: infos ) {
                if(info.getClassName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
                    return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
