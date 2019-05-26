package com.github.blombler008.twitchbot.dave.application;/*
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

import com.github.blombler008.twitchbot.dave.core.Bot;
import com.github.blombler008.twitchbot.dave.core.ImplBot;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;
import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;
import com.github.blombler008.twitchbot.dave.core.exceptions.AuthenticationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Load {

    private Load instance;
    private String[] args;
    private Bot bot;
    private ConfigManager configManager;
    private YamlConfiguration config;

    public Load(String[] args) {
        this.instance = this;
        this.args = args;
    }

    public static void main(String[] args) {
        Load load = new Load(args);
        load.configManager = new ConfigManager(args);
        load.finishConfigModel();
        load.createSocket();
    }

    public void finishConfigModel() {
        config = configManager.getConfig();
        String channel = config.getString("twitch.channel");
        String nickname = config.getString("twitch.nickname");
        String oAuth;
        if(!config.getBoolean("twitch.externalOAuth")) {
            oAuth = config.getString("twitch.oAuth");
        } else {
            try {
                StringBuilder password = new StringBuilder();
                File passFile = new File("password.txt");
                BufferedReader bf = new BufferedReader(new FileReader(passFile));

                String ls;
                while ((ls = bf.readLine()) != null) {
                    password.append(ls);
                }
                bf.close();

                oAuth = password.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
//            String string = Strings.IRC_PASS_TEMPLATE;
//            string = string.replaceAll("%oauth%", );
//
//            string = Strings.IRC_NICK_TEMPLATE;
//            string = string.replaceAll("%name%", "dave-bot");
        }
        bot = createBot(channel, nickname, oAuth);


    }

    public void createSocket() {
        if(bot.initializeSockets()) {
            if(!bot.login()) {
                throw new AuthenticationException("Bot login Failed!");
            }
        }
    }

    public Bot createBot(String channel, String nickname, String oAuth) {
        ImplBot.Builder builder = new ImplBot.Builder();
        builder.setChannel(channel);
        builder.setNickname(nickname);
        builder.setPassword(oAuth);
        return builder.build();
    }


}
