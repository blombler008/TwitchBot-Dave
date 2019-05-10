package com.github.blombler008.twitchbot;/*
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

    public static final String SERVER = "irc.twitch.tv";
    public static final String PORT = "6667";

    public static final String MSG_TEMPLATE = "PRIVMSG %channel% :%message%";
    public static final String CMD_TEMPLATE = "PRIVMSG %channel% :/%command% %args%";

    public static final String NICK_TEMPLATE = "NICK %name%";
    public static final String PASS_TEMPLATE = "PASS %oauth%";
    public static final String JOIN_TEMPLATE = "JOIN %channel%";
    public static final String PART_TEMPLATE = "PART %channel%";

    public static final String PING_TEMPLATE = "PING :tmi.twitch.tv";
    public static final String PONG_TEMPLATE = "PONG :tmi.twitch.tv";

    public static final String CAPREQ_TEMPLATE = "CAP REQ :twitch.tv/%irc%";

    public static final String CAP_MEMBERSHIP = "CAP REQ :twitch.tv/membership";
    public static final String CAP_TAGS = "CAP REQ :twitch.tv/tags";
    public static final String CAP_COMMANDS = "CAP REQ :twitch.tv/commands";
    public static final String CAP_CHAT_COMMANDS = "CAP REQ :twitch.tv/tags twitch.tv/commands";

    public static final String HTML_HTTP_11_200_OK = "HTTP/1.1 200 OK\r\n";
    public static final String HTML_HTTP_11_404_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    public static final String HTML_TRANSFER_ENCODING_BASE64 = "Transfer-Encoding: base64\r\n";
    public static final String HTML_CONTENT_ENCODING_BASE64 = "Content-Encoding: base64\r\n";
    public static final String HTML_CONTENT_APPLICATION_JSON = "Content-Type: application/json\r\n";
    public static final String HTML_CONTENT_APPLICATION_PDF = "Content-Type: application/pdf\r\n";
    public static final String HTML_CONTENT_TEXT_HTML = "Content-Type: text/html\r\n";
    public static final String HTML_CONTENT_TEXT_JAVASCRIPT = "Content-Type: text/javascript\r\n";
    public static final String HTML_CONTENT_TEXT_STYLESHEET = "Content-Type: text/css\r\n";
    public static final String HTML_CONTENT_IMAGE_PNG = "Content-Type: image/png\r\n";
    public static final String HTML_CONNECTION_CLOSE = "Connection: close\r\n";
    public static final String HTML_CONTENT_LENGTH = "Content-Length: ";

    public static final String HTML_END_OF_HEADERS = "\r\n\r\n";
    public static final String URL_PATH_SEPARATOR = "\\/+";
}
