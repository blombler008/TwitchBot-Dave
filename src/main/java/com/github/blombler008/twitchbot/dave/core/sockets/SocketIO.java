package com.github.blombler008.twitchbot.dave.core.sockets;/*
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

import java.io.*;
import java.net.Socket;

public class SocketIO {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    @Deprecated
    private SocketIO(){
        inputStream = null;
        socket = null;
        outputStream = null;
    }

    public SocketIO(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public static void executeAsLong(Runnable runnable, String namex) {
        new Thread(runnable, namex).start();
    }

    public PrintWriter getSocketOutputWriter() {
        return new PrintWriter(outputStream);
    }

    public BufferedReader getSocketInputReader() {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public InputStream getSocketInput() {
        return inputStream;
    }

    public OutputStream getSocketOutput() {
        return outputStream;
    }

    public Socket getSocket() {
        return socket;
    }
}

