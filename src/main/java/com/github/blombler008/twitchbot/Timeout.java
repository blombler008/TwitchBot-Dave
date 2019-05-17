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

import java.util.Date;
import java.util.Random;

public class Timeout {
    private static boolean katch = false;

    private static long autoTimeout;
    private static Thread thread;
    private static String winner;
    private static int low;
    private static int high;
    private static long sleep;
    private static Random random = new Random();

    static{
        low = Integer.parseInt(TwitchBot.getConfig().getProperty(Strings.CONFIG_TIMER_MIN));
        high = Integer.parseInt(TwitchBot.getConfig().getProperty(Strings.CONFIG_TIMER_MAX));
        winner = TwitchBot.getCatchWinner();
    }

    public static boolean byTimeout() {
        return katch;
    }

    public static void startTimer() {
        thread = new Thread(() -> {
            boolean breakOut = false;


            while(!breakOut) {
                try {

                    sleep = Long.parseLong(String.valueOf(random.nextInt(high - low) + low));
                    System.out.println("Timer> Sleeping for " + sleep);
                    Thread.sleep(sleep);

                    if (!katch) {
                        newTimeout();
                    }

                } catch (Exception ignore) {
                    ignore.printStackTrace();
                    breakOut = true;
                }
            }
        },"Timer");
        thread.start();
    }
    
    public static void newTimeout() {
        katch = true;
        autoTimeout = new Date().getTime();
        TwitchBot.updateCatch("true", null, -1);
        System.out.println("Timer> New Catch");
    }

    public static String getWinner() {
        return winner;
    }

    public static String setWinner(String name) {
        if(katch) {
            long now = new Date().getTime();
            katch = false;
            long diff = now - autoTimeout;
            StringBuilder message = new StringBuilder();
            String m = TwitchBot.getConfig().getProperty(Strings.CONFIG_CATCH_WINNER_MESSAGE);
            message.append(m.replaceAll("%name%", name));
            winner = name;
            TwitchBot.updateCatch("false", name, diff);
            System.out.println(message.toString());
            return message.toString();
        } else {
            return null;
        }
    }
}
