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

import com.github.blombler008.twitchbot.dave.application.commands.WebCommand;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketIO;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketReader;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketThread;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketWriter;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebServe {

    private static final List<WebServe> webServeList = new ArrayList<>();
    private SocketWriter writer;
    private SocketReader reader;
    private final List<WebCommand> commands;
    private final Socket socket;
    private final SocketIO socketIO;
    private SocketThread.Callback cc;

    public WebServe(Socket socket, String writerName, String readerName) throws IOException {
        this.commands = new ArrayList<>();
        this.socket = socket;
        socketIO = new SocketIO(socket);
        writer = new SocketWriter(socketIO, writerName);
        reader = new SocketReader(socketIO, readerName);
    }

    public void set() {
        StringBuilder output = new StringBuilder();
        reader.setCallback((thread, line) -> {
            String url = WebMessageAdapter.getRequestURL(line);
            String toSend = "{}";
            if(Validator.isNotNull(url)) System.out.println(url);
            if(WebMessageAdapter.isEndOfCall(line)) {
                output.append(Strings.HTML_HTTP_11_200_OK);
                output.append(Strings.HTML_CONNECTION_CLOSE);
                output.append(Strings.HTML_CONTENT_APPLICATION_JSON);
                output.append(Strings.HTML_CONTENT_LENGTH);
                output.append(toSend.length());
                output.append(Strings.HTML_END_OF_HEADERS);
                output.append(toSend);
                send(output.toString());

                reader.interrupt();
                writer.interrupt();
                socket.close();
                remove();
            }
        });
        reader.start();
    }

    public void send(String str) throws IOException {
        OutputStreamWriter pWriter = new OutputStreamWriter(socketIO.getSocketOutput());
        pWriter.write(str);
        pWriter.flush();
    }

    public static void add(WebServe webServe) {
        webServeList.add(webServe);
    }

    public void remove() {
        webServeList.remove(this);
    }

}
