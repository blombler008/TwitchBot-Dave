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

import org.json.JSONObject;

import java.io.*;

public class JSONFile {

    private static File json;

    public static void setFile(File json) {
        JSONFile.json = json;
    }

    public static void show() {
        updateCatch("true");
    }

    public static void hide() {
        updateCatch("false");
    }

    public static boolean updateCatch(String aTrue) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(json));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject jsonObj = new JSONObject(builder.toString());
            jsonObj.put("catch", aTrue);

            jsonObj.write(new FileWriter(json)).flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String get() {

        StringBuilder stringBuilder;
        BufferedReader bufferedReader;
        try {
            stringBuilder =  new StringBuilder();
            bufferedReader= new BufferedReader(new FileReader(json));
            String line;

            while(((line = bufferedReader.readLine()) != null)) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return stringBuilder.toString();
    }
}
