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
import com.github.blombler008.twitchbot.dave.main.Load;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandSetPoints;
import com.github.blombler008.twitchbot.dave.main.configs.CatchConfig;
import com.github.blombler008.twitchbot.dave.main.katch.CatchManager;
import com.github.blombler008.twitchbot.dave.main.katch.JSONFile;
import com.github.blombler008.twitchbot.dave.main.katch.TimerCatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandCatch extends Command {

    private final String command = "catch";
    private final CatchConfig config;
    private final CatchManager manager;
    private final TwitchIRCListener twitch = getTwitch();

    public CommandCatch() {
        this.config = Load.IMP.getCatchConfig();
        this.manager = config.getManager();
    }


    @Override
    public void run(String[] message, UserInfo userInfo, String channel, String msgString) throws RuntimeException {
        SocketIO.executeAsLong(() -> {

            String username = userInfo.getDisplayName();

            String winner;
            if(message.length > 1) {
                winner = message[1];
            } else {
                winner = username;
            }

            if(config.isEnable()) {


                if(config.isPoolEnabled()) {
                    if(manager.getTimer().isCatch()) {

                        if (manager.getCooldown() == null) {
                            manager.setCooldown(config.getPoolTime() + config.getPoolDelay());
                            manager.startCatch();;
                            manager.closePool();
                        }
                        if(manager.isPoolOpen()) {
                            manager.getUserList().add(userInfo);
                            System.out.println("Added " + username + " to pool");
                            if(config.isPoolMessageOngoingEnabled()) {
                                twitch.sendMessage(config.getPoolMessageOngoing(username));
                            }
                        }

                    } else {
                        twitch.sendMessage(config.getPoolMessageClosed(username));
                    }


                } else {
                    if ((!config.isWinnerRepeatEnable()) && manager.getTimer().isCatch()) {
                        twitch.sendMessage(config.getWinnerRepeatMessage(winner));
                        return;
                    }
                    if (manager.getTimer().setWinner(winner)) {
                        twitch.sendMessage(config.getWinnerMessage(winner));
                        manager.getTimer().endCatch();
                        if(config.isWinnerRewardEnable()) {
                            twitch.sendMessage(config.getWinnerRewardCommand(winner));
                        }
                    } else {
                        if (config.isMissedEnable()) {
                            long lastWinnerTime = manager.getTimer().getLastWinnerTime();
                            long missedTime = config.getMissedTime();
                            long timeDiff = (lastWinnerTime + missedTime);
                            long systemTime = System.currentTimeMillis();
                            if ( timeDiff > systemTime) {
                                twitch.sendMessage(config.getMissedMessage(winner, manager.getTimer().getLastWinner()));
                                return;
                            }
                        }
                        twitch.sendMessage(config.getNo(username));
                    }
                }

            }

        },"Catch-Command-Executor");

    }


    @Override
    public String getCommand() {
        return command;
    }

    public CatchConfig getConfig() {
        return config;
    }
}
