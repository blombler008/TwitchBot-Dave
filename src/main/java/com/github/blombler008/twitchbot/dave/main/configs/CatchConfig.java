package com.github.blombler008.twitchbot.dave.main.configs;/*
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

import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;

import java.util.Random;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class CatchConfig {

    private YamlConfiguration config;

    private String no;
    private String blendImage;
    private String missedMessage;
    private String winnerMessage;
    private String winnerRewardCommand;
    private String winnerRepeatMessage;
    private String poolMessageClosed;
    private String poolMessageOngoing;

    private boolean enable;
    private boolean missedEnable;
    private boolean winnerRewardEnable;
    private boolean winnerRepeatEnable;
    private boolean poolEnable;
    private boolean poolMessageOngoingEnabled;

    private int missedTime;
    private int timerMin;
    private int timerMax;
    private int winnerRewardMin;
    private int winnerRewardMax;
    private int poolTime;
    private int poolDelay;

    public CatchConfig(YamlConfiguration config) {
        this.config = config;
    }

    public void gen() {

        enable = config.getBoolean(CONFIG_CATCH_ENABLE);

        if(enable) {
            winnerMessage = config.getString(CONFIG_CATCH_WINNER_MESSAGE);
            blendImage = config.getString(CONFIG_CATCH_IMAGE_LOCATION);
            timerMin = config.getInteger(CONFIG_CATCH_TIMER_MIN);
            timerMax = config.getInteger(CONFIG_CATCH_TIMER_MAX);
            no = config.getString(CONFIG_CATCH_NO);

            poolEnable = config.getBoolean(CONFIG_CATCH_POOL_ENABLE);
            poolTime = config.getInteger(CONFIG_CATCH_POOL_TIME);
            poolDelay = config.getInteger(CONFIG_CATCH_POOL_DELAY);

            poolMessageClosed = config.getString(CONFIG_CATCH_POOL_MESSAGE_CLOSED);
            poolMessageOngoing = config.getString(CONFIG_CATCH_POOL_MESSAGE_ONGOING);
            poolMessageOngoingEnabled = config.getBoolean(CONFIG_CATCH_POOL_MESSAGE_ONGOING_ENABLE);

            missedEnable = config.getBoolean(CONFIG_CATCH_MISSED_ENABLE);
            missedMessage = config.getString(CONFIG_CATCH_MISSED_MESSAGE);
            missedTime = config.getInteger(CONFIG_CATCH_MISSED_TIME);


            winnerRepeatEnable = config.getBoolean(CONFIG_CATCH_WINNER_REPEAT_ENABLE);
            winnerRepeatMessage = config.getString(CONFIG_CATCH_WINNER_REPEAT_MESSAGE);


            winnerRewardEnable = config.getBoolean(CONFIG_CATCH_WINNER_REWARD_ENABLE);
            winnerRewardCommand = config.getString(CONFIG_CATCH_WINNER_REWARD_COMMAND);
            winnerRewardMin = config.getInteger(CONFIG_CATCH_WINNER_REWARD_MIN);
            winnerRewardMax = config.getInteger(CONFIG_CATCH_WINNER_REWARD_MAX);

        }
    }

    @Override
    public String toString() {
        return "CatchConfig{" +
                "config=" + config +
                ", no='" + no + '\'' +
                ", blendImage='" + blendImage + '\'' +
                ", missedMessage='" + missedMessage + '\'' +
                ", winnerMessage='" + winnerMessage + '\'' +
                ", winnerRewardCommand='" + winnerRewardCommand + '\'' +
                ", winnerRepeatMessage='" + winnerRepeatMessage + '\'' +
                ", enable=" + enable +
                ", missedEnable=" + missedEnable +
                ", winnerRewardEnable=" + winnerRewardEnable +
                ", winnerRepeatEnable=" + winnerRepeatEnable +
                ", missedTime=" + missedTime +
                ", timerMin=" + timerMin +
                ", timerMax=" + timerMax +
                ", winnerRewardMin=" + winnerRewardMin +
                ", winnerRewardMax=" + winnerRewardMax +
                '}';
    }

    public boolean isEnable() {
        return enable;
    }

    public String getNo(String name) {
        return no.replaceAll("%name%", name);
    }

    public String getBlendImage() {
        return blendImage;
    }

    public String getMissedMessage(String name, String winner) {
        return missedMessage.replaceAll("%name%", name).replaceAll("%winner%",winner);
    }

    public String getWinnerMessage(String name) {
        return winnerMessage.replaceAll("%name%", name);
    }

    public String getWinnerRewardCommand(String name) {
        int minMax = new Random().nextInt(getWinnerRewardMax()- getWinnerRewardMin()) + getWinnerRewardMin();
        return winnerRewardCommand.replaceAll("%name%", name).replaceAll("%min_max%", minMax + "");
    }

    public String getWinnerRepeatMessage(String name) {
        return winnerRepeatMessage.replaceAll("%name%", name);
    }

    public boolean isMissedEnable() {
        return missedEnable;
    }

    public boolean isWinnerRewardEnable() {
        return winnerRewardEnable;
    }

    public boolean isWinnerRepeatEnable() {
        return winnerRepeatEnable;
    }

    public int getMissedTime() {
        return missedTime;
    }

    public int getTimerMin() {
        return timerMin;
    }

    public int getTimerMax() {
        return timerMax;
    }

    public int getWinnerRewardMin() {
        return winnerRewardMin;
    }

    public int getWinnerRewardMax() {
        return winnerRewardMax;
    }

    public boolean isPoolEnabled() {
        return poolEnable;
    }

    public long getPoolTime() {
        return poolTime;
    }

    public long getPoolDelay() {
        return poolDelay;

    }

    public boolean isPoolMessageOngoingEnabled() {
        return poolMessageOngoingEnabled;
    }

    public String getPoolMessageClosed(String name) {
        return poolMessageClosed.replaceAll("%name%", name);
    }

    public String getPoolMessageOngoing(String name) {
        return poolMessageOngoing.replaceAll("%name%", name);
    }
}
