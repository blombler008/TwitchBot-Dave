package com.github.blombler008.twitchbot.commands;/*
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
import com.github.blombler008.twitchbot.TwitchBot;
import com.github.blombler008.twitchbot.threads.TwitchIRCListener;

import java.io.IOException;
import java.util.Date;

public class CommandCatch extends Command{

    public CommandCatch() {
        super("catch");
    }
    int afterCatchTime = Integer.parseInt(TwitchBot.getConfig().getProperty(Strings.CONFIG_AFTER_CATCH_TIME));
    boolean afterCatchEnable = Boolean.parseBoolean(TwitchBot.getConfig().getProperty(Strings.CONFIG_AFTER_CATCH_ENABLE));
    boolean afterCatchChatCommandEnable = Boolean.parseBoolean(TwitchBot.getConfig().getProperty(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND_ENABLE));
    boolean repeat = Boolean.parseBoolean(TwitchBot.getConfig().getProperty(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER));
    private long last;
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String run(String[] got, TwitchIRCListener instance) throws IOException {
        String[] subGot = got[0].split(";");
        String name = subGot[3].split("=")[1];

        String string = Strings.MSG_TEMPLATE;
        string = string.replaceAll("%channel%", got[3]);

        String byTimeoutUser = null;
        if(Timeout.byTimeout()) {
            if(Timeout.getWinner().equals(name)) {
                if(repeat) {
                    byTimeoutUser = Timeout.setWinner(name);
                } else {
                    String mss = TwitchBot.getConfig().getProperty(Strings.CONFIG_CATCH_REPEAT_SAME_WINNER_MESSAGE);
                    instance.send(string.replaceAll("%message%", mss.replaceAll("%name%", Timeout.getWinner())));
                    return null;
                }
            } else {
                byTimeoutUser = Timeout.setWinner(name);
            }
        }

        if (byTimeoutUser != null) {
            stringBuilder.append(byTimeoutUser);
            last = new Date().getTime();
            instance.send(string.replaceAll("%message%", stringBuilder.toString()));

            if(afterCatchChatCommandEnable) {
                String m = TwitchBot.getConfig().getProperty(Strings.CONFIG_AFTER_CATCH_CHAT_COMMAND);
                instance.send(string.replaceAll("%message%", m.replaceAll("%name%", Timeout.getWinner())));
            }
            return null;
        } else {

            if (afterCatchEnable) {
                if ((last + afterCatchTime) > new Date().getTime()) {

                    String m = TwitchBot.getConfig().getProperty(Strings.CONFIG_AFTER_CATCH_MESSAGE);
                    stringBuilder.append(m.replaceAll("%name%", Timeout.getWinner()));
                } else {
                    stringBuilder.append(TwitchBot.getConfig().getProperty(Strings.CONFIG_NO_CATCH));
                }
            }
        }
        instance.send(string.replaceAll("%message%", stringBuilder.toString()));
        return null;
    }
}
