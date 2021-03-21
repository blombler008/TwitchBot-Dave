package com.github.blombler008.twitchbot.dave.main.commands.katch;/*
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

import com.github.blombler008.twitchbot.dave.application.commands.WebCommand;
import com.github.blombler008.twitchbot.dave.application.threads.TwitchIRCListener;
import com.github.blombler008.twitchbot.dave.core.Strings;
import com.github.blombler008.twitchbot.dave.core.config.ConfigManager;
import com.github.blombler008.twitchbot.dave.main.configs.CatchConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class WebCommandCatch extends WebCommand {

    private final File configFolder;
    private final CatchConfig config;

    public WebCommandCatch(ConfigManager configManager, CatchConfig config) {
        super("/catch");
        this.config = config;
        this.configFolder = configManager.getFolder();
    }

    @Override
    public String run(OutputStream outputStream) throws IOException {


        File file = new File(configFolder, config.getBlendImage());
        String [] ends = file.getName().split("\\.");
        String end = ends[ends.length-1];


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if(end.equalsIgnoreCase("png")) {
                BufferedImage image = ImageIO.read(file);
                ImageIO.write(image, "png", out);
                setContentType(Strings.HTML_CONTENT_IMAGE_PNG);
            } else if(end.equalsIgnoreCase("gif")) {

                InputStream in = new FileInputStream(file);
                try {
                    byte[] buffer = new byte[1024];
                    int count;

                    while ((count = in.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }

                    out.flush();
                } finally {
                    in.close();
                    out.close();
                    setContentType(Strings.HTML_CONTENT_IMAGE_X_ICON);
                }

            }
        } catch (Exception e) {
            System.out.println(file.getName() + " does not exist");
        }



        byte[] image = out.toByteArray();

        String output = Strings.HTML_HTTP_11_200_OK +
                Strings.HTML_CONNECTION_CLOSE +
                Strings.HTML_ACCESS_CONTROL_ALLOW_CREDENTIALS +
                Strings.HTML_ACCESS_CONTROL_ALLOW_ORIGIN +
                getContentType() +
                Strings.HTML_CONTENT_LENGTH +
                image.length +
                Strings.HTML_END_OF_HEADERS;
        outputStream.write(output.getBytes());
        outputStream.write(image);
        outputStream.flush();
        return null;
    }


}
