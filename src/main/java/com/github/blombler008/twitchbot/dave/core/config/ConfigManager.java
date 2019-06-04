package com.github.blombler008.twitchbot.dave.core.config;/*
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

import com.github.blombler008.twitchbot.dave.core.StringUtils;
import com.github.blombler008.twitchbot.dave.core.Validator;

import java.io.File;

import static com.github.blombler008.twitchbot.dave.core.Strings.CONFIG_DEFAULT_PATH;
import static com.github.blombler008.twitchbot.dave.core.Strings.CONFIG_FILE;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored"})
public class ConfigManager {
    private File workDirectory;
    private File config;

    public ConfigManager(String[] args) {

        String path = CONFIG_DEFAULT_PATH;

        if (StringUtils.hasKey(args, "workDir")) {
            String value = StringUtils.getValueOf(args, "workDir");
            if (value != null) {
                path = (Validator.isValidPath(value)) ? value : path;
            }
        }
        this.workDirectory = new File(path);
        if (this.workDirectory.isFile()) {
            this.workDirectory.delete();
            this.workDirectory.mkdirs();
        }
        if (!this.workDirectory.exists()) {
            this.workDirectory.mkdirs();
        }
        this.config = new File(workDirectory, CONFIG_FILE);

    }

    public YamlConfiguration getConfig() {
        FileConfiguration fileConfig = new FileConfiguration(config);
        if (!fileConfig.isExistsBefore()) {
            fileConfig.copy();
        }
        return new YamlConfiguration(fileConfig, workDirectory);
    }

    public File getFolder() {
        return workDirectory;
    }
}
