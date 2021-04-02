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
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.Bot;
import com.github.blombler008.twitchbot.dave.core.ImplBot;
import com.github.blombler008.twitchbot.dave.core.WebServe;
import com.github.blombler008.twitchbot.dave.core.config.ApplicationConfig;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;
import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;
import com.github.blombler008.twitchbot.dave.core.exceptions.AuthenticationException;
import com.github.blombler008.twitchbot.dave.gui.GUI;
import com.github.blombler008.twitchbot.dave.main.commands.CommandDice;
import com.github.blombler008.twitchbot.dave.main.commands.WebCommandFavicon;
import com.github.blombler008.twitchbot.dave.main.commands.katch.*;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandAddPoints;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandPoints;
import com.github.blombler008.twitchbot.dave.main.commands.points.CommandSetPoints;
import com.github.blombler008.twitchbot.dave.main.configs.*;
import com.github.blombler008.twitchbot.dave.main.katch.JSONFile;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import discord4j.core.event.domain.guild.MemberUpdateEvent;
import discord4j.core.event.domain.lifecycle.GatewayLifecycleEvent;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.ActivityUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class Load {

    public static Load IMP; // root initialisation object
    public static Logger log; // logger
    private final String[] args; // start arguments
    private final List<WebCommand> webCommands = new ArrayList<>(); // web access commands
    private final List<CommandType> twitchCommands = new ArrayList<>(); // twitch irc commands

    // content managers for relation between web, twitch, discord, sql and teamspeak
    private final List<ContentManager> contentManagers = new ArrayList<>();

    // twitch bot instance
    private Bot twitchBot;

    // web bot instance
    private Bot webBot;

    // managing object of the configuration object
    private ConfigManager configManager;

    // root config of the configuration file
    private YamlConfiguration config;

    // root IRC listener of the twitch chat
    private TwitchIRCListener twitch;

    // Configuration objects
    private WebConfig webConfig;
    private DiceConfig configDice;
    private CatchConfig configCatch;
    private MySQLConfig configMySQL;
    private TwitchConfig twitchConfig;
    private DiscordConfig discordConfig;
    private PointsConfig configPoints;
    private ApplicationConfig applicationConfig;


    // Some constants for the discord bot
    private final static Snowflake CHANNEL_VERIFICATION_ID = Snowflake.of(824623601273405440L);
    private final static Snowflake CHANNEL_MEMBERS_LIST_ID = Snowflake.of(825021730765930576L); // Channel does currently not exist
    private final static Snowflake MESSAGE_VERIFICATION_ID = Snowflake.of(824624336392421418L);
    private final static Snowflake ROLE_VERIFIED_ID = Snowflake.of(824620958719410206L);
    private final static Snowflake ROLE_UNVERIFIED_ID = Snowflake.of(824622725023924264L);
    private final static Snowflake GUILD_ID = Snowflake.of(432270031922003969L);
    private final static String emoteCheckmark = "✅"; // :white_check_mark: emote :: one default discord unicode
    private final static String nickname = "Lucie"; // Bot nickname on the Discord server
    private final static String statusMessage = "deinen befehlen"; // status activity and message
    private final static ActivityUpdateRequest statusActivity = Activity.listening(statusMessage); // status activity and message



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

        String[] logo = new String[]{
                "IyMjIyMjIyMgIyMgICAgICAjIyAjIyMjICMjIyMjIyMjICAjIyMjIyMgICMjICAgICAjIyAjIyMjIyMjIyAgICMjIyMjIyMgICMjIyMjIyMjIA",
                "ICAgIyMgICAgIyMgICMjICAjIyAgIyMgICAgICMjICAgICMjICAgICMjICMjICAgICAjIyAjIyAgICAgIyMgIyMgICAgICMjICAgICMjICAgIA",
                "ICAgIyMgICAgIyMgICMjICAjIyAgIyMgICAgICMjICAgICMjICAgICAgICMjICAgICAjIyAjIyAgICAgIyMgIyMgICAgICMjICAgICMjICAgIA",
                "ICAgIyMgICAgIyMgICMjICAjIyAgIyMgICAgICMjICAgICMjICAgICAgICMjIyMjIyMjIyAjIyMjIyMjIyAgIyMgICAgICMjICAgICMjICAgIA",
                "ICAgIyMgICAgIyMgICMjICAjIyAgIyMgICAgICMjICAgICMjICAgICAgICMjICAgICAjIyAjIyAgICAgIyMgIyMgICAgICMjICAgICMjICAgIA",
                "ICAgIyMgICAgIyMgICMjICAjIyAgIyMgICAgICMjICAgICMjICAgICMjICMjICAgICAjIyAjIyAgICAgIyMgIyMgICAgICMjICAgICMjICAgIA",
                "ICAgIyMgICAgICMjIyAgIyMjICAjIyMjICAgICMjICAgICAjIyMjIyMgICMjICAgICAjIyAjIyMjIyMjIyAgICMjIyMjIyMgICAgICMjICAgIA"
        };

        System.out.println();
        for (String s : logo) {
            System.out.println(new String(Base64.getDecoder().decode(s)));
        }
        System.out.println();

        if (IMP == null)
            IMP = load;
        else
            return;


        load.setConfigModel();

        load.enableLogger();

        if (load.configManager.isDiscord()) {

            try {
                // Creating a client using the discord configs token
                DiscordClient client = DiscordClient.create(load.discordConfig.getPassword());

                // logging in
                GatewayDiscordClient gateway = client.login().block();

                // gateway null check
                if (Objects.nonNull(gateway)) {

                    Guild currentGuild = gateway.getGuildById(GUILD_ID).block();

                    // set self nickname and Update Presence to something funny
                    // check the guild to be available
                    if (Objects.nonNull(currentGuild)) {
                        currentGuild.changeSelfNickname(nickname).block();

                        // update bot presence
                        gateway.updatePresence(Presence.online(statusActivity)).block();


                        // Give a role upon joining the server
                        gateway.on(MemberJoinEvent.class).subscribe(event -> {

                            // extracting the member who joined
                            final Member member = event.getMember();

                            log.info(member.getMention() + " Joined");

                            // if the member is a bot ignore him!
                            if (member.isBot()) {
                                log.info(member.getMention() + " ignored (bot)");
                                return;
                            }
                            member.addRole(ROLE_UNVERIFIED_ID).block();


                            log.info(member.getMention() + " Added Role");
                        });


                        // simple bot ping command example // -> !ping results in an replay like : `@blombler: !ping -> Pong!`
                        // TODO: remove later and create a command interface for commands
                        gateway.on(MessageCreateEvent.class).subscribe(event -> {
                            final Message oldMessage = event.getMessage();
                            if ("!ping".equals(oldMessage.getContent())) {
                                final MessageChannel channel = oldMessage.getChannel().block();
                                Message newMessage = channel.createMessage(
                                        oldMessage.getAuthor().get().getMention()
                                                + ": "
                                                + oldMessage.getContent()
                                                + " -> pong!"
                                ).block();
                                if(event.getGuildId().isPresent())
                                    oldMessage.delete().block();
                            }
                        });

                        // Message verification on reaction hit
                        gateway.on(ReactionAddEvent.class).subscribe(event -> {


                            // TODO: user later for a function
                            boolean removeReaction = true;
                            boolean passedVerification = true;

                            // extract the message
                            Message message = event.getMessage().block();

                            // check message for null ... never seen it happen
                            if (Objects.isNull(message)) {
                                log.info("Message happens to be null somehow !?!?!?!?");
                                return;
                            }

                            // check if the event member is present
                            if (event.getMember().isPresent()) {
                                final Member member = event.getMember().get();

                                // check if the member is a bot
                                if (member.isBot()) {
                                    return;
                                }


                                // check if the guild is not null (usually it don't but happens sometimes)
                                if (Objects.isNull(currentGuild)) {
                                    log.info("Guild is not valid!");
                                    return;
                                }

                                // check if the event emote is a unicode
                                if (event.getEmoji().asUnicodeEmoji().isPresent()) {

                                    // extracting the unicode emote
                                    String emote = event.getEmoji().asUnicodeEmoji().get().getRaw();


                                    // check if the emote is a checkmark
                                    // and verify that the user reacted is in the right channel
                                    // and verify that the user reacted message is the right message
                                    if (emote.equals(emoteCheckmark)
                                            && event.getChannelId().equals(CHANNEL_VERIFICATION_ID)
                                            && event.getMessageId().equals(MESSAGE_VERIFICATION_ID)) {
                                        // extracting the Role of the guild and verify that the user does have the role
                                        Role currentGuildUnverifiedRole = currentGuild.getRoleById(ROLE_UNVERIFIED_ID).block();
                                        // make sure the role is not null -.-
                                        if (Objects.nonNull(currentGuildUnverifiedRole)) {

                                            // extraction of the value for a null check
                                            // and check for true
                                            Boolean block = member.getRoles().hasElement(currentGuildUnverifiedRole).block();
                                            if (Objects.nonNull(block) && block) {

                                                // remove the user reaction
                                                //noinspection ConstantConditions
                                                if (removeReaction) {
                                                    message.removeReaction(event.getEmoji(), member.getId()).block();
                                                }

                                                // remove verified user role and add verified role
                                                //noinspection ConstantConditions
                                                if (passedVerification) {

                                                    member.removeRole(ROLE_UNVERIFIED_ID).block();
                                                    member.addRole(ROLE_VERIFIED_ID).block();
                                                }
                                                log.info(member.getMention() + " Verified ");
                                            }
                                        } else {
                                            log.info("Role is null!");
                                        }
                                    }
                                } else {
                                    log.info("Emote is not a unicode!");
                                }
                            }
                        });


                        try {

                            // update members as when a user joins or leaves
                            currentGuild.getChannelById(CHANNEL_MEMBERS_LIST_ID).doOnSuccess(guildChannel -> {
                                gateway.on(MemberLeaveEvent.class).subscribe(load::updateMembers);
                                gateway.on(MemberJoinEvent.class).subscribe(load::updateMembers);
                            }).block();

                        } catch (Exception ingore) {}

                    }
                    // log the completion of the code execution
                    new Thread(() -> gateway.onDisconnect().block()).start();
                    log.info("DiscordBot started");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (load.configManager.isGui()) {
            try {
                if (load.applicationConfig.isLightTheme()) {
                    FlatLightLaf.install();
                } else {
                    FlatDarkLaf.install();
                }
                FlatInspector.install("ctrl shift alt X");
                FlatUIDefaultsInspector.install("ctrl shift alt Y");
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                UIManager.put("CheckBox.icon.style", "filled");
                UIManager.put("Component.arrowType", "triangle");
                UIManager.put("TabbedPane.tabLayoutPolicy", "scroll");
                UIManager.put("Component.hideMnemonics", false);

            } catch (Exception ignore) {
            }

            GUI gui = new GUI(load);
            gui.setContentPane(gui.$$$getRootComponent$$$());
            gui.setSize(gui.getDimension());
            gui.setMinimumSize(gui.getMinimumDimension());
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            if (gui.apply()) {
                gui.setVisible(true);
            }
            ((JTabbedPane) gui.$$$getRootComponent$$$()).setSelectedIndex(1);
            ((JTabbedPane) gui.$$$getRootComponent$$$()).setSelectedIndex(0);
            return;
        }

        if (load.configManager.isTwitch()) {
            if (load.createSocketTwitch()) {
                load.createSocketWeb();
                load.join();
                load.createConfigTwitch();
                load.createConfigMySQL();
                load.createConfigWeb();

                load.registerCommands();

                load.startCatch();

            }
        }

    }

    private void updateMembers(MemberLeaveEvent event) {
        Guild currentGuild = event.getGuild().block();
        if(Objects.nonNull(currentGuild)) {
            updateMembers(CHANNEL_MEMBERS_LIST_ID, currentGuild, currentGuild.getMembers().count().block());
        }
    }

    private void updateMembers(MemberJoinEvent event) {
        Guild currentGuild = event.getGuild().block();
        if(Objects.nonNull(currentGuild)) {
            updateMembers(CHANNEL_MEMBERS_LIST_ID, currentGuild, currentGuild.getMembers().count().block());
        }
    }

    private void updateMembers(Snowflake channel, Guild currentGuild, long memberCount) {
        try {
            VoiceChannel members_channel = currentGuild.getChannelById(channel).cast(VoiceChannel.class).block();
            log.info("Members in discord: " + memberCount);
            if (Objects.nonNull(members_channel))  {
                members_channel.edit(spec -> {
                    spec.setName("Members: " + memberCount);
                }).block(Duration.ofSeconds(30));
            }
            log.info("Members channel updated");
        } catch (IllegalStateException e) {
            log.info("Members channel update Failed");
        }

    }

    public void enableLogger() {
        if (Objects.isNull(Load.log)) {
            Load.log = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        }
    }


    public void createConfigMySQL() {
        if (configMySQL.connect()) {
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
        for (WebCommand cmd : webCommands) {
            WebServe.addCommand(cmd);
            System.out.println("Added WebServe: " + cmd);
        }
        for (CommandType cmd : twitchCommands) {
            twitch.addCommand(cmd);
            System.out.println("Added Command: " + cmd);
        }
    }

    public void setConfigModel() {
        configManager = new ConfigManager(args);
        addContentManager(configManager);

        config = configManager.getConfig();

        twitchConfig = new TwitchConfig(config);
        discordConfig = new DiscordConfig(config);
        webConfig = new WebConfig(config);

        configDice = new DiceConfig(config);
        configCatch = new CatchConfig(config);

        configMySQL = new MySQLConfig(config);

        configPoints = new PointsConfig(config, configMySQL);

        applicationConfig = new ApplicationConfig(null); //TODO: add a localStorage config file


        twitchConfig.gen();
        discordConfig.gen();
        configPoints.gen();
        configMySQL.gen();
        configDice.gen();
        configCatch.gen();
    }

    public void join() {
        twitch.joinChannel(twitchConfig.getChannel());
//        twitch.joinChannel("binarydave");
//        twitch.sendMessage(new Random().nextInt(100) + " - Bitte mir random stuff senden per Whisper(/w blombler008 <msg>) ... Danke <3 ♥ ... empfehlung tattyplay ♥");
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
