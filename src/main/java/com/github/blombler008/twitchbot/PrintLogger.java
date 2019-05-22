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

import javax.swing.*;
import java.io.*;

public class PrintLogger extends PrintStream {

    public PrintStream lg;
    public PrintStream out;
    public JTextArea jTextArea;
    public boolean jtextNotNull = false;

    public PrintLogger(OutputStream out) {
        super(out);
    }

    public PrintLogger(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintLogger(OutputStream out, boolean autoFlush, String encoding)
            throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public PrintLogger(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrintLogger(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrintLogger(File file) throws FileNotFoundException {
        super(file);
    }

    public PrintLogger(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public void logTimer(String s) {
        TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabTimer().log(s);
        lg.println(s);
        out.println(s);
    }

    public void logTwitch(String s) {
        TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabTwitch().log(s);
        lg.println(s);
        out.println(s);
    }

    public void logWeb(String s) {
        TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabWeb().log(s);
        lg.println(s);
        out.println(s);
    }

    public void logWindow(String s) {
        TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabWindow().log(s);
        lg.println(s);
        out.println(s);
    }

    @Override
    public void flush() {
        lg.flush();
        out.flush();
    }

    @Override
    public void close() {
        lg.close();
        out.close();
    }

    @Override
    public void write(int b) {
        lg.write(b);
        out.write(b);
        jTextArea.append(String.valueOf((char) b));
        if(TwitchBot.isNewFrame()) TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabAll().log(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        lg.write(buf, off, len);
        out.write(buf, off, len);
        try {
            jTextArea.append(new String(buf, off, len, "UTF-8"));
            if(TwitchBot.isNewFrame())TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabAll().log(new String(buf, off, len, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        lg.write(b);
        out.write(b);
        jTextArea.append(new String(b, "UTF-8"));
        if(TwitchBot.isNewFrame())TwitchBot.getGraphicsWindow().getFrame().getConsolePanel().getTabAll().log(new String(b, "UTF-8"));
    }

    public void writeSeparate(int str, boolean console) {

        if (console) {
            write(str);
        } else {
            lg.write(str);
        }
    }

    public void writeSeparate(String str, boolean console) {

        if (console) {
            println(str);
        } else {
            lg.println(str);
        }
    }
}
