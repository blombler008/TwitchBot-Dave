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

import com.github.blombler008.twitchbot.threads.TwitchIRCListener;

import java.io.IOException;
import java.util.Random;

public class CommandZoggos extends Command {

    public CommandZoggos() {
        super("zoggos");
    }

    @Override
    public String run(String[] args, TwitchIRCListener instance) {
        StringBuilder stringBuilder = new StringBuilder();
        String [] subGot = args[0].split(";");
        String name = subGot[3].split("=")[1];
        stringBuilder.append(name);
        stringBuilder.append(" ");
        stringBuilder.append("has ");
        stringBuilder.append(new Random().nextInt(Integer.MAX_VALUE));
        stringBuilder.append(" binary11Zoggos");
        return stringBuilder.toString();
    }
}
