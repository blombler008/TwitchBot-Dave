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
import com.github.blombler008.twitchbot.Strings;
import com.github.blombler008.twitchbot.TwitchBot;
import com.github.blombler008.twitchbot.commands.Command;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TwitchIRCListener extends Thread {

    private static String prefixB = "> ";
    private static String prefixA = "< ";
    private InputStream in;
    private OutputStreamWriter outWriter;
    private List<Command> commands = new ArrayList<>();
    private final PrintLogger logger;

    public TwitchIRCListener(OutputStream out, InputStream in, PrintLogger logger) {
        this.setName("Twitch-Socket-Listener");
        this.outWriter = new OutputStreamWriter(out);
        this.in = in;
        this.logger = logger;
    }

    @Override
    public void run() {
        boolean breakOut = false;
        while (!breakOut) {
            try {
                Scanner s = new Scanner(in);
                String line;
                String [] got;

                while(s.hasNextLine()) {
                    line = s.nextLine();
                    System.out.println(prefixB + line);
                    got = line.split("\\s+");
                    if(got.length >= 1) {
                        if(got.length == 2 && got[0].equalsIgnoreCase("ping")) {
                            send(Strings.PONG_TEMPLATE);
                            System.out.println(prefixA + "pong sent!");
                        }
                        if(got.length >= 3 && got[1].equalsIgnoreCase("376")) {
                            send(Strings.CAP_CHAT_COMMANDS);
                            send(Strings.CAP_MEMBERSHIP);
                            send(Strings.CAP_COMMANDS);
                            send(Strings.CAP_TAGS);

                            String string;

                            String channel = TwitchBot.getConfig().getProperty(Strings.CONFIG_TWITCH_CHANNEL);
                            if (!channel.startsWith("#")) {
                                channel = "#" + channel;
                            }


                            string = Strings.JOIN_TEMPLATE;
                            string = string.replaceAll("%channel%", channel);
                            send(string);/*
                            string = Strings.JOIN_TEMPLATE;
                            string = string.replaceAll("%channel%", "#binarydave");
                            send(string);
                            */
                        }
                        if(got.length >= 5 && got[2].equalsIgnoreCase("PRIVMSG")) {

                            got[4] = got[4].replaceFirst(":", "");

                            if(got[4].startsWith("!")) {
                                got[4] = got[4].replaceFirst("\\!", "");
                                for(Command cmd: commands) {
                                    if(got[4].equalsIgnoreCase(cmd.getCommandName())) {
                                        String outCome = cmd.run(got, this);

                                        if(outCome != null) {
                                            String string = Strings.MSG_TEMPLATE;
                                            string = string.replaceAll("%channel%", got[3]);
                                            string = string.replaceAll("%message%", outCome);
                                            // Thread.sleep(3000);
                                            send(string);
                                        }

                                    }
                                }
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
                breakOut = true;
            }
        }

    }
    public void send(String message) throws IOException {

        logger.writeSeparate("Send Twitch> " + message, false);
        outWriter.write(message + "\r\n");
        outWriter.flush();
    }

    public void addCommand(Command cmd) {
        commands.add(cmd);
    }

    public boolean login() {
        try {
            StringBuilder password = new StringBuilder();
            File passFile = new File("password.txt");
            BufferedReader bf = new BufferedReader(new FileReader(passFile));

            String ls;
            while((ls = bf.readLine()) != null) {
                password.append(ls);
            }
            bf.close();

            String string = Strings.PASS_TEMPLATE;
            string = string.replaceAll("%oauth%", password.toString());
            send(string);

            string = Strings.NICK_TEMPLATE;
            string = string.replaceAll("%name%", "dave-bot");
            send(string);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
