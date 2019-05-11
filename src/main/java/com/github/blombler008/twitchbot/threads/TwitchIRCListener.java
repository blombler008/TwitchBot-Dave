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

import com.github.blombler008.twitchbot.Strings;
import com.github.blombler008.twitchbot.Timeout;

import java.io.*;
import java.sql.Time;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class TwitchIRCListener extends Thread {

    private String prefixB;
    private InputStream in;
    private OutputStreamWriter outWriter;
    private String prefixA;
    private long last;

    public TwitchIRCListener(String prefixA, String prefixB, OutputStream out, InputStream in) {
        this.setName("Twitch-Socket-Listener");
        this.prefixA = prefixA;
        this.prefixB = prefixB;
        this.outWriter = new OutputStreamWriter(out);
        this.in = in;
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

                            string = Strings.JOIN_TEMPLATE;
                            string = string.replaceAll("%channel%", "#tattyplay");
                            send(string);/*
                            string = Strings.JOIN_TEMPLATE;
                            string = string.replaceAll("%channel%", "#binarydave");
                            send(string);
                            */
                        }
                        if(got.length >= 5 && got[2].equalsIgnoreCase("PRIVMSG")) {

                            String string = Strings.MSG_TEMPLATE;
                            string = string.replaceAll("%channel%", got[3]);
                            got[4] = got[4].replaceFirst(":", "");
                            StringBuilder stringBuilder = new StringBuilder();
                            if(got[4].startsWith("!")) {
                                got[4] = got[4].replaceFirst("\\!", "");


                                if(got[4].equalsIgnoreCase("dice")) {
                                    stringBuilder.append("You rolled a ");
                                    stringBuilder.append(new Random().nextInt(5000000));
                                    stringBuilder.append("!");
                                }

                                if(got[4].equalsIgnoreCase("catch")) {
                                    String [] subGot = got[0].split(";");
                                    String name = subGot[3].split("=")[1];
                                    String ssss = Timeout.byTimeout(name);
                                    if(ssss != null) {
                                        stringBuilder.append(ssss);
                                        last = new Date().getTime();
                                    } else {
                                        if((last + 10000) > new Date().getTime()) {
                                            stringBuilder.append("You just missed it! ");
                                            stringBuilder.append(name);
                                            stringBuilder.append(" was first!");
                                        } else {
                                            stringBuilder.append("There is currently no catch on going!");
                                        }
                                    }
                                }


                                /*if(got[4].equalsIgnoreCase("zoggos")) {
                                    String [] subGot = got[0].split(";");
                                    String name = subGot[3].split("=")[1];
                                    stringBuilder.append(name);
                                    stringBuilder.append(" ");
                                    stringBuilder.append("has ");
                                    stringBuilder.append(new Random().nextInt(Integer.MAX_VALUE));
                                    stringBuilder.append(" binary11Zoggos");
                                }*/
                            }
                            string = string.replaceAll("%message%", stringBuilder.toString());
                            // Thread.sleep(3000);
                            send(string);
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
        outWriter.write(message + "\r\n");
        outWriter.flush();
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
            string = string.replaceAll("%name%", "blombler");
            send(string);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
