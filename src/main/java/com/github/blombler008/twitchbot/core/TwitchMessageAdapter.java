package com.github.blombler008.twitchbot.core;/*
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

import static com.github.blombler008.twitchbot.core.Strings.*;

public class TwitchMessageAdapter {

    public static String pass(String oAuth) {
        return IRC_PASS_TEMPLATE.replaceAll("%oAuth%", oAuth);
    }

    public static String nick(String nickname) {
        return IRC_NICK_TEMPLATE.replaceAll("%name%", nickname);
    }


    public static String join(String channel) {
        return IRC_JOIN_TEMPLATE.replaceAll("%channel%", channel);
    }

    public static String sendMessage(String channel, String message) {
        return MSG_TEMPLATE.replaceAll("%channel%", channel).replaceAll("%message%", message);
    }

    public static String sendWhisperMessage(String channel, String message, String user) {
        return MSG_WHISPLER_TEMPLATE.replaceAll("%channel%", channel).replaceAll("%message%", message).replaceAll("%name%", user);
    }
}
