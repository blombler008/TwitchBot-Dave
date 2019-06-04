package com.github.blombler008.twitchbot.dave.core;/*
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

public class Strings {

    public static final String TWITCH_SERVER = "irc.twitch.tv";
    public static final String TWITCH_PORT = "6667";

    public static final String MSG_TEMPLATE = "PRIVMSG %channel% :%message%";
    public static final String MSG_WHISPLER_TEMPLATE = "PRIVMSG %channel% :/w %name% %message%";

    public static final String IRC_NICK_TEMPLATE = "NICK %name%";
    public static final String IRC_PASS_TEMPLATE = "PASS %oAuth%";
    public static final String IRC_JOIN_TEMPLATE = "JOIN %channel%";
    public static final String IRC_PART_TEMPLATE = "PART %channel%";

    public static final String IRC_PING_TEMPLATE = "PING :tmi.twitch.tv";
    public static final String IRC_PONG_TEMPLATE = "PONG :tmi.twitch.tv";

    public static final String IRC_CAP_REQ_TEMPLATE = "CAP REQ :twitch.tv/%irc%";

    public static final String IRC_CAP_MEMBERSHIP = "CAP REQ :twitch.tv/membership";
    public static final String IRC_CAP_TAGS = "CAP REQ :twitch.tv/tags";
    public static final String IRC_CAP_COMMANDS = "CAP REQ :twitch.tv/commands";
    public static final String IRC_CAP_CHAT_COMMANDS = "CAP REQ :twitch.tv/tags twitch.tv/commands";

    public static final String HTML_HTTP_11_200_OK = "HTTP/1.1 200 OK\r\n";
    public static final String HTML_HTTP_11_404_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    public static final String HTML_TRANSFER_ENCODING_BASE64 = "Transfer-Encoding: base64\r\n";
    public static final String HTML_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials: true\r\n";
    public static final String HTML_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: null\r\n";
    public static final String HTML_CONTENT_ENCODING_BASE64 = "Content-Encoding: base64\r\n";
    public static final String HTML_CONTENT_APPLICATION_JSON = "Content-Type: application/json\r\n";
    public static final String HTML_CONTENT_APPLICATION_PDF = "Content-Type: application/pdf\r\n";
    public static final String HTML_CONTENT_TEXT_HTML = "Content-Type: text/html\r\n";
    public static final String HTML_CONTENT_TEXT_JAVASCRIPT = "Content-Type: text/javascript\r\n";
    public static final String HTML_CONTENT_TEXT_STYLESHEET = "Content-Type: text/css\r\n";
    public static final String HTML_CONTENT_IMAGE_PNG = "Content-Type: image/png\r\n";
    public static final String HTML_CONTENT_IMAGE_X_ICON = "Content-Type: image/x-icon\r\n";
    public static final String HTML_CONNECTION_CLOSE = "Connection: close\r\n";
    public static final String HTML_CONTENT_LENGTH = "Content-Length: ";

    public static final String HTML_END_OF_HEADERS = "\r\n\r\n";

    public static final String URL_PATH_SEPARATOR = "\\/+";
    public static final String STRING_REGEX_SEPARATOR = "\\s+";

    public static final String BOT_NULL_OBJECT = "Bot is not created";

    public static final String CONFIG_FILE = "config.yaml";
    public static final String CONFIG_DEFAULT_PATH = "TwitchBot-Dave";

    // WEB /////////////////////////////////////////////////////////////////////////////
    public static final String CONFIG_WEB_SERVER = "web.server";
    public static final String CONFIG_WEB_PORT = "web.port";

    // TWITCH //////////////////////////////////////////////////////////////////////////
    public static final String CONFIG_TWITCH_CHANNEL = "twitch.channel";
    public static final String CONFIG_TWITCH_NICKNAME = "twitch.nickname";
    public static final String CONFIG_TWITCH_EXTERNAL_OAUTH = "twitch.externalOAuth";
    public static final String CONFIG_TWITCH_OAUTH = "twitch.OAuth";
    public static final String CONFIG_TWITCH_PREFIX = "twitch.prefix";

    // DICE /////////////////////////////////////////////////////////////////////////////
    public static final String CONFIG_DICE_ENABLE = "game.dice.enable";
    public static final String CONFIG_DICE_MAX_BOUND = "game.dice.max";

    // CATCH ////////////////////////////////////////////////////////////////////////////
    public static final String CONFIG_CATCH_ENABLE = "game.catch.enable";
    public static final String CONFIG_CATCH_NO = "game.catch.no";

    public static final String CONFIG_CATCH_TIMER_MIN = "game.catch.timer.min";
    public static final String CONFIG_CATCH_TIMER_MAX = "game.catch.timer.max";

    public static final String CONFIG_CATCH_MISSED_TIME = "game.catch.missed.time";
    public static final String CONFIG_CATCH_MISSED_ENABLE = "game.catch.missed.enable";
    public static final String CONFIG_CATCH_MISSED_MESSAGE = "game.catch.missed.message";


    public static final String CONFIG_CATCH_WINNER_REWARD = "game.catch.winner.reward.message";
    public static final String CONFIG_CATCH_WINNER_REWARD_ENABLE = "game.catch.winner.reward.enable";

    public static final String CONFIG_CATCH_WINNER_MESSAGE = "game.catch.winner.message";

    public static final String CONFIG_CATCH_WINNER_REPEAT_ENABLE = "game.catch.winner.repeat.enable";
    public static final String CONFIG_CATCH_WINNER_REPEAT_MESSAGE = "game.catch.winner.repeat.message";

    public static final String CONFIG_CATCH_IMAGE_LOCATION = "game.catch.image.location";

}
