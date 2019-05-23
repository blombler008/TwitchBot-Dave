package com.github.blombler008.twitchbot.threads;/*
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

import com.github.blombler008.twitchbot.PrintLogger;
import com.github.blombler008.twitchbot.Strings;
import com.github.blombler008.twitchbot.Timeout;
import com.github.blombler008.twitchbot.TwitchBot;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebListener extends Thread {

    public static List<String> sites = new ArrayList<>();
    public final PrintLogger logger;
    private String prefix;
    private OutputStreamWriter webOutWriter;
    private InputStream webIn;
    private OutputStream webOut;
    private String preLine;
    private Timeout timeout;
    private Socket client;


    public WebListener(String prefix, Socket client, String name, PrintLogger logger) throws IOException {
        this.setName(name);
        this.logger = logger;
        this.client = client;
        this.prefix = prefix;
        this.webIn = client.getInputStream();
        this.webOut = client.getOutputStream();
        this.webOutWriter = new OutputStreamWriter(client.getOutputStream());
        this.preLine = (client.getInetAddress()).getHostAddress() + " on " + client.getPort() + " requested ";
        TwitchBot.getPrintStream().logWeb(prefix + " Socket: " + client.getInetAddress() + ":" + client.getPort() + " created!");
    }

    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        boolean breakOut = false;

        BufferedReader bfs = new BufferedReader(new InputStreamReader(webIn));
        while (!breakOut) {
            try {
                if (isInterrupted()) {
                    break;
                }
                String line = bfs.readLine();

                logger.writeSeparate("Send Web> " + line, false);
                String[] got;
                while (line != null) {
                    got = line.split("\\s+");
                    if (got.length > 2 && got[0].equalsIgnoreCase("get")) {
                        StringBuilder output = new StringBuilder();

                        String s1 = got[1].replaceFirst(Strings.URL_PATH_SEPARATOR, "");
                        String[] paths = s1.split(Strings.URL_PATH_SEPARATOR);
                        File index = null;

                        output.append(Strings.HTML_HTTP_11_200_OK);
                        output.append(Strings.HTML_CONNECTION_CLOSE);

                        if (sideExists(s1)) {
                            if (paths[0].equalsIgnoreCase("json")) {
                                index = handleJSON(paths);
                            }

                            if (s1.equalsIgnoreCase("favicon.ico")) {
                                handleFavicon();
                                continue;
                            }

                            if (s1.equalsIgnoreCase("")) {
                                handleRoot();
                                continue;
                            }

                            if (paths[0].equalsIgnoreCase("getPenguin")) {
                                String penguinLocation = TwitchBot.getConfig().getProperty(Strings.CONFIG_REWARD_LOCATION);
                                File penguin = new File(penguinLocation);
                                if (penguin.getName().endsWith(".gif")) {
                                    handleGIF(penguin);
                                } else {
                                    handlePNG(penguin);
                                }
                                continue;
                            }

                        } else {
                            if (s1.contains(".")) {
                                String extension = null;
                                for (String sx : paths) {
                                    if (sx.contains(".")) {
                                        extension = sx.substring(sx.lastIndexOf(".") + 1);
                                        break;
                                    }
                                }

                                if (extension != null) {
                                    switch (extension) {
                                        case "js":
                                            index = new File("html", s1);
                                            output.append(Strings.HTML_CONTENT_TEXT_JAVASCRIPT);
                                            break;
                                        case "css":
                                            index = new File("html", s1);
                                            output.append(Strings.HTML_CONTENT_TEXT_STYLESHEET);
                                            break;
                                        case "json":
                                            index = new File("json", s1);
                                            output.append(Strings.HTML_CONTENT_APPLICATION_JSON);
                                            break;
                                        case "png":
                                            index = new File("html", s1);
                                            handlePNG(index);
                                            continue;
                                        case "gif":
                                            index = new File("html", s1);
                                            handleGIF(index);
                                            continue;
                                        case "html":
                                            index = new File("html", s1);
                                            output.append(Strings.HTML_CONTENT_TEXT_HTML);
                                            break;
                                        default:
                                            index = new File("html/404index.html");
                                            output.append(Strings.HTML_CONTENT_TEXT_HTML);
                                            break;
                                    }
                                } else {

                                    output.append(Strings.HTML_HTTP_11_200_OK);
                                    output.append(Strings.HTML_CONNECTION_CLOSE);
                                    output.append(Strings.HTML_CONTENT_TEXT_HTML);

                                    index = new File("html/404index.html");
                                }
                            }
                        }


                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader bf = new BufferedReader(new FileReader(index));
                        String vLine;

                        while ((vLine = bf.readLine()) != null) {
                            stringBuilder.append(vLine);
                            stringBuilder.append("\n");
                        }

                        String b = stringBuilder.toString();

                        output.append(Strings.HTML_CONTENT_LENGTH);
                        output.append(b.length());
                        output.append(Strings.HTML_END_OF_HEADERS);
                        output.append(b);

                        webOutWriter.write(output.toString());
                        webOutWriter.flush();

                        TwitchBot.getPrintStream().logWeb(prefix + preLine + got[1] + " ,file send :" + index);

                    }
                    if (line.equalsIgnoreCase("")) {
                        getClient().close();
                        this.interrupt();
                    }
                    line = bfs.readLine();
                    if (line == null) {
                        getClient().close();
                        this.interrupt();
                    }
                }

            } catch (SocketException e) {
                e.getCause();
                breakOut = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGIF(File png) throws IOException {
        StringBuilder output = new StringBuilder();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(png);
        try {
            byte[] buffer = new byte[1024];
            int count;

            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }

            // Flush out stream, to write any remaining buffered data
            out.flush();
        } finally {
            in.close();
        }
        out.close();

        output.append(Strings.HTML_HTTP_11_200_OK);
        output.append(Strings.HTML_CONNECTION_CLOSE);
        output.append(Strings.HTML_CONTENT_IMAGE_X_ICON);
        output.append(Strings.HTML_CONTENT_LENGTH);
        output.append(out.toByteArray().length);
        output.append(Strings.HTML_END_OF_HEADERS);
        webOutWriter.write(output.toString());
        webOutWriter.flush();
        webOut.write(out.toByteArray());
        webOut.flush();
    }


    private boolean sideExists(String s1) {
        for (String s : sites) {
            if (s.equalsIgnoreCase(s1))
                return true;
        }
        return false;
    }

    private void handlePNG(File png) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedImage image = ImageIO.read(png);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);

        output.append(Strings.HTML_HTTP_11_200_OK);
        output.append(Strings.HTML_CONNECTION_CLOSE);
        output.append(Strings.HTML_CONTENT_IMAGE_PNG);
        output.append(Strings.HTML_CONTENT_LENGTH);
        output.append(stream.toByteArray().length);
        output.append(Strings.HTML_END_OF_HEADERS);
        webOutWriter.write(output.toString());
        webOutWriter.flush();
        webOut.write(stream.toByteArray());
        webOut.flush();
    }

    private void handleFavicon() throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedImage image = ImageIO.read(new File("favicon.png"));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);

        output.append(Strings.HTML_HTTP_11_200_OK);
        output.append(Strings.HTML_CONNECTION_CLOSE);
        output.append(Strings.HTML_CONTENT_IMAGE_PNG);
        output.append(Strings.HTML_CONTENT_LENGTH);
        output.append(stream.toByteArray().length);
        output.append(Strings.HTML_END_OF_HEADERS);
        webOutWriter.write(output.toString());
        webOutWriter.flush();
        webOut.write(stream.toByteArray());
        webOut.flush();
    }

    private File handleJSON(String paths[]) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File json;

        if (paths.length == 1) {
            json = new File("json", "index.json");
        } else if (paths.length > 1) {
            StringBuilder s = new StringBuilder();
            for (int i = 1; i < paths.length; i++) {
                s.append(paths[i]);
                if (i != paths.length - 1)
                    s.append("/");
            }
            json = new File("json", s.toString());
        } else {
            json = new File("html/404index.html");
        }

        return json;
    }

    private void handleRoot() throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedImage image = ImageIO.read(new File("favicon.png"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);
        String encodedImage = Base64.encode(outputStream.toByteArray());
        File index = new File("html/index.html");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bf = new BufferedReader(new FileReader(index));
        String vLine = "";

        while ((vLine = bf.readLine()) != null) {
            stringBuilder.append(vLine);
            stringBuilder.append("\n");
        }

        String b = stringBuilder.toString();
        b = b.replaceAll("%favicon%", encodedImage);

        output.append(Strings.HTML_HTTP_11_200_OK);
        output.append(Strings.HTML_CONNECTION_CLOSE);
        output.append(Strings.HTML_CONTENT_TEXT_HTML);
        output.append(Strings.HTML_CONTENT_LENGTH);
        output.append(b.length());
        output.append(Strings.HTML_END_OF_HEADERS);
        output.append(b);
        webOutWriter.write(output.toString());
        webOutWriter.flush();

        String send = Arrays.toString(output.toString().split("\r\n"));
        TwitchBot.getPrintStream().logWeb(prefix + preLine + "/ ,file send :" + index);
    }

    public Socket getClient() {
        return client;
    }
}
