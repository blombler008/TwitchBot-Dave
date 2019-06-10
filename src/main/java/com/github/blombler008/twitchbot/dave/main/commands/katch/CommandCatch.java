package com.github.blombler008.twitchbot.dave.main.commands.katch;/*
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

import com.github.blombler008.twitchbot.dave.application.UserInfo;
import com.github.blombler008.twitchbot.dave.application.commands.Command;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketIO;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandAddPoints;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandSetPoints;
import com.github.blombler008.twitchbot.dave.main.configs.CatchConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandCatch extends Command {

    private final String cmd = "catch";
    private final CatchConfig config;
    private final TwitchIRCListener twitch;
    private List<UserInfo> list = new ArrayList<>();
    private TimerCatch timer;
    private Long cooldown;
    private boolean ispoolopen = false;
    private CommandSetPoints commandSetPoints;

    public CommandCatch(TwitchIRCListener twitch, CatchConfig config, CommandSetPoints commandSetPoints) {
        super(twitch);
        this.commandSetPoints = commandSetPoints;
        this.config = config;
        this.twitch = twitch;
        timer = new TimerCatch(config, this);
        SocketIO.executeAsLong(() -> timer.set(),"Timer-Waiter");
    }
    @Override
    public void run(String[] message, UserInfo info, String channel, String msgString) throws RuntimeException {
        SocketIO.executeAsLong(() -> {

            String username = info.getDisplayName();

            String winner;
            if(message.length > 1) {
                winner = message[1];
            } else {
                winner = username;
            }

            if(config.isEnable()) {


                if(config.isPoolEnabled()) {
                    if(timer.isCatch()) {

                        if (cooldown == null) {
                            cooldown = (config.getPoolTime() + config.getPoolDelay());
                            newCatch();
                            ispoolopen = true;
                        }
                        if(ispoolopen) {
                            list.add(info);
                            System.out.println("Added " + username + " to pool");
                            if(config.isPoolMessageOngoingEnabled()) {
                                twitch.sendMessage(config.getPoolMessageOngoing(username));
                            }
                        }

                    } else {
                        twitch.sendMessage(config.getPoolMessageClosed(username));
                    }


                } else {
                    if ((!config.isWinnerRepeatEnable()) && timer.isCatch()) {
                        twitch.sendMessage(config.getWinnerRepeatMessage(winner));
                        return;
                    }
                    if (timer.setWinner(winner)) {
                        twitch.sendMessage(config.getWinnerMessage(winner));
                        timer.endCatch();
                        if(config.isWinnerRewardEnable()) {
                            twitch.sendMessage(config.getWinnerRewardCommand(winner));
                        }
                    } else {
                        if (config.isMissedEnable()) {
                            long lastWinnerTime = timer.getLastWinnerTime();
                            long missedTime = config.getMissedTime();
                            long timeDiff = (lastWinnerTime + missedTime);
                            long systemTime = System.currentTimeMillis();
                            if ( timeDiff > systemTime) {
                                twitch.sendMessage(config.getMissedMessage(winner, timer.getLastWinner()));
                                return;
                            }
                        }
                        twitch.sendMessage(config.getNo(username));
                    }
                }

            }

        },"Catch-Command-Executor");

    }

    public void newCatch() {
        ispoolopen = true;
        cooldown = (config.getPoolTime() + config.getPoolDelay());
        timer.newCatch();
        timer.startPool(() -> {

            if(list.isEmpty()) {
                System.out.println("Pool is empty> waiting again!");
                newCatch();
                return;
            }

            UserInfo[] users = list.toArray(new UserInfo[]{});

            int random = new Random().nextInt(users.length);

            String winner = users[random].getDisplayName();

            timer.setWinner(winner);
            list.clear();
            twitch.sendMessage(config.getWinnerMessage(winner));
            JSONFile.hide();

            String end = config.getWinnerRewardCommand(winner);
            if(config.isWinnerRewardEnable()) {
                twitch.sendMessage(end);
            } else {
                commandSetPoints.add(end, winner);
            }
            System.out.println(" - " + end);
            System.out.println(" - " + winner + " won the pool catch!");
            ispoolopen = false;
        }, cooldown);
    }

    public TimerCatch getTimer() {
        return timer;
    }
    @Override
    public String toString() {
        return "CommandCatch{" +
                "cmd='" + cmd + '\'' +
                ", config=" + config +
                ", twitch=" + twitch +
                ", timer=" + timer +
                '}';
    }

    public CatchConfig getConfig() {
        return config;
    }
}
