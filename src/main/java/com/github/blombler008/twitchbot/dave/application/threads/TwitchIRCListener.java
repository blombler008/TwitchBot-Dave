package com.github.blombler008.twitchbot.dave.application.threads;/*
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

import com.github.blombler008.twitchbot.dave.application.UserInfo;
import com.github.blombler008.twitchbot.dave.application.commands.Command;
import com.github.blombler008.twitchbot.dave.application.commands.CommandType;
import com.github.blombler008.twitchbot.dave.core.Bot;
import com.github.blombler008.twitchbot.dave.core.StringUtils;
import com.github.blombler008.twitchbot.dave.core.TwitchMessageAdapter;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketReader;
import com.github.blombler008.twitchbot.dave.core.sockets.SocketWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class TwitchIRCListener {

    private final Bot bot;
    private final SocketWriter writer;
    private final SocketReader reader;
    private final List<CommandType> commands;
    private String channel;
    private String prefix;

    public TwitchIRCListener(Bot bot) {
        this.bot = bot;
        this.writer = bot.getWriter();
        this.reader = bot.getReader();
        this.commands = new ArrayList<>();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void set() {
        reader.setCallback(thread -> {
            SocketReader reader = (SocketReader) thread;
//            System.out.println(reader.line);

            String [] response = reader.line.split(STRING_REGEX_SEPARATOR);
            if(StringUtils.isNumber(response[1])) {
                String responseCode = response[1];

                if (Integer.parseInt(responseCode) == 376) {
                    bot.setLoggedin(true);
                    System.out.println("Connected to Twitch IRC");
                    capReq();
                }
                return;
            }

            if(response.length > 4) {
                String channel = response[3];
                String mode = response[2];

                response[4] = response[4].replaceFirst(":", "");

                List<String> msg = new ArrayList<>();
                StringBuilder msgString = new StringBuilder();

                for (int i = 4; i < response.length; i++) {
                    msg.add(response[i]);
                    msgString.append(response[i]);
                    if(i != response.length-1) msgString.append(" ");
                }

                String[] message = msg.toArray(new String[]{});


                UserInfo userInfo = null;
                if(!mode.equals("*")) {

                    if(mode.equalsIgnoreCase(CommandType.TYPE_NOTICE)) {
                        System.out.println(reader.line);
                    }
                    if(mode.equalsIgnoreCase(CommandType.TYPE_WHISPER)) {
                        userInfo = new UserInfo(response[0], true);
                    }

                    if(mode.equalsIgnoreCase(CommandType.TYPE_PRIVMSG)) {
                        userInfo = new UserInfo(response[0], false);
                    }
                }


                if(userInfo != null) {
                    for (CommandType type : commands) {
                        if (msg.get(0).equalsIgnoreCase(prefix + type.getName())) {
                            type.execute(message, userInfo, channel, msgString.toString());
                        }
                    }
                    System.out.println(userInfo.getDisplayName() + " @ " + userInfo.isWhisper() + ": " + msgString.toString());
                }

            }


        });
    }

    public void addCommand(CommandType type) {
        commands.add(type);
    }

    private void sendWhisperMessage(String message, String name) {
        writer.send(TwitchMessageAdapter.sendWhisperMessage(channel, message, name));
    }


    public void capReq() {
        writer.send(IRC_CAP_COMMANDS);
        writer.send(IRC_CAP_MEMBERSHIP);
        writer.send(IRC_CAP_TAGS);
        writer.send(IRC_CAP_CHAT_COMMANDS);
    }

    public void joinChannel(String channel) {
        if(!channel.startsWith("#"))
            channel = '#' + channel;

        this.channel = channel;

        writer.send(TwitchMessageAdapter.join(channel));
    }


    public void sendMessage(String message) {
        writer.send(TwitchMessageAdapter.sendMessage(channel, message));
    }

}
