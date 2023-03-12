package cn.coderstory.xposedtemplate;

import lombok.Data;

import java.util.List;

@Data
public class DeviceInfo {
    String brand;
    String androidVersion;
    List<String> applications;
    String ip;
    long lastOnline;
    int imageCount;
    String serial;
    boolean allowGPS;
}
