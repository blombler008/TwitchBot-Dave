package com.github.blombler008.twitchbot.commands;/*
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

import com.github.blombler008.twitchbot.Strings;
import com.github.blombler008.twitchbot.Timeout;
import com.github.blombler008.twitchbot.TwitchBot;
import com.github.blombler008.twitchbot.threads.TwitchIRCListener;

import java.io.IOException;
import java.util.Arrays;

public class CommandNewCatch extends Command {

    public CommandNewCatch() {
        super("newcatch");
    }

    @Override
    public String run(String[] args, TwitchIRCListener instance) throws IOException {
        String[] subGot = args[0].split(";");
        subGot[1] = subGot[1].replaceAll("/1", "");

        String[] baggies = subGot[1].split(",");
        String name = subGot[3].split("=")[1];
        System.out.println("args: " + Arrays.toString(args));
        System.out.println("userInfo: " + Arrays.toString(subGot));
        System.out.println("baggies: " + Arrays.toString(baggies));
        System.out.println("name: " + name);
        if(baggies[0].startsWith("badges=")) {
            baggies[0] = baggies[0].replaceFirst("badges=", "");
        }
        System.out.println("baggies: " + Arrays.toString(baggies));
        boolean newCatch = false;
        for (String s: baggies) {
            if(s.equalsIgnoreCase("broadcaster")) {
                newCatch = true;
            }
            if(s.equalsIgnoreCase("global_mod")) {
                newCatch = true;
            }
            if(s.equalsIgnoreCase("moderator")) {
                newCatch = true;
            }
            if(s.equalsIgnoreCase("staff")) {
                newCatch = true;
            }
            if(s.equalsIgnoreCase("admin")) {
                newCatch = true;
            }
            if(name.equalsIgnoreCase("tattyplay")) {
                newCatch = true;
            }
        }
        if(newCatch) {
            /*
            String string = Strings.MSG_TEMPLATE;
            string = string.replaceAll("%channel%", args[3]);
            instance.send(string.replaceAll("%message%", "Works!"));
            */
            Timeout.newTimeout();
        }


        return null;
    }
}
