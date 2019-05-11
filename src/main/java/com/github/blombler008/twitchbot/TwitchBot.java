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
import org.json.JSONObject;

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
                WebListener.sites.add("");
                WebListener.sites.add("json");
                WebListener.sites.add("favicon.ico");


                if(instance.startWebListener() ) {
                    if(instance.startTracker()) {
                        Timeout.startTimer();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String[] updateCatch(String aTrue, String name, long diff) {
        try {
            File catchFile = new File("json/index.json");
            BufferedReader reader = new BufferedReader(new FileReader(catchFile));
            StringBuilder builder = new StringBuilder();
            boolean fastest = false;

            String line;
            while((line = reader.readLine() )!= null) {
                builder.append(line);
            }
            System.out.println(builder);
            JSONObject jsonObj = new JSONObject(builder.toString());
            jsonObj.put("catch", aTrue);
            String lastTime = "0";
            String lastWinner = "";
            if(jsonObj.has("bestTime") && jsonObj.has("lastWinner")) {

                lastTime = String.valueOf(jsonObj.getLong("bestTime"));
                lastWinner = jsonObj.getString("lastWinner");

                if (name != null) {
                    jsonObj.put("lastWinner", name);
                }
                if ((diff != -1 && diff < Long.parseLong(lastTime)) || Long.parseLong(lastTime) == -1) {
                    jsonObj.put("bestTime", diff);
                }
            } else {
                if(!jsonObj.has("lastWinner")) jsonObj.put("lastWinner", name) ;
                if(!jsonObj.has("bestTime")) jsonObj.put("bestTime", "-1");
            }
            jsonObj.write(new FileWriter(catchFile)).flush();

            String s [] = new String[] {lastTime, lastWinner};
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean startTracker() {
        try {
            clientTracker = new ClientTrackerThread();
            clientTracker.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<WebListener> getWebClientThreads() {
        return instance.threadTracker;
    }
    public static Thread getWebClientThread() {
        return instance.clientTracker;
    }

    public static ServerSocket getWebServerSocket() {
        return instance.webServerSocket;
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
            webServerSocket.setReuseAddress(true);
            /*clientTracker = new ClientTrackerThread();
            clientTracker.start();*/
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
