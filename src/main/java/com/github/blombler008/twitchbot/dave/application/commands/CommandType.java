package com.github.blombler008.twitchbot.dave.application.commands;/*
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

public class CommandType {

    public static final String TYPE_NOTICE = "NOTICE";
    public static final String TYPE_WHISPER = "WHISPER";
    public static final String TYPE_PRIVMSG = "PRIVMSG";

    private final String commandType;
    private final String commandName;
    private final Command command;


    public CommandType(String commandType, Command command) {
         this(commandType, command.getCommand(), command);
    }


    public CommandType(String commandType, String commandName, Command command) {
        this.commandName = commandName;
        this.command = command;


        switch (commandType) {
            case TYPE_NOTICE:
                this.commandType = TYPE_NOTICE;
                break;
            case TYPE_WHISPER:
                this.commandType = TYPE_WHISPER;
                break;
            case TYPE_PRIVMSG:
                this.commandType = TYPE_PRIVMSG;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + commandType);
        }
    }

    public String getCommandType() {
        return commandType;
    }

    public String getCommandName() {
        return commandName;
    }

    public void execute(String[] message, UserInfo userInfo, String channel, String messageString) {
        command.run(message, userInfo, channel, messageString);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommandType{");
        sb.append("commandType='").append(commandType).append('\'');
        sb.append(", commandName='").append(commandName).append('\'');
        sb.append(", command=").append(command);
        sb.append('}');
        return sb.toString();
    }
}
