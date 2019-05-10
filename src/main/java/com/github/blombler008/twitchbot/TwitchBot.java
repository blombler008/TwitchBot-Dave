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

import com.github.blombler008.twitchbot.threads.ClientTrackerThread;
import com.github.blombler008.twitchbot.threads.ConsoleListener;
import com.github.blombler008.twitchbot.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.threads.WebListener;
import com.sun.deploy.net.HttpRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import sun.net.httpserver.HttpServerImpl;

import java.io.*;
import java.net.*;
import java.util.*;

public class TwitchBot {
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static String prefixGot = "> ";
    private static String prefixSend = "< ";
    private static TwitchBot instance;

    private List<WebListener> threadTracker;
    private ClientTrackerThread clientTracker;
    private ServerSocket webServerSocket;

    public static void main(String[] args) {

        try {
            instance = new TwitchBot();
            if(instance.startTwitchIRC()) {
                if(instance.startWebListener() ) {
                    boolean isBreaking = false;

                    String name;
                    String prefix;
                    Socket client;
                    while(!isBreaking) {
                        try {
                            client = instance.webServerSocket.accept();
                            if(client != null) {
                                name = "Web-Listener";
                                prefix = "Web> ";

                                WebListener webListener = new WebListener(prefix, client, name);
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
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static List<WebListener> getWebClientThreads() {
        return instance.threadTracker;
    }
    public static Thread getWebClientThread() {
        return instance.clientTracker;
    }

    public boolean startTwitchIRC() {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(Strings.SERVER, Integer.parseInt(Strings.PORT)));
            inputStream = s.getInputStream();
            outputStream = s.getOutputStream();

            TwitchIRCListener twitchIRCListener = new TwitchIRCListener(prefixGot, prefixSend, outputStream, inputStream);
            ConsoleListener consoleListener = new ConsoleListener(twitchIRCListener);

            twitchIRCListener.start();
            consoleListener.start();

            twitchIRCListener.login();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean startWebListener() {
        threadTracker = new ArrayList<>();
        try {
            /*
            HttpServer server = HttpServer.create(new InetSocketAddress(8087), 0) ;
            server.createContext("/", t -> {
                String response = "This is the response";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                System.out.println(t.getRemoteAddress().toString());
              });

            server.start();
            return false;
*/


            webServerSocket = new ServerSocket(8080);

            /*clientTracker = new ClientTrackerThread();
            clientTracker.start();*/
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
