package com.github.blombler008.twitchbot.main.katch;

import com.github.blombler008.twitchbot.application.UserInfo;
import com.github.blombler008.twitchbot.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.core.sockets.SocketIO;
import com.github.blombler008.twitchbot.main.ContentManager;
import com.github.blombler008.twitchbot.main.Load;
import com.github.blombler008.twitchbot.main.configs.CatchConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 *
 * MIT License
 *
 * Copyright (c) 2020 blombler008
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
 *
 * Project TwitchBot-Dave
 * Package com.github.blombler008.twitchbot.dave.main.katch
 * User lucie
 * Creation Date 11/03/2021
 * Creation Time 23:04
 */
public class CatchManager extends ContentManager {

    private final CatchConfig config;
    private TwitchIRCListener twitch;
    private List<UserInfo> userList = new ArrayList<>();
    private TimerCatch timer;
    private Long cooldown;
    private boolean poolState = false;

    // Deployed in CatchConfig::gen();
    public CatchManager() {
        this.config = Load.IMP.getCatchConfig();
        startCatch(false);
    }

    public void startCatch() {
        this.twitch = Load.IMP.getTwitch();
        startCatch(true);
    }

    private void startCatch(boolean f) {

        if(config.isEnable()) {

            if(!config.isStarted()) {
                timer = new TimerCatch(config);
                SocketIO.executeAsLong(() -> timer.set(),"Timer-Waiter");
            }
            if(twitch != null && twitch.isLoggedIn()) {
                if(f) newCatch();
            }
        }

    }


    private void newCatch() {
        poolState = true;
        cooldown = (config.getPoolTime() + config.getPoolDelay());
        timer.newCatch();
        timer.startPool(() -> {

            if(userList.isEmpty()) {
                System.out.println("Pool is empty> waiting again!");
                newCatch();
                return;
            }

            UserInfo[] users = userList.toArray(new UserInfo[]{});

            int random = new Random().nextInt(users.length);

            String winner = users[random].getDisplayName();

            timer.setWinner(winner);
            userList.clear();
            twitch.sendMessage(config.getWinnerMessage(winner));
            JSONFile.hide();

            String end = config.getWinnerRewardCommand(winner);
            if(config.isWinnerRewardEnable()) {
                twitch.sendMessage(end);
            } else {
                //commandSetPoints.add(end, winner);
            }
            System.out.println(" - " + end);
            System.out.println(" - " + winner + " won the pool catch!");
            poolState = false;
        }, cooldown);
    }

    public TimerCatch getTimer() {
        return timer;
    }


    public Long getCooldown() {
        return cooldown;
    }

    public List<UserInfo> getUserList() {
        return userList;
    }

    public boolean isPoolOpen() {
        return poolState;
    }

    public void closePool() {
        this.poolState = false;
    }

    public void setCooldown(long l) {
        this.cooldown = l;
    }
}
