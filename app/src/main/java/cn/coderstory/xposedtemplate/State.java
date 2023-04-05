package cn.coderstory.xposedtemplate;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class State {
    public static boolean allowGPS;
    public static String baseURL = "http://192.168.50.5";//BackDoor.getBaseURL();
    public static List<File> imgList = new ArrayList<>();
    public static Activity activity;
    public static boolean isExpire = false;
    public static List<String> mediaList;
    public static boolean hasPermission;
    public static String notice = "";

    public static Context context;
}
