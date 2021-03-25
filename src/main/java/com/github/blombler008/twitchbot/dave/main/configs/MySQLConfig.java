package com.github.blombler008.twitchbot.dave.main.configs;/*
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

import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class MySQLConfig {

    private String hostname;
    private String username;
    private String password;
    private String database;
    private int port;

    private boolean connected;
    private Connection connection;

    private YamlConfiguration config;


    public MySQLConfig(YamlConfiguration config) {
        this.config = config;
    }

    public boolean gen() {
        hostname = config.getString(CONFIG_MYSQL_HOSTNAME);
        boolean externalPassword = config.getBoolean(CONFIG_MYSQL_EXTERNAL_PASSWORD);

        if(externalPassword) {
            password = config.getPassword("mysql");
        } else {
            password = config.getString(CONFIG_MYSQL_PASSWORD);
        }

        username = config.getString(CONFIG_MYSQL_USERNAME);
        port = config.getInteger(CONFIG_MYSQL_PORT);
        database = config.getString(CONFIG_MYSQL_DATABASE);
        return true;
    }

    public String getHostname() {
        return hostname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return connected;
    }

    public Connection getConnection() {
        if(!connected) {
            return null;
        }
        try {
            if(connection.isClosed()) {
                connect();
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean connect() {
        if(!isConnected()) {
            try {
                AtomicReference<String> connectionString = new AtomicReference<>(MYSQL_CONNECTION_STRING);
                connectionString.set(connectionString.get().replaceAll("%hostname%", String.valueOf(getHostname())));
                connectionString.set(connectionString.get().replaceAll("%username%", String.valueOf(getUsername())));
                connectionString.set(connectionString.get().replaceAll("%password%", String.valueOf(getPassword())));
                connectionString.set(connectionString.get().replaceAll("%port%", String.valueOf(getPort())));

                connection = DriverManager.getConnection(connectionString.get());

                connected=true;
            }catch (SQLException e) {
                e.printStackTrace();
                connected=false;
            }
        }
        return isConnected();
    }
}
