package com.github.blombler008.twitchbot.threads;/*
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

import com.github.blombler008.twitchbot.PrintLogger;
import com.github.blombler008.twitchbot.Timeout;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleListener extends Thread {

    private final TwitchIRCListener listener;
    private final PrintLogger logger;

    public ConsoleListener(TwitchIRCListener listener, PrintLogger logger) {
        this.setName("Console-Listener");
        this.logger = logger;
        this.listener = listener;
    }

    @Override
    public void run() {
        boolean breakOut = false;
        while (!breakOut) {
            try {
                Scanner s = new Scanner(System.in);
                String line;
                while (s.hasNextLine()) {
                    line = s.nextLine();
                    if (progress(line)) {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
                breakOut = true;
            }
        }
    }

    public boolean progress(String str) throws IOException {
        logger.writeSeparate("Console Send> " + str, false);
        if (str.equalsIgnoreCase("q") ||
                str.equalsIgnoreCase("exit") ||
                str.equalsIgnoreCase("stop") ||
                str.equalsIgnoreCase("quit")) {
            System.exit(0);
            return true;
        }
        if (str.equalsIgnoreCase("newCatch")) {
            Timeout.newTimeout();
            return true;
        }

        listener.send(str);
        return false;
    }
}
