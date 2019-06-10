package com.github.blombler008.twitchbot.dave.main.configs;/*
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

import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class TwitchConfig {

    private String channel;
    private String nickname;
    private String password;
    private String prefix;
    private YamlConfiguration config;

    public TwitchConfig(YamlConfiguration config) {
        this.config = config;
    }

    public boolean gen() {
        try {
            channel = config.getString(CONFIG_TWITCH_CHANNEL).toLowerCase();
            nickname = config.getString(CONFIG_TWITCH_NICKNAME);
            prefix = config.getString(CONFIG_TWITCH_PREFIX);
            boolean externalOAuth = config.getBoolean(CONFIG_TWITCH_EXTERNAL_OAUTH);
            if (!externalOAuth) {
                password = config.getString(CONFIG_TWITCH_OAUTH);
            } else {
                StringBuilder passwordBuilder = new StringBuilder();
                File passFile = new File(config.getWorkingDirectory(), "password.txt");
                BufferedReader bf = new BufferedReader(new FileReader(passFile));

                String ls;
                while ((ls = bf.readLine()) != null) {
                    passwordBuilder.append(ls);
                }
                bf.close();

                password = passwordBuilder.toString();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getChannel() {
        return channel;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefix() {
        return prefix;
    }
}
