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

import com.github.blombler008.twitchbot.commands.CommandCatch;
import com.github.blombler008.twitchbot.commands.CommandDice;
import com.github.blombler008.twitchbot.commands.CommandNewCatch;
import com.github.blombler008.twitchbot.threads.ClientTrackerThread;
import com.github.blombler008.twitchbot.threads.ConsoleListener;
import com.github.blombler008.twitchbot.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.threads.WebListener;
import com.github.blombler008.twitchbot.window.GraphicsWindow;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class TwitchBot {
    // TODO: convert full frame to permanent Frame
    public static GuiFrame frame;
    private static TwitchBot instance;
    private static Properties config;
    private static boolean noGraph = false;
    private static boolean mergeOld = false;
    private static boolean nothing = true;
    private static PrintLogger logger;
    private static ConsoleListener consoleManager;
    private List<WebListener> threadTracker;
    private ClientTrackerThread clientTracker;
    private ServerSocket webServerSocket;
    private TwitchIRCListener twitchIRCListener;

    public static void main(String[] args) throws FileNotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");

        File logs = new File("logs");
        File log = new File(logs, File.separator + "log-" + formatter.format(new Date()) + ".txt.log");

        frame = new GuiFrame();

        logs.mkdirs();
        logger = new PrintLogger(System.out);
        logger.out = System.out;
        logger.lg = new PrintStream(log);
        logger.jTextArea = frame.text;
        logger.jtextNotNull = true;

        System.setOut(logger);

        try {
            instance = new TwitchBot();

            if (instance.checkArgs(args)) {
                if (instance.parseConfig(Strings.CONFIG_FILE)) {
                    if (instance.checkConfig(Strings.CONFIG_FILE)) {
                        if (instance.checkOldConfig(Strings.CONFIG_FILE) && mergeOld) {
                            instance.checkConfig(Strings.CONFIG_FILE);
                            return;
                        }
                        if (nothing || noGraph) {
                            if (nothing)
                                instance.startNewConsole();
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

    public static void send(String text) throws IOException {
        consoleManager.progress(text);
    }

    public static Properties getConfig() {
        return config;
    }

    public static String[] updateCatch(String aTrue, String name, long diff) {
        try {
            File catchFile = new File("json/index.json");
            BufferedReader reader = new BufferedReader(new FileReader(catchFile));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            System.out.println(builder);
            JSONObject jsonObj = new JSONObject(builder.toString());
            jsonObj.put("catch", aTrue);
            String lastTime = "0";
            String lastWinner = "";
            if (jsonObj.has("bestTime") && jsonObj.has("lastWinner")) {

                lastTime = String.valueOf(jsonObj.getLong("bestTime"));
                lastWinner = jsonObj.getString("lastWinner");

                if (name != null) {
                    jsonObj.put("lastWinner", name);
                }
                if ((diff != -1 && diff < Long.parseLong(lastTime)) || Long.parseLong(lastTime) == -1) {
                    jsonObj.put("bestTime", diff);
                }
            } else {
                if (!jsonObj.has("lastWinner"))
                    jsonObj.put("lastWinner", name);
                if (!jsonObj.has("bestTime"))
                    jsonObj.put("bestTime", "-1");
            }


            jsonObj.write(new FileWriter(catchFile)).flush();

            return new String[]{lastTime, lastWinner};
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
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            System.out.println(builder);
            JSONObject jsonObj = new JSONObject(builder.toString());

            if (!jsonObj.has("lastWinner")) {
                if (updateCatch("false", "", -1) != null) {
                    return getCatchWinner();
                }
            }

            return jsonObj.getString("lastWinner");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

    public static TwitchIRCListener getTwitchIRCListener() {
        return instance.twitchIRCListener;
    }

    private void startNewConsole() {
        frame.setVisible(true);
    }

    private boolean checkOldConfig(String configFile) {
        try {
            File f = new File(configFile);
            if (config.containsKey(Strings.CONFIG_OLD_MIN_TIME)) {
                config.put(Strings.CONFIG_TIMER_MIN, config.getProperty(Strings.CONFIG_OLD_MIN_TIME));
                config.remove(Strings.CONFIG_OLD_MIN_TIME);
            }
            if (config.containsKey(Strings.CONFIG_OLD_MAX_TIME)) {
                config.put(Strings.CONFIG_TIMER_MAX, config.getProperty(Strings.CONFIG_OLD_MAX_TIME));
                config.remove(Strings.CONFIG_OLD_MAX_TIME);
            }
            if (config.containsKey(Strings.CONFIG_OLD_PENGUIN_LOCATION)) {
                config.put(Strings.CONFIG_REWARD_LOCATION, config.getProperty(Strings.CONFIG_OLD_PENGUIN_LOCATION));
                config.remove(Strings.CONFIG_OLD_PENGUIN_LOCATION);
            }
            if (config.containsKey(Strings.CONFIG_OLD_TWITCH_CHANNEL)) {
                config.put(Strings.CONFIG_TWITCH_CHANNEL, config.getProperty(Strings.CONFIG_OLD_TWITCH_CHANNEL));
                config.remove(Strings.CONFIG_OLD_TWITCH_CHANNEL);
            }
            if (config.containsKey(Strings.CONFIG_OLD_FIRST_CATCH)) {
                config.put(Strings.CONFIG_CATCH_WINNER_MESSAGE, config.getProperty(Strings.CONFIG_OLD_FIRST_CATCH));
                config.remove(Strings.CONFIG_OLD_FIRST_CATCH);
            }
            if (config.containsKey(Strings.CONFIG_OLD_NO_CATCH)) {
                config.put(Strings.CONFIG_CATCH_NO, config.getProperty(Strings.CONFIG_OLD_NO_CATCH));
                config.remove(Strings.CONFIG_OLD_NO_CATCH);
            }
            if (config.containsKey(Strings.CONFIG_OLD_AFTER_CATCH_MESSAGE)) {
                config.put(Strings.CONFIG_CATCH_MISSED_MESSAGE, config.getProperty(Strings.CONFIG_OLD_AFTER_CATCH_MESSAGE));
                config.remove(Strings.CONFIG_OLD_AFTER_CATCH_MESSAGE);
            }
            if (config.containsKey(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND)) {
                config.put(Strings.CONFIG_CATCH_WINNER_COMMAND, config.getProperty(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND));
                config.remove(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND);
            }
            if (config.containsKey(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND_ENABLE)) {
                config.put(Strings.CONFIG_CATCH_WINNER_COMMAND_ENABLE, config.getProperty(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND_ENABLE));
                config.remove(Strings.CONFIG_OLD_AFTER_CATCH_CHAT_COMMAND_ENABLE);
            }
            if (config.containsKey(Strings.CONFIG_OLD_AFTER_CATCH_ENABLE)) {
                config.put(Strings.CONFIG_CATCH_MISSED_ENABLE, config.getProperty(Strings.CONFIG_OLD_AFTER_CATCH_ENABLE));
                config.remove(Strings.CONFIG_OLD_AFTER_CATCH_ENABLE);
            }
            if (config.containsKey(Strings.CONFIG_OLD_DICE_ENABLE)) {
                config.put(Strings.CONFIG_DICE_ENABLE, config.getProperty(Strings.CONFIG_OLD_DICE_ENABLE));
                config.remove(Strings.CONFIG_OLD_DICE_ENABLE);
            }
            if (config.containsKey(Strings.CONFIG_OLD_AFTER_CATCH_TIME)) {
                config.put(Strings.CONFIG_CATCH_MISSED_TIME, config.getProperty(Strings.CONFIG_OLD_AFTER_CATCH_TIME));
                config.remove(Strings.CONFIG_OLD_AFTER_CATCH_TIME);
            }
            if (config.containsKey(Strings.CONFIG_OLD_CATCH_ENABLE)) {
                config.put(Strings.CONFIG_CATCH_ENABLE, config.getProperty(Strings.CONFIG_OLD_CATCH_ENABLE));
                config.remove(Strings.CONFIG_OLD_CATCH_ENABLE);
            }
            if (config.containsKey(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER)) {
                config.put(Strings.CONFIG_CATCH_WINNER_REPEAT_ENABLE, config.getProperty(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER));
                config.remove(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER);
            }
            if (config.containsKey(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER_MESSAGE)) {
                config.put(Strings.CONFIG_CATCH_WINNER_REPEAT_MESSAGE, config.getProperty(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER_MESSAGE));
                config.remove(Strings.CONFIG_OLD_CATCH_REPEAT_SAME_WINNER_MESSAGE);
            }

            config.store(new FileWriter(f), "configs of TwitchBot-Dave");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkArgs(String[] args) {
        try {
            if (args.length > 0) {
                for (String s : args) {
                    if (s.startsWith("--")) {
                        String command = s.replaceFirst("--", "");
                        if (command.equals("noGraph")) {
                            noGraph = true;
                            nothing = false;
                        }
                        if (command.equals("mergeOld")) {
                            mergeOld = true;
                        }
                        if (command.equalsIgnoreCase("testVersion")) {
                            nothing = false;
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

    public boolean checkConfig(String file) {
        try {
            File File = new File(file);
            if (!config.containsKey(Strings.CONFIG_TIMER_MIN))
                config.put(Strings.CONFIG_TIMER_MIN, "60000");

            if (!config.containsKey(Strings.CONFIG_TIMER_MAX))
                config.put(Strings.CONFIG_TIMER_MAX, "300000");

            if (!config.containsKey(Strings.CONFIG_PORT))
                config.put(Strings.CONFIG_PORT, "8087");

            if (!config.containsKey(Strings.CONFIG_REWARD_LOCATION))
                config.put(Strings.CONFIG_REWARD_LOCATION, "html/penguin.png");

            if (!config.containsKey(Strings.CONFIG_TWITCH_CHANNEL))
                config.put(Strings.CONFIG_TWITCH_CHANNEL, "binarydave");

            if (!config.containsKey(Strings.CONFIG_CATCH_WINNER_MESSAGE))
                config.put(Strings.CONFIG_CATCH_WINNER_MESSAGE, "%name% caught the penguin first!");

            if (!config.containsKey(Strings.CONFIG_CATCH_NO))
                config.put(Strings.CONFIG_CATCH_NO, "There is currently no catch on going!");

            if (!config.containsKey(Strings.CONFIG_CATCH_MISSED_MESSAGE))
                config.put(Strings.CONFIG_CATCH_MISSED_MESSAGE, "You just missed it! %name% was first!");

            if (!config.containsKey(Strings.CONFIG_CATCH_WINNER_COMMAND))
                config.put(Strings.CONFIG_CATCH_WINNER_COMMAND, "!addpoints %name% 5000");

            if (!config.containsKey(Strings.CONFIG_CATCH_WINNER_COMMAND_ENABLE))
                config.put(Strings.CONFIG_CATCH_WINNER_COMMAND_ENABLE, "true");

            if (!config.containsKey(Strings.CONFIG_CATCH_MISSED_ENABLE))
                config.put(Strings.CONFIG_CATCH_MISSED_ENABLE, "true");

            if (!config.containsKey(Strings.CONFIG_DICE_ENABLE))
                config.put(Strings.CONFIG_DICE_ENABLE, "true");

            if (!config.containsKey(Strings.CONFIG_CATCH_MISSED_TIME))
                config.put(Strings.CONFIG_CATCH_MISSED_TIME, "10000");

            if (!config.containsKey(Strings.CONFIG_CATCH_ENABLE))
                config.put(Strings.CONFIG_CATCH_ENABLE, "true");

            if (!config.containsKey(Strings.CONFIG_CATCH_WINNER_REPEAT_ENABLE))
                config.put(Strings.CONFIG_CATCH_WINNER_REPEAT_ENABLE, "false");

            if (!config.containsKey(Strings.CONFIG_CATCH_WINNER_REPEAT_MESSAGE))
                config.put(Strings.CONFIG_CATCH_WINNER_REPEAT_MESSAGE, "You won last catch already!");


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
            if (!File.exists()) {
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

    public boolean startTracker() {
        try {
            clientTracker = new ClientTrackerThread(logger);
            clientTracker.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean startTwitchIRC() {
        try {
            boolean diceEnable = Boolean.parseBoolean(config.getProperty(Strings.CONFIG_DICE_ENABLE));
            boolean catchEnable = Boolean.parseBoolean(config.getProperty(Strings.CONFIG_DICE_ENABLE));

            Socket s = new Socket();
            s.connect(new InetSocketAddress(Strings.SERVER, Integer.parseInt(Strings.PORT)));
            InputStream inputStream = s.getInputStream();
            OutputStream outputStream = s.getOutputStream();

            twitchIRCListener = new TwitchIRCListener(outputStream, inputStream, logger);
            consoleManager = new ConsoleListener(twitchIRCListener, logger);

            if (catchEnable)
                twitchIRCListener.addCommand(new CommandCatch());
            if (diceEnable)
                twitchIRCListener.addCommand(new CommandDice());
            twitchIRCListener.addCommand(new CommandNewCatch());
            //twitchIRCListener.addCommand(new CommandZoggos());

            twitchIRCListener.start();
            consoleManager.start();

            twitchIRCListener.login();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean stopTwitchIRC() {
        return false; // TODO: Later implementation
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
