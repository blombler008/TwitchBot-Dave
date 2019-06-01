package com.github.blombler008.twitchbot.dave.application.commands;/*
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

public class CommandType {

    public static String TYPE_NOTICE = "NOTICE";
    public static String TYPE_WHISPER = "WHISPER";
    public static String TYPE_PRIVMSG = "PRIVMSG";
    private String type;
    private String name;
    private Command cmd;

    public CommandType(String type, String name, Command cmd) {
        this.name = name;
        this.cmd = cmd;

        if(type.equalsIgnoreCase(TYPE_NOTICE)) this.type = TYPE_NOTICE;
        else if(type.equalsIgnoreCase(TYPE_WHISPER)) this.type = TYPE_WHISPER;
        else if(type.equalsIgnoreCase(TYPE_PRIVMSG)) this.type = TYPE_PRIVMSG;
        else throw new RuntimeException("Invalid type: " + type);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void execute(String[] message, UserInfo userInfo, String channel, String messageString) {
        cmd.run(message, userInfo, channel, messageString);
    }
}
