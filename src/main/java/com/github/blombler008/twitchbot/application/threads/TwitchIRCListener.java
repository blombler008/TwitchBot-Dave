package com.github.blombler008.twitchbot.application.threads;/*
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

import com.github.blombler008.twitchbot.application.commands.CommandType;
import com.github.blombler008.twitchbot.core.Bot;
import com.github.blombler008.twitchbot.core.StringUtils;
import com.github.blombler008.twitchbot.core.Strings;
import com.github.blombler008.twitchbot.core.TwitchMessageAdapter;
import com.github.blombler008.twitchbot.core.sockets.SocketWriter;
import com.github.blombler008.twitchbot.application.UserInfo;
import com.github.blombler008.twitchbot.core.sockets.SocketReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitchIRCListener {

    private final Bot bot;
    private final SocketWriter writer;
    private final SocketReader reader;
    private final List<CommandType> commands;
    private String channel;
    private String prefix;
    private boolean loggedIn = false;

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
        reader.setCallback((thread, line) -> {
//            SocketReader reader = (SocketReader) thread;
//            System.out.println(reader.line);

            String[] response = line.split(Strings.STRING_REGEX_SEPARATOR);
            if (StringUtils.isNumber(response[1])) {
                String responseCode = response[1];

                if (Integer.parseInt(responseCode) == 376) {
                    System.out.println("Connected to Twitch IRC");
                    capReq();
                }
                return;
            }

            if (line.equalsIgnoreCase(Strings.IRC_PING_TEMPLATE)) {
                writer.send(Strings.IRC_PONG_TEMPLATE);
                System.out.println("> " + Strings.IRC_PING_TEMPLATE);
                System.out.println("< " + Strings.IRC_PONG_TEMPLATE);
            }

            if (response.length > 4) {
                String channel = response[3];
                String mode = response[2];

                response[4] = response[4].replaceFirst(":", "");

                List<String> msg = new ArrayList<>();
                StringBuilder msgString = new StringBuilder();

                for (int i = 4; i < response.length; i++) {
                    msg.add(response[i]);
                    msgString.append(response[i]);
                    if (i != response.length - 1)
                        msgString.append(" ");
                }

                String[] message = msg.toArray(new String[]{});


                UserInfo userInfo = null;
                if (!mode.equals("*")) {

                    if (mode.equalsIgnoreCase(CommandType.TYPE_NOTICE)) {
                        System.out.println(line);
                    }
                    if (mode.equalsIgnoreCase(CommandType.TYPE_WHISPER)) {
                        userInfo = new UserInfo(response[0], true);
                    }

                    if (mode.equalsIgnoreCase(CommandType.TYPE_PRIVMSG)) {
                        userInfo = new UserInfo(response[0], false);
                    }
                }


                if (userInfo != null) {
                    for (CommandType type : commands) {
                        if (msg.get(0).equalsIgnoreCase(prefix + type.getCommandName())) {
                            type.execute(message, userInfo, channel, msgString.toString());
                        }
                    }
                    System.out.println(userInfo.getDisplayName() + " @ [" + userInfo.isWhisper() + ", " + userInfo.getUserId() + "]: " + msgString.toString());
                }

            }


        });
        reader.start();
    }

    public void addCommand(CommandType type) {
        commands.add(type);
    }

    public void sendWhisperMessage(String message, String name) {
        writer.send(TwitchMessageAdapter.sendWhisperMessage(channel, message, name));
    }


    public void capReq() {
        writer.send(Strings.IRC_CAP_COMMANDS);
        writer.send(Strings.IRC_CAP_MEMBERSHIP);
        writer.send(Strings.IRC_CAP_TAGS);
        writer.send(Strings.IRC_CAP_CHAT_COMMANDS);
    }

    public void joinChannel(String channel) {
        if (!channel.startsWith("#"))
            channel = '#' + channel;

        this.channel = channel;

        writer.send(TwitchMessageAdapter.join(channel));
    }

    @Override
    public String toString() {
        return "TwitchIRCListener{" +
                "bot=" + bot +
                ", commands=" + Arrays.toString(commands.toArray()) +
                ", channel='" + channel + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }

    public boolean login(String oAuth, String nickname) {

        try {
            writer.send(TwitchMessageAdapter.pass(oAuth));
            writer.send(TwitchMessageAdapter.nick(nickname));
            return (loggedIn = true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendMessage(String message) {
        writer.send(TwitchMessageAdapter.sendMessage(channel, message));
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
