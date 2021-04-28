package com.github.blombler008.twitchbot.main.commands.points;/*
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

import com.github.blombler008.twitchbot.core.StringUtils;
import com.github.blombler008.twitchbot.application.UserInfo;
import com.github.blombler008.twitchbot.application.commands.Command;
import com.github.blombler008.twitchbot.main.Load;
import com.github.blombler008.twitchbot.main.configs.PointsConfig;

public class CommandAddPoints extends Command {

    private final PointsConfig config;
    private final String command = "addpoints";

    public CommandAddPoints() {

        this.config = Load.IMP.getPointConfig();
    }

    public void send(String amount, String user) {
        //commandSetPoints.add(amount, user);
    }

    @Override
    public void run(String[] message, UserInfo info, String channel, String msgString) {
        if(message.length > 2) {
            if(StringUtils.isNumber(message[2])) {
                String user = message[1];
                String amount = message[2];

                send(amount, user);
            }
        } else if(message.length > 1) {
            if(StringUtils.isNumber(message[1])) {
                send(message[1], info.getDisplayName());
            }
        }
    }

    @Override
    public String getCommand() {
        return command;
    }
}
