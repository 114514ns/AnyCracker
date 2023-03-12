package cn.coderstory.xposedtemplate;

import android.app.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class State {
    public static boolean allowGPS;
    public static String baseURL = BackDoor.getBaseURL();
    public static List<File> imgList = new ArrayList<>();
    public static Activity activity;
}
