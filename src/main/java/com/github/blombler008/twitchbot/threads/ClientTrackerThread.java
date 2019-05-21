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
import com.github.blombler008.twitchbot.TwitchBot;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientTrackerThread extends Thread {

    private final PrintLogger logger;

    public ClientTrackerThread(PrintLogger logger) {
        this.setName("ClientTracker-Thread");
        this.logger = logger;
    }

    @Override
    public void run() {

        boolean isBreaking = false;

        String name;
        String prefix;
        Socket client;
        while(!isBreaking) {
            try {
                client = TwitchBot.getWebServerSocket().accept();
                if(client != null) {
                    name = "Web-Listener";
                    prefix = "Web> ";

                    WebListener webListener = new WebListener(prefix, client, name, logger);
                    webListener.start();
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isBreaking = true;
            }
        }
    }
}
