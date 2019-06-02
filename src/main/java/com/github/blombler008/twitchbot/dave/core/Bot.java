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

import com.github.blombler008.twitchbot.dave.core.sockets.SocketIO;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketReader;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketWriter;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Bot {

    private String server;
    private int port;

    private SocketWriter writer;
    private SocketReader reader;
    private SocketIO socketIO;

    private Bot(){}

    public Bot(ImplBot bot) {
        if(bot != null) {
            this.port = bot.getPort();
            this.server = bot.getServer();
        } else {
            throw new NullPointerException(BOT_NULL_OBJECT);
        }
    }

    public boolean initializeSockets(String readerName, String writerName) {

        try {
            socketIO =  new SocketIO(new Socket(server, port));

            reader = new SocketReader(socketIO, readerName);
            writer = new SocketWriter(socketIO, writerName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public SocketReader getReader() {
        return reader;
    }

    public SocketWriter getWriter() {
        return writer;
    }

    public boolean hostSockets(String readerName, String writerName) {

        try {
            ServerSocket server = new ServerSocket(port);
            SocketIO.executeAsLong(() -> {
                WebServe web;
                while(true) {
                    try {
                        Thread.yield();
                        web = new WebServe(server.accept(), writerName, readerName);
                        WebServe.add(web);

                        web.set();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }, "WebSocketListener");


            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
