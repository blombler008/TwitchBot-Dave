package com.github.blombler008.twitchbot.dave.main;/*
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

import com.github.blombler008.twitchbot.dave.application.commands.CommandType;
import com.github.blombler008.twitchbot.dave.application.commands.WebCommand;
import com.github.blombler008.twitchbot.dave.core.Strings;
import com.github.blombler008.twitchbot.dave.main.commands.katch.*;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandAddPoints;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandPoints;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandSetPoints;
import com.github.blombler008.twitchbot.dave.main.configs.*;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.Bot;
import com.github.blombler008.twitchbot.dave.core.ImplBot;
import com.github.blombler008.twitchbot.dave.core.WebServe;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;
import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;
import com.github.blombler008.twitchbot.dave.core.exceptions.AuthenticationException;
import com.github.blombler008.twitchbot.dave.main.commands.CommandDice;
import com.github.blombler008.twitchbot.dave.main.commands.WebCommandFavicon;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class Load {

    private Load instance;
    private String[] args;
    private Bot twitchBot;
    private ConfigManager configManager;
    private YamlConfiguration config;
    private TwitchIRCListener twitch;

    private WebConfig webConfig;
    private DiceConfig configDice;
    private CatchConfig configCatch;
    private MySQLConfig configMySQL;
    private TwitchConfig twitchConfig;
    private PointsConfig configPoints;

    private List<WebCommand> webCommands;
    private List<CommandType> twitchCommands;

    private Bot webBot;
    private CommandCatch commandCatch;
    private CommandAddPoints commandAddPoints;
    private CommandSetPoints commandSetPoints;

    public Load(String[] args) {
        this.instance = this;
        webCommands = new ArrayList<>();
        twitchCommands = new ArrayList<>();
        this.args = args;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Load load = new Load(args);
        load.setConfigModel();
        load.createSocketTwitch();
        load.createSocketWeb();
        load.join();
        load.createConfigTwitch();
        load.createConfigMySQL();
        load.createConfigWeb();

        load.registerCommands();

        load.startCatch();
    }

    public void createConfigMySQL() {
        if(configMySQL.connect()) {
            try {
                PreparedStatement statement = configMySQL.getConnection().prepareStatement(MYSQL_DATABASE_CREATE.replaceAll("%database%", configMySQL.getDatabase()));
                int code = statement.executeUpdate();
                statement.close();

                statement = configMySQL.getConnection().prepareStatement(MYSQL_DATABASE_USE.replaceAll("%database%", configMySQL.getDatabase()));
                code = statement.executeUpdate();
                statement.close();

                statement = configMySQL.getConnection().prepareStatement(MYSQL_TABLE_POINTS_CREATE.replaceAll("%database%", configMySQL.getDatabase()));
                code = statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void startCatch() {
        JSONFile.setFile(new File(config.getWorkingDirectory(), "json/index.json"));
        commandCatch.newCatch();
    }

    public void createConfigTwitch() {
        commandSetPoints = new CommandSetPoints(twitch, configPoints);
        commandAddPoints = new CommandAddPoints(twitch, configPoints, commandSetPoints);
        commandCatch = new CommandCatch(twitch, configCatch, commandSetPoints);

        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "dice", new CommandDice(twitch, configDice)));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "catch", commandCatch));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "newcatch", new CommandNewCatch(twitch, commandCatch)));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "endcatch", new CommandEndCatch(twitch, commandCatch)));

        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "setpoints",commandSetPoints));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, "addpoints", commandAddPoints));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, configPoints.getCommand(), new CommandPoints(twitch, configPoints, commandSetPoints)));
    }

    public void createConfigWeb() {
        webCommands.add(new WebCommandJson(twitch, configManager));
        webCommands.add(new WebCommandFavicon(twitch, configManager));
        webCommands.add(new WebCommandCatch(twitch, configManager, configCatch));
    }

    public void createSocketWeb() {
        if (webConfig.gen()) {
            webBot = createBot(webConfig.getServer(), webConfig.getPort());
            webBot.hostSockets("Web-Reader", "Web-Writer");
        }
    }

    public void registerCommands() {
        for(WebCommand cmd: webCommands) {
            WebServe.addCommand(cmd);
        }
        for(CommandType cmd: twitchCommands) {
            twitch.addCommand(cmd);
        }
    }

    public void setConfigModel() {
        configManager = new ConfigManager(args);

        config = configManager.getConfig();

        twitchConfig = new TwitchConfig(config);
        webConfig = new WebConfig(config);

        configDice = new DiceConfig(config);
        configCatch = new CatchConfig(config);

        configMySQL = new MySQLConfig(config);

        configPoints = new PointsConfig(config, configMySQL);

        configPoints.gen();
        configMySQL.gen();
        configDice.gen();
        configCatch.gen();
    }

    public void join() {
        twitch.joinChannel(twitchConfig.getChannel());
//        twitch.joinChannel("binarydave");
//        twitch.sendMessage(new Random().nextInt(100) + " - Bitte mir random stuff senden per Whisper(/w blombler008 <msg>) ... Danke ♥ ... empfehlung tattyplay ♥");
    }

    public void createSocketTwitch() {

        if (twitchConfig.gen()) {
            twitchBot = createBot(TWITCH_SERVER, Integer.parseInt(TWITCH_PORT));

            if (twitchBot.initializeSockets("Twitch-Reader", "Twitch-Writer")) {
                twitch = new TwitchIRCListener(twitchBot);
                twitch.setPrefix(twitchConfig.getPrefix());
                if (!twitch.login(twitchConfig.getPassword(), twitchConfig.getNickname())) {
                    throw new AuthenticationException("Bot login Failed!");
                } else {
                    twitch.set();
                }
            }
        }
    }

    public Bot createBot(String nickname, int port) {
        ImplBot.Builder builder = new ImplBot.Builder();
        builder.setServer(nickname);
        builder.setPort(port);
        return builder.build();
    }

}
