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

import jdk.nashorn.internal.codegen.CompilerConstants;

public abstract class SocketThread extends Thread {

    private Callback c = thread -> {};

    public void setCallback(Callback c) {
        this.c = c;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.yield();
                runSocketAction(c);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    protected abstract void runSocketAction(Callback c) throws Exception;

    public interface Callback {
        void callback(SocketThread thread);
    }
}
