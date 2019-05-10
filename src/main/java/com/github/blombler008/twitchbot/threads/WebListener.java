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

import com.github.blombler008.twitchbot.Strings;
import com.github.blombler008.twitchbot.Timeout;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class WebListener extends Thread {

    private String prefix;
    private OutputStreamWriter webOutWriter;
    private InputStream webIn;
    private OutputStream webOut;
    private String preLine;
    private Timeout timeout;
    private Socket client;


    public WebListener(String prefix, Socket client, String name) throws IOException {
        this.setName(name);
        this.client = client;
        this.prefix = prefix;
        this.webIn = client.getInputStream();
        this.webOut = client.getOutputStream();
        this.webOutWriter = new OutputStreamWriter(client.getOutputStream());
        this.preLine = (client.getInetAddress()).getHostAddress() + " on " + client.getPort() + " requested ";
        this.timeout = new Timeout(client);
        System.out.println(prefix + " Socket: " + client.getInetAddress() + ":" + client.getPort() + " created!");
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
                if(isInterrupted()) {
                    break;
                }
                String line = bfs.readLine();
                String [] got;
                while(line != null)  {
                    got = line.split("\\s+");
                    if(got.length > 2 && got[0].equalsIgnoreCase("get")) {
                        StringBuilder output = new StringBuilder();
                        String s1 = got[1];

                        if(s1.equalsIgnoreCase("/json")) {
                            File json = new File("json/index.json");
                            StringBuilder stringBuilder = new StringBuilder();
                            BufferedReader bf = new BufferedReader(new FileReader(json));
                            String vLine = "";

                            while((vLine = bf.readLine()) != null) {
                                stringBuilder.append(vLine);
                            }

                            String b = stringBuilder.toString();

                            output.append(Strings.HTML_HTTP_11_200_OK);
                            output.append(Strings.HTML_CONNECTION_CLOSE);
                            output.append(Strings.HTML_CONTENT_APPLICATION_JSON);
                            output.append(Strings.HTML_CONTENT_LENGTH);
                            output.append(b.length());
                            output.append(Strings.HTML_END_OF_HEADERS);
                            output.append(b);
                        }

                        if(s1.equalsIgnoreCase("/main.js")) {
                            File index = new File("html/main.js");
                            StringBuilder stringBuilder = new StringBuilder();
                            BufferedReader bf = new BufferedReader(new FileReader(index));
                            String vLine = "";

                            while((vLine = bf.readLine()) != null) {
                                stringBuilder.append(vLine);
                            }

                            String b = stringBuilder.toString();

                            output.append(Strings.HTML_HTTP_11_200_OK);
                            output.append(Strings.HTML_CONNECTION_CLOSE);
                            output.append(Strings.HTML_CONTENT_TEXT_JAVASCRIPT);
                            output.append(Strings.HTML_CONTENT_LENGTH);
                            output.append(b.length());
                            output.append(Strings.HTML_END_OF_HEADERS);
                            output.append(b);
                        }

                        if(s1.equalsIgnoreCase("/favicon.ico")) {
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
                            continue;
                            /*
                            byte[] bytes = Files.readAllBytes(Paths.get("favicon.png"));
                            output = Strings.OUTPUT_HEADERS_IMAGE + bytes.length + Strings.HTML_END_OF_HEADERS+ new String(bytes);*/
                        }

                        if(s1.equalsIgnoreCase("/")) {
                            BufferedImage image = ImageIO.read(new File("favicon.png"));
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            ImageIO.write(image, "png", outputStream);
                            String encodedImage = Base64.encode(outputStream.toByteArray());
                            File index = new File("html/index.html");
                            StringBuilder stringBuilder = new StringBuilder();
                            BufferedReader bf = new BufferedReader(new FileReader(index));
                            String vLine = "";

                            while((vLine = bf.readLine()) != null) {
                                stringBuilder.append(vLine);
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
                        }
                        webOutWriter.write(output.toString());
                        webOutWriter.flush();

                        String send = Arrays.toString(output.toString().split("\r\n"));
                        System.out.println(prefix + preLine + got[1] + " :" + send);

                    }
                    if(line.equalsIgnoreCase("")) {
                        getClient().close();
                        this.interrupt();
                    }
                    line = bfs.readLine();
                    if(line == null) {
                        getClient().close();
                        this.interrupt();
                    }
                }

            } catch (SocketException e) {
                e.getCause();
                breakOut = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getClient() {
        return client;
    }
}
