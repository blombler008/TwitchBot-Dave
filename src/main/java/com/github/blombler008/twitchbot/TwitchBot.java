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
import com.github.blombler008.twitchbot.window.GraphicsWindow;
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
    private static Properties config;
    private static boolean noGraph = false;

    private List<WebListener> threadTracker;
    private ClientTrackerThread clientTracker;
    private ServerSocket webServerSocket;

    public static void main(String[] args) {

        try {
            instance = new TwitchBot();

            if(instance.checkArgs(args)) {
                if (instance.parseConfig(Strings.CONFIG_FILE)) {
                    if (instance.checkConfig(Strings.CONFIG_FILE)) {

                        if (noGraph) {
                            if (instance.startTwitchIRC()) {
                                WebListener.sites.add("");
                                WebListener.sites.add("json");
                                WebListener.sites.add("favicon.ico");
                                WebListener.sites.add("getPenguin");

                                if (instance.startWebListener()) {
                                    if (instance.startTracker()) {
                                        Timeout.startTimer();
                                    }
                                }
                            }
                        } else {
                            GraphicsWindow graphicsWindow = new GraphicsWindow(instance);
                            graphicsWindow.start();
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean checkArgs(String[] args) {
        try {
            if(args.length > 0) {
                for (String s: args) {
                    if(s.startsWith("--")) {
                        String command = s.replaceFirst("--", "");
                        if(command.equals("noGraph")) {
                            noGraph = true;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Properties getConfig() {
        return config;
    }

    public boolean checkConfig(String file) {
        try {
            File File = new File(file);
            if(!config.containsKey(Strings.CONFIG_MIN_TIME))
                config.put(Strings.CONFIG_MIN_TIME, "60000");

            if(!config.containsKey(Strings.CONFIG_MAX_TIME))
                config.put(Strings.CONFIG_MAX_TIME, "300000");

            if(!config.containsKey(Strings.CONFIG_PORT))
                config.put(Strings.CONFIG_PORT, "8087");

            if(!config.containsKey(Strings.CONFIG_PENGUIN_LOCATION))
                config.put(Strings.CONFIG_PENGUIN_LOCATION, "html/penguin.png");

            if(!config.containsKey(Strings.CONFIG_TWITCH_CHANNEL))
                config.put(Strings.CONFIG_TWITCH_CHANNEL, "binarydave");

            if(!config.containsKey(Strings.CONFIG_FIRST_CATCH))
                config.put(Strings.CONFIG_FIRST_CATCH, "%name% caught the penguin first!");

            if(!config.containsKey(Strings.CONFIG_NO_CATCH))
                config.put(Strings.CONFIG_NO_CATCH, "There is currently no catch on going!");

            if(!config.containsKey(Strings.CONFIG_AFTER_CATCH_MESSAGE))
                config.put(Strings.CONFIG_AFTER_CATCH_MESSAGE, "You just missed it! %name% was first!");

            if(!config.containsKey(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND))
                config.put(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND, "!addpoints %name% 5000");

            if(!config.containsKey(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND_ENABLE))
                config.put(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND_ENABLE, "true");

            if(!config.containsKey(Strings.CONFIG_AFTER_CATCH_ENABLE))
                config.put(Strings.CONFIG_AFTER_CATCH_ENABLE, "true");

            if(!config.containsKey(Strings.CONFIG_DICE_ENABLE))
                config.put(Strings.CONFIG_DICE_ENABLE, "true");

            if(!config.containsKey(Strings.CONFIG_AFTER_CATCH_TIME))
                config.put(Strings.CONFIG_AFTER_CATCH_TIME, "10000");

            if(!config.containsKey(Strings.CONFIG_CATCH_ENABLE))
                config.put(Strings.CONFIG_CATCH_ENABLE, "true");

            if(!config.containsKey(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER))
                config.put(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER, "false");

            if(!config.containsKey(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER_MESSAGE))
                config.put(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER_MESSAGE, "You won last catch already!");


            config.store(new FileWriter(File), "configs of TwitchBot-Dave");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean parseConfig(String file) {
        try {
            File File = new File(file);
            if(!File.exists()) {
                File.createNewFile();

            } else if (File.isDirectory()) {
                File.delete();
                File.createNewFile();
            }
            config = new Properties();
            config.load(new FileReader(File));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String[] updateCatch(String aTrue, String name, long diff) {
        try {
            File catchFile = new File("json/index.json");
            BufferedReader reader = new BufferedReader(new FileReader(catchFile));
            StringBuilder builder = new StringBuilder();

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

            return new String[] {lastTime, lastWinner};
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCatchWinner() {
        try {
            File catchFile = new File("json/index.json");
            BufferedReader reader = new BufferedReader(new FileReader(catchFile));
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine() )!= null) {
                builder.append(line);
            }
            System.out.println(builder);
            JSONObject jsonObj = new JSONObject(builder.toString());

            if(!jsonObj.has("lastWinner")) {
                if(updateCatch("false", "", -1) != null) {
                    return getCatchWinner();
                }
            }

            return jsonObj.getString("lastWinner");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean startTracker() {
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
            String port = config.getProperty(Strings.CONFIG_PORT);

            webServerSocket = new ServerSocket(Integer.parseInt(port));
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
