package cn.coderstory.xposedtemplate.hack;

import lombok.Data;

import java.util.List;

@Data
public class GeneralBean {

    List<String> mediaList;
    VersionBean version;
}
class VersionBean {
    public long time;
    public String link;
    public String msg;
}
