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

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.github.blombler008.twitchbot.dave.application.commands.CommandType;
import com.github.blombler008.twitchbot.dave.application.commands.WebCommand;
import com.github.blombler008.twitchbot.dave.core.config.ApplicationConfig;
import com.github.blombler008.twitchbot.dave.gui.GUI;
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
import com.github.blombler008.twitchbot.dave.main.katch.CatchManager;
import com.github.blombler008.twitchbot.dave.main.katch.JSONFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class Load {

    public static Load IMP;
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
    private ApplicationConfig applicationConfig;

    private List<WebCommand> webCommands = new ArrayList<>();
    private List<CommandType> twitchCommands = new ArrayList<>();
    private List<ContentManager> contentManagers = new ArrayList<>();

    private Bot webBot;

    private Load(String[] args) {
        this.args = args;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Load load = new Load(args);

        String[] logo = new String[] {
                "ICBfX19fX19fICAgICBfICAgICAgXyAgICAgX18gICAgIF9fX19fX18gICAgIF9fX19fICAgIF9fICAgX18gICAgICBfX19fXyAgICAgIF9fX19fICAgICAgX19fX19fXyAgIA",
                "L1xfX19fX19fKVwgIC9fL1wgIC9cX1wgICAvXF9cICAvXF9fX19fX18pXCAgL1wgX18vXCAgL1xfXCAvXy9cICAgL1wgIF9fL1wgICAgKSBfX18gKCAgIC9cX19fX19fXylcIA",
                "XChfX18gIF9fXC8gICkgKSApKCAoICggICBcL18vICBcKF9fXyAgX19cLyAgKSApX19cLyAoICggKF8pICkgKSAgKSApKF8gKSApICAvIC9cXy9cIFwgIFwoX19fICBfX1wvIA",
                "ICAvIC8gLyAgICAgL18vIC8vXFwgXF9cICAgL1xfXCAgIC8gLyAvICAgICAvIC8gLyAgICAgXCBcX19fLyAvICAvIC8gX18vIC8gIC8gL18vIChfXCBcICAgLyAvIC8gICAgIA",
                "ICggKCAoICAgICAgXCBcIC8gIFwgLyAvICAvIC8gLyAgKCAoICggICAgICBcIFwgXF8gICAgLyAvIF8gXCBcICBcIFwgIF9cIFwgIFwgXCApXy8gLyAvICAoICggKCAgICAgIA",
                "ICBcIFwgXCAgICAgIClfKSAvXCAoXyggICggKF8oICAgIFwgXCBcICAgICAgKSApX18vXCAoIChfKCApXykgKSAgKSApKF9fKSApICBcIFwvX1wvIC8gICAgXCBcIFwgICAgIA",
                "ICAvXy9fLyAgICAgIFxfXC8gIFwvXy8gICBcL18vICAgIC9fL18vICAgICAgXC9fX19cLyAgXC9fLyBcX1wvICAgXC9fX19fXC8gICAgKV9fX19fKCAgICAgL18vXy8gICAgIA"
        };

        for(String s: logo) {
            System.out.println(new String(Base64.getDecoder().decode(s)));
        }

        if(IMP == null)
            IMP = load;
        else
            return;



        load.setConfigModel();

        if(load.configManager.isGuiTest()) {
            try {
                if(load.applicationConfig.isLightTheme()) {
                    FlatLightLaf.install();
                } else {
                    FlatDarkLaf.install();
                }
                FlatInspector.install("ctrl shift alt X");
                FlatUIDefaultsInspector.install("ctrl shift alt Y");
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFrame.setDefaultLookAndFeelDecorated( true );
                JDialog.setDefaultLookAndFeelDecorated( true );
                UIManager.put( "CheckBox.icon.style", "filled" );
                UIManager.put( "Component.arrowType", "triangle" );
                UIManager.put( "TabbedPane.tabLayoutPolicy", "scroll" );
                UIManager.put( "Component.hideMnemonics", false );

            } catch (Exception ignore) {
            }

            GUI gui = new GUI(load);
            gui.setContentPane(gui.$$$getRootComponent$$$());
            gui.setSize(gui.getDimension());
            gui.setMinimumSize(gui.getMinimumDimension());
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            if(gui.apply()) {
                gui.setVisible(true);
            }
            ((JTabbedPane)gui.$$$getRootComponent$$$()).setSelectedIndex(1);
            ((JTabbedPane)gui.$$$getRootComponent$$$()).setSelectedIndex(0);
            return;
        }

        if(load.createSocketTwitch()) {
            load.createSocketWeb();
            load.join();
            load.createConfigTwitch();
            load.createConfigMySQL();
            load.createConfigWeb();

            load.registerCommands();

            load.startCatch();

        }
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
        configCatch.getManager().startCatch();
    }

    public void createConfigTwitch() {
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandDice()));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandCatch()));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandNewCatch()));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandEndCatch()));

        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandSetPoints()));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandAddPoints()));
        twitchCommands.add(new CommandType(CommandType.TYPE_PRIVMSG, new CommandPoints()));
    }

    public void createConfigWeb() {
        webCommands.add(new WebCommandJson(configManager));
        webCommands.add(new WebCommandFavicon(configManager));
        webCommands.add(new WebCommandCatch(configManager, configCatch));
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
            System.out.println("Added WebServe: " + cmd);
        }
        for(CommandType cmd: twitchCommands) {
            twitch.addCommand(cmd);
            System.out.println("Added Command: " + cmd);
        }
    }

    public void setConfigModel() {
        configManager = new ConfigManager(args);
        addContentManager(configManager);

        config = configManager.getConfig();

        twitchConfig = new TwitchConfig(config);
        webConfig = new WebConfig(config);

        configDice = new DiceConfig(config);
        configCatch = new CatchConfig(config);

        configMySQL = new MySQLConfig(config);

        configPoints = new PointsConfig(config, configMySQL);

        applicationConfig = new ApplicationConfig(null); //TODO: add a localStorage config file


        twitchConfig.gen();
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

    public boolean createSocketTwitch() {

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
            return true;
        } else {
            return false;
        }
    }

    public Bot createBot(String nickname, int port) {
        ImplBot.Builder builder = new ImplBot.Builder();
        builder.setServer(nickname);
        builder.setPort(port);
        return builder.build();
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public File getWorkingDirectory() {
        return config.getWorkingDirectory();
    }

    public TwitchConfig getTwitchConfig() {
        return twitchConfig;
    }
    public TwitchIRCListener getTwitch() {
        return twitch;
    }

    public CatchConfig getCatchConfig() {
        return configCatch;
    }

    public void addContentManager(ContentManager manager) {
        System.out.println("Added Manager: " + manager);
        contentManagers.add(manager);
    }

    public PointsConfig getPointConfig() {
        return configPoints;
    }

    public DiceConfig getDiceConfig() {
        return configDice;
    }
}
