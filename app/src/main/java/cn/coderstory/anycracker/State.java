package cn.coderstory.anycracker;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class State {
    public static boolean allowGPS;
    public static String baseURL = "https://api.pprocket.cn";//BackDoor.getBaseURL();
    public static List<File> imgList = new ArrayList<>();
    public static Activity activity;
    public static boolean isExpire = false;
    public static List<String> mediaList;
    public static boolean hasPermission;
    public static String notice = "";

    public static Context context;
    public static int interval;
    public static long BUILD_TIME = -1;
    public static boolean needUpdate = false;
    public static boolean isValid = true;
}
