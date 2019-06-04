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

public class ImplBot {

    private String server;
    private Integer port;

    private ImplBot() {
    }

    public Integer getPort() {
        try {
            return port;
        } catch (Exception e) {
            return null;
        }
    }

    private void setPort(int port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    private void setServer(String server) {
        this.server = server;
    }

    public ImplBot login() {
        boolean fail = false;

        if (getPort() == null) {
            fail = true;
        }

        if (getServer() == null) {
            fail = true;
        }

        if (fail) {
            return null;
        }
        return this;
    }

    public static class Builder {

        private ImplBot instance;

        public Builder() {
            instance = new ImplBot();
        }

        public Builder setServer(String server) {
            instance.setServer(server);
            return this;
        }

        public Builder setPort(int port) {
            instance.setPort(port);
            return this;
        }

        public Bot build() {
            return new Bot(instance.login());
        }
    }

}
