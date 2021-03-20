package com.github.blombler008.twitchbot.dave.main.commands;/*
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
import com.github.blombler008.twitchbot.dave.main.Load;
import com.github.blombler008.twitchbot.dave.main.configs.DiceConfig;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;

import java.util.Random;

public class CommandDice extends Command {

    private final String command = "dice";
    private DiceConfig config;


    public CommandDice() {
        this.config = Load.IMP.getDiceConfig();
    }

    @Override
    public void run(String[] message, UserInfo info, String channel, String fullMessage) throws RuntimeException {
        getTwitch().sendMessage("@" + info.getDisplayName() + " rolled a " + Math.abs(new Random().nextInt(config.getMaxBound())));
    }


    @Override
    public String getCommand() {
        return command;
    }
}
