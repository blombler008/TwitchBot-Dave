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

package com.github.blombler008.twitchbot.core.config;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.github.blombler008.twitchbot.core.StringUtils;

import java.io.*;
import java.util.*;

public class YamlConfiguration {

    private static YamlConfig config = new YamlConfig();

    static {
        config.readConfig.setClassTags(false);
        config.writeConfig.setAutoAnchor(false);
        config.writeConfig.setWriteRootElementTags(false);
        config.writeConfig.setWriteRootTags(false);
        config.writeConfig.setCanonical(false);
        config.writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
        config.setAllowDuplicates(false);
    }

    private boolean reloaded = true;
    private FileConfiguration fileConfiguration;
    private File workingDirectory;
    private Map<String, Object> root;
    private YamlReader reader;


    public YamlConfiguration(FileConfiguration fileConfiguration, File workingDirectory) {
        this.fileConfiguration = fileConfiguration;
        this.workingDirectory = workingDirectory;
        try {
            reader = new YamlReader(fileConfiguration.getReader(), config);
            Map<?, ?> x = reader.read(HashMap.class);
            root = getMapFromObject(x);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfig getConfig() {
        return config;
    }

    public void copy() {
        if (root.isEmpty()) {
            fileConfiguration.copy();
            while (!fileConfiguration.finishedCopy()) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            reloaded = false;
            reload();
            while (!isReloaded()) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void set(String key, Object value) {
        set(key, value, root);
    }

    public Map<String, Object> getMap(String string) {
        return getMap(string, root);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<String> getList(String string) {
        List<String> list = new ArrayList<>();
        if (get(string) instanceof Map) {
            list.addAll(((Map) get(string)).keySet());
        }
        if (get(string) instanceof List) {
            list.addAll(((List) get(string)));
        }
        return list;
    }

    public Object get(String string) {
        Object gotten = get(string, root);
        if(Objects.isNull(gotten)) {
            throw new NullPointerException();
        }
        return gotten;
    }

    public void save() {
        save(root);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void set(String key, Object value, Map<String, Object> current) {
        String[] slit = key.split("\\.+");
        String fold = slit[0];
        if (slit.length == 1) {
            current.put(key, value);
            return;
        }
        StringBuilder newOne = new StringBuilder();
        for (int i = 1; i < slit.length; i++) {
            newOne.append(slit[i]);
            if (i != slit.length - 1) {
                newOne.append(".");
            }
        }
        if (current.containsKey(fold)) {
            if (current.get(fold) instanceof Map) {
                set(newOne.toString(), value, (Map) current.get(fold));
            }
        } else {
            Map<String, Object> newMap = new HashMap<>();
            current.put(fold, newMap);
            set(newOne.toString(), value, (Map) current.get(fold));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object get(String path, Map<String, Object> nodeMap) {
        if (path.equals("*")) {
            return root;
        }
        String[] slit = path.split("\\.+");
        String fold = slit[0];
        if (slit.length == 1) {
            return nodeMap.get(path);
        }
        StringBuilder newOne = new StringBuilder();
        for (int i = 1; i < slit.length; i++) {
            newOne.append(slit[i]);
            if (i != slit.length - 1) {
                newOne.append(".");
            }
        }
        if (nodeMap.containsKey(fold)) {
            if (nodeMap.get(fold) instanceof Map) {
                return get(newOne.toString(), (Map) nodeMap.get(fold));
            } else if (nodeMap.get(fold) instanceof List) {
                return (List) nodeMap.get(fold);
            } else {
                return nodeMap.get(fold);
            }
        }

        return null;
    }

    public void getCommand(String string) {
        String failMessage = StringUtils.replaceStringWith("%command% is not a valid command", "command", string);

        if (getMap("commands." + string) == null) {
            throw new NullPointerException(failMessage);
        }

        Map<String, Object> commandMap = getMap(StringUtils.replaceStringWith("commands.%command%", "command", string));

        if (!commandMap.containsKey("description") || !commandMap.containsKey("usage")) {
            throw new NullPointerException(failMessage);
        }
    }

    public List<String> getCommandList() {
        return getList("commands");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(String string, Map<String, Object> current) {
        return (Map<String, Object>) get(string, current);
    }


    // Just a work around the Type Safety
    public List<Object> getListFromObject(List<?> list) {
        List<Object> x = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            x.add(i, list.get(i));
        }
        return x;
    }

    // Just a work around the Type Safety
    public Map<String, Object> getMapFromObject(Map<?, ?> map) {
        if (map == null) {
            return new HashMap<>();
        }
        Map<String, Object> x = new HashMap<>();
        for (Map.Entry<?, ?> set : map.entrySet()) {
            if (set.getKey() instanceof String) {
                x.put(set.getKey().toString(), set.getValue());
            }
        }
        return x;
    }

    @Override
    public String toString() {
        return "YamlConfiguration{" +
                "fileConfiguration=" + fileConfiguration +
                ", root=" + root +
                '}';
    }

    public void save(Map<String, Object> map) {
        try {
            YamlWriter writer = new YamlWriter(fileConfiguration.getWriter(), config);
            writer.write(map);
            writer.close();
        } catch (IOException ignore) {
        }
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Integer getInteger(String key) {
        return Integer.parseInt(getString(key));
    }

    public boolean getBoolean(String key) {
        String val = getString(key);
        if (val == null || val.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(val);
    }

    public void reload() {
        reloaded = false;
        try {
            while (true) {
                if (fileConfiguration.finishedCopy()) {
                    break;
                }
            }
            reader = new YamlReader(fileConfiguration.getReader(), config);
            root = getMapFromObject(reader.read(HashMap.class));

        } catch (YamlException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            reloaded = true;
        }
    }

    public boolean isReloaded() {
        return reloaded;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public String getPassword(String entry) {
        return getPassword("password.yml", entry);
    }

    public String getPassword(String file, String entry) {
        YamlReader reader = null;
        BufferedReader bf = null;

        try {
            File passFile = new File(getWorkingDirectory(), file);

            try {
                bf = new BufferedReader(new FileReader(passFile));
            } catch (FileNotFoundException e) {
                return null;
            }

            reader = new YamlReader(bf, new YamlConfig());
            Map<?,?> map = getMapFromObject(reader.read(HashMap.class));
            return (String) map.get(entry);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bf != null)  bf.close();

                if(reader != null) reader.close();
            } catch (IOException ignore) {}
        }
        return null;

    }
}
