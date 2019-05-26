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

import com.sun.istack.internal.Pool;

import javax.security.auth.login.LoginException;

public class ImplBot {

    private String nickname;
    private String oAuth;
    private String channel;

    private ImplBot() {}

    private void setChannel(String channel) {
        this.channel = channel;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setPassword(String oAuth) {
        this.oAuth = oAuth;
    }


    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return oAuth;
    }

    public String getChannel() {
        return channel;
    }


    public ImplBot login() {
        boolean fail = false;

        if(getPassword() == null) {
            fail = true;
        }

        if(getChannel() == null) {
            fail = true;
        }

        if(getNickname() == null) {
            fail = true;
        }

        if(fail) {
            return null;
        }
        return this;
    }

    public static class Builder {

        private ImplBot instance;

        public Builder() {
            instance = new ImplBot();
        }

        public Builder setPassword(String oAuth) {
            instance.setPassword(oAuth);
            return this;
        }

        public Builder setChannel(String channel) {
            instance.setChannel(channel);
            return this;
        }

        public Builder setNickname(String nickname) {
            instance.setNickname(nickname);
            return this;
        }

        public Bot build() {
            return new Bot(instance.login());
        }
    }

}
