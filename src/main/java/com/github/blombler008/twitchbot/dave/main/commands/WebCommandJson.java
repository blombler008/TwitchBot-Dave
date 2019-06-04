package com.github.blombler008.twitchbot.dave.main.commands;/*
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

import com.github.blombler008.twitchbot.dave.application.commands.WebCommand;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;

import java.io.File;
import java.io.OutputStream;

import static com.github.blombler008.twitchbot.dave.core.Strings.HTML_CONTENT_APPLICATION_JSON;

public class WebCommandJson extends WebCommand {

    private final String url = "json";
    private File configFolder;

    public WebCommandJson(TwitchIRCListener twitch, ConfigManager configManager) {
        super(twitch);
        this.configFolder = configManager.getFolder();
    }

    @Override
    public String run(OutputStream outputStream) {
        String json = "{\"abd\":\"def\"}";
        setContentType(HTML_CONTENT_APPLICATION_JSON);
        return json;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String toString() {
        return null;
    }
}
