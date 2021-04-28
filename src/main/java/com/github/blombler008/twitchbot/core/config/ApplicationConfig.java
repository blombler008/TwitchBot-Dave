package com.github.blombler008.twitchbot.core.config;

public class ApplicationConfig {

    private String title = "Gui test";
    private boolean resizeable = true;
    private boolean showTreeLines = true;
    private boolean systemTrayEnable = true;

    private YamlConfiguration localStorage;
    private boolean isLightTheme;

    public ApplicationConfig(YamlConfiguration localStorage) {

        this.localStorage = localStorage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
    }

    public boolean isTreeLinesShowen() {
        return showTreeLines;
    }

    public void setShowTreeLines(boolean showTreeLines) {
        this.showTreeLines = showTreeLines;
    }

    public boolean isSystemTrayEnabled() {
        return systemTrayEnable;
    }

    public void setSystemTrayEnable(boolean systemTrayEnable) {
        this.systemTrayEnable = systemTrayEnable;
    }

    public boolean isLightTheme() {
        return isLightTheme;
    }

    public void setLightTheme(boolean isLightTheme) {
        this.isLightTheme = isLightTheme;
    }
}
