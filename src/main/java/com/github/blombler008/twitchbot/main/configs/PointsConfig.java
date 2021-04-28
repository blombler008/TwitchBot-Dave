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

public class PointsConfig {

    private final YamlConfiguration config;
    private String pointsCommand;
    private boolean useNames;
    private MySQLConfig mysqlConfig;

    public PointsConfig(YamlConfiguration config, MySQLConfig mysqlConfig) {
        this.config = config;
        this.mysqlConfig = mysqlConfig;
    }

    public boolean gen() {
        try {
            pointsCommand = config.getString(Strings.CONFIG_POINTS_COMMAND);
            useNames = config.getBoolean(Strings.CONFIG_POINTS_USE_NAMES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCommand() {
        return pointsCommand;
    }

    public MySQLConfig getMySQL() {
        return mysqlConfig;
    }
}
