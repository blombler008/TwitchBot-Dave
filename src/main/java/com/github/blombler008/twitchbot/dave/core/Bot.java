package com.github.blombler008.twitchbot.dave.core;/*
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

import com.github.blombler008.twitchbot.dave.core.sockets.SocketReader;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketWriter;

import java.net.Socket;

public class Bot {

    private ImplBot instance;

    private String oAuth;
    private String nickname;
    private String channel;

    private SocketWriter writer;
    private SocketReader reader;

    private Bot(){}

    public Bot(ImplBot bot) {
        if(bot != null) {
            this.oAuth = bot.getPassword();
            this.nickname = bot.getNickname();
            this.channel = bot.getChannel();
        } else {
            throw new NullPointerException("Bot is no logged in");
        }
    }

    public boolean initializeSockets() {

        try {
            Socket s =  new Socket();

            reader = new SocketReader();
            writer = new SocketWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public boolean login() {

        return false;
    }
}
