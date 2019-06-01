/*
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

package com.github.blombler008.twitchbot.dave.core.config;

import com.github.blombler008.twitchbot.dave.core.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileConfiguration {

    private boolean existsBefore = true;
    private InputStream inputStream;
    private File file;
    private boolean locked = false;
    private boolean finishedCopy = true;

    public FileConfiguration(File file) {
        this.file = file;
        try {
            if (!file.isDirectory()) {
                if (!file.exists()) {
                    file.mkdirs();
                    if (file.isDirectory()) {
                        file.delete();
                    }
                    file.createNewFile();
                    existsBefore = false;
                }
                return;
            }
            throw new FileNotFoundException(StringUtils.replaceStringWith("Failed to read file: %file%", "file", file.getAbsolutePath()));
        } catch (Throwable ignore) {
        }
    }

    public FileConfiguration(FileConfiguration config) {
        this.file = config.file;
        this.inputStream = config.inputStream;
        this.locked = config.locked;
        this.finishedCopy = config.finishedCopy;
    }

    public FileConfiguration(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return "FileConfiguration{" +
                "existsBefore=" + existsBefore +
                ", file=" + file +
                ", locked=" + locked +
                '}';
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public Reader getReader() throws FileNotFoundException {
        if (file == null) {
            return new InputStreamReader(inputStream);
        }
        return new FileReader(file);
    }

    public Writer getWriter() throws IOException {
        if (!locked) {
            if (file == null) {
                return null;
            }
            return new FileWriter(file);
        }
        return null;
    }

    public boolean copy() {
        if (!locked) {
            finishedCopy = false;
            try {
                BufferedWriter bf = new BufferedWriter(new FileWriter(file));

                String namme = file.getName();
                URL x0 = this.getClass().getResource("/");
                System.out.println(x0);

                InputStream stream = ClassLoader.getSystemResourceAsStream(namme);

                if(stream != null) {
                    Scanner scanner = new Scanner(stream);

                    while (scanner.hasNextLine()) {
                        bf.write(scanner.nextLine());
                        bf.newLine();
                        bf.flush();
                    }

                    finishedCopy = true;
                    return true;
                } else {
                    throw new NullPointerException(StringUtils.replaceStringWith("Failed to get resolve the file: %file%", "file", file.getName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public File getFile() {
        return file;
    }

    public void lock() {
        locked = true;
    }

    public boolean finishedCopy() {
        return finishedCopy;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isExistsBefore() {
        return existsBefore;
    }
}
