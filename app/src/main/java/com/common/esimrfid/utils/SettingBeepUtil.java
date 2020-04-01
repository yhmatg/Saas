package com.common.esimrfid.utils;

public class SettingBeepUtil {
    private static boolean isOpen;
    private static boolean sledOpen;
    private static boolean hostOpen;

    public static boolean isOpen() {
        return isOpen;
    }

    public static void setOpen(boolean open) {
        isOpen = open;
    }


    public static boolean isSledOpen() {
        return sledOpen;
    }

    public static void setSledOpen(boolean sledOpen) {
        SettingBeepUtil.sledOpen = sledOpen;
    }

    public static boolean isHostOpen() {
        return hostOpen;
    }

    public static void setHostOpen(boolean hostOpen) {
        SettingBeepUtil.hostOpen = hostOpen;
    }
}
