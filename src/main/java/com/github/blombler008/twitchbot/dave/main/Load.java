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
import com.github.blombler008.twitchbot.dave.application.configs.DiceConfig;
import com.github.blombler008.twitchbot.dave.application.configs.TwitchConfig;
import com.github.blombler008.twitchbot.dave.application.commands.CommandType;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.Bot;
import com.github.blombler008.twitchbot.dave.core.ImplBot;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;
import com.github.blombler008.twitchbot.dave.application.configs.WebConfig;
import com.github.blombler008.twitchbot.dave.core.config.YamlConfiguration;
import com.github.blombler008.twitchbot.dave.core.exceptions.AuthenticationException;
import com.github.blombler008.twitchbot.dave.main.commands.CommandDice;

import java.io.IOException;

import static com.github.blombler008.twitchbot.dave.core.Strings.*;

public class Load {

    private Load instance;
    private String[] args;
    private Bot twitchBot;
    private ConfigManager configManager;
    private YamlConfiguration config;
    private TwitchIRCListener twitch;
    private TwitchConfig twitchConfig;
    private WebConfig webConfig;
    private Bot webBot;

    public Load(String[] args) {
        this.instance = this;
        this.args = args;
    }

    public static void main(String[] args) throws IOException {
        Load load = new Load(args);
        load.setConfigModel();
        load.createSocketTwitch();
        load.createSocketWeb();
        load.join();
        load.registerCommands();
    }

    public void createSocketWeb() {
        if(webConfig.gen()) {
            webBot = createBot(webConfig.getServer(), webConfig.getPort());
            webBot.hostSockets("Web-Reader", "Web-Writer");
        }

    }

    public void registerCommands() {
        DiceConfig configDice = new DiceConfig(config);
        CommandType diceType = new CommandType(CommandType.TYPE_PRIVMSG, "dice", new CommandDice(twitch, configDice));

        twitch.addCommand(diceType);
    }

    public void setConfigModel() {
        configManager = new ConfigManager(args);
        config = configManager.getConfig();
        twitchConfig = new TwitchConfig(config);
        webConfig = new WebConfig(config);
    }

    public void join() {
        twitch.joinChannel(twitchConfig.getChannel());
//        twitch.joinChannel("binarydave");
//        twitch.sendMessage(new Random().nextInt(100) + " - Bitte mir random stuff senden per Whisper(/w blombler008 <msg>) ... Danke ♥ ... empfehlung tattyplay ♥");
    }

    public void createSocketTwitch() throws IOException {

        if(twitchConfig.gen()) {
            twitchBot = createBot(TWITCH_SERVER, Integer.parseInt(TWITCH_PORT));

            if(twitchBot.initializeSockets("Twitch-Reader", "Twitch-Writer")) {
                twitch = new TwitchIRCListener(twitchBot);
                twitch.setPrefix(twitchConfig.getPrefix());
                if(!twitch.login(twitchConfig.getPassword(), twitchConfig.getNickname())) {
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
