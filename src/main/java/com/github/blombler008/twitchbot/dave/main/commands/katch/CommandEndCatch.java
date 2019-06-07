package com.github.blombler008.twitchbot.dave.main.commands.katch;/*
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
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;

public class CommandEndCatch extends Command {

    private final String cmd = "endcatch";
    private final CommandCatch commandCatch;
    private final TwitchIRCListener twitch;

    public CommandEndCatch(TwitchIRCListener twitch, CommandCatch commandCatch) {
        super(twitch);
        this.commandCatch = commandCatch;
        this.twitch = twitch;

    }
    @Override
    public void run(String[] message, UserInfo info, String channel, String msgString) throws RuntimeException {

        if(info.isBroadcaster() || info.isModerator()) {
            if(commandCatch.getTimer().isCatch()) {
                commandCatch.getTimer().endCatch();
                twitch.sendMessage("The catch where forced to end!");
            } else {
                twitch.sendMessage(commandCatch.getConfig().getNo(info.getDisplayName()));
            }
        }
    }

    @Override
    public String toString() {
        return null;
    }
}
