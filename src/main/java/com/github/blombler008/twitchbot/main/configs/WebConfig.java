package com.github.blombler008.twitchbot.main.configs;/*
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

import com.github.blombler008.twitchbot.core.Strings;
import com.github.blombler008.twitchbot.core.config.YamlConfiguration;

public class WebConfig {
    private YamlConfiguration config;
    private int port;
    private String server;

    public WebConfig(YamlConfiguration config) {
        this.config = config;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public boolean gen() {
        try {
            this.port = config.getInteger(Strings.CONFIG_WEB_PORT);
            this.server = config.getString(Strings.CONFIG_WEB_SERVER);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
