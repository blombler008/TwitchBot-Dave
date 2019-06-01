package com.github.blombler008.twitchbot.dave.application;/*
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


import java.awt.*;
import java.util.*;
import java.util.List;

public class UserInfo {

    private List<String> fullInfo;

    private boolean isWhisper = false;

    private Color color;
    private Map<String,Integer> badges;
    private String uIdMessage;
    private String displayName;
    private String userType = "";
    private String bits;
    private boolean isModerator = false;
    private boolean isSubscriber = false;
    private boolean isTurboUser = false;
    private boolean isBroadcaster = false;
    private int inSubMonth = 0;
    private int roomId;
    private int userId;

    private boolean superUser = false;

    public UserInfo(String name) {
        if(name.equalsIgnoreCase("tattyplay")) {
            superUser = true;
        }
    }

    public UserInfo(String pref, boolean whisper) {
        this.isWhisper = whisper;
        fullInfo = Arrays.asList(pref.split(";"));
        String [] b;
        for(String s: fullInfo) {
            b=s.split("=");
            if(!isWhisper) {
                if(b.length > 1) {
                    if (b[0].equalsIgnoreCase("badges")) {
                        badges = new HashMap<>();
                        String[] c = b[1].split(",");
                        for (String x : c) {
                            String[] g = x.split("/");
                            badges.put(g[0], Integer.parseInt(g[1]));
                            if (g[0].equalsIgnoreCase("broadcaster")) {
                                isBroadcaster = true;
                            }

                            if (g[0].equalsIgnoreCase("subscriber")) {
                                inSubMonth = Integer.parseInt(g[1]);
                            }
                        }
                    }

                    if (b[0].equalsIgnoreCase("color")) {
                        int data0 = Integer.valueOf(b[1].substring(1, 3), 16);
                        int data1 = Integer.valueOf(b[1].substring(3, 5), 16);
                        int data2 = Integer.valueOf(b[1].substring(5, 7), 16);
                        color = new Color(data0, data1, data2);

                    }
                    if (b[0].equalsIgnoreCase("display-name")) {
                        displayName = b[1];
                    }

                    if (b[0].equalsIgnoreCase("id")) {
                        uIdMessage = b[1];
                    }

                    if (b[0].equalsIgnoreCase("mod")) {
                        if(b[1].equals("1")) {
                            isModerator = true;
                        }
                    }

                    if (b[0].equalsIgnoreCase("subscriber")) {
                        if(b[1].equals("1")) {
                            isSubscriber = true;
                        }
                    }

                    if (b[0].equalsIgnoreCase("turbo")) {
                        if(b[1].equals("1")) {
                            isTurboUser = true;
                        }
                    }

                    if (b[0].equalsIgnoreCase("room-id")) {
                        roomId = Integer.parseInt(b[1]);
                    }

                    if (b[0].equalsIgnoreCase("user-id")) {
                        userId = Integer.parseInt(b[1]);
                    }

                    if (b[0].equalsIgnoreCase("user-type")) {
                        userType = b[1];
                    }
                }

            } else {
                if(b.length > 1) {
                    if (b[0].equalsIgnoreCase("badges")) {
                        badges = new HashMap<>();
                        String[] c = b[1].split(",");
                        for (String x : c) {
                            String[] g = x.split("/");
                            badges.put(g[0], Integer.parseInt(g[1]));
                            if (g[0].equalsIgnoreCase("broadcaster")) {
                                isBroadcaster = true;
                            }
                        }
                    }

                    if (b[0].equalsIgnoreCase("color")) {
                        int data0 = Integer.valueOf(b[1].substring(1, 3), 16);
                        int data1 = Integer.valueOf(b[1].substring(3, 5), 16);
                        int data2 = Integer.valueOf(b[1].substring(5, 7), 16);
                        color = new Color(data0, data1, data2);
                    }

                    if (b[0].equalsIgnoreCase("user-type")) {
                        userType = b[1];
                    }

                    if (b[0].equalsIgnoreCase("user-id")) {
                        userId = Integer.parseInt(b[1]);
                    }

                    if (b[0].equalsIgnoreCase("turbo")) {
                        isTurboUser = true;
                    }

                    if (b[0].equalsIgnoreCase("display-name")) {
                        displayName = b[1];
                    }
                }
            }



        }
    }


    public Map<String,Integer> getBadges() {
        return badges;
    }

    public String getuIdMessage() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return uIdMessage;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isModerator() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return isModerator;
    }

    public boolean isSubscriber() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return isSubscriber;
    }

    public boolean isTurboUser() {
        return isTurboUser;
    }

    public boolean isBroadcaster() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return isBroadcaster;
    }

    public int getSubMonth() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return inSubMonth;
    }

    public int getRoomId() {
        if(isWhisper) throw new RuntimeException("This message is a whisper message");
        return roomId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isWhisper() {
        return isWhisper;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "isWhisper=" + isWhisper +
                ", color=" + color +
                ", badges=" + badges +
                ", uIdMessage='" + uIdMessage + '\'' +
                ", displayName='" + displayName + '\'' +
                ", userType='" + userType + '\'' +
                ", isModerator=" + isModerator +
                ", isSubscriber=" + isSubscriber +
                ", isTurboUser=" + isTurboUser +
                ", isBroadcaster=" + isBroadcaster +
                ", inSubMonth=" + inSubMonth +
                ", roomId=" + roomId +
                ", userId=" + userId +
                '}';
    }
}
