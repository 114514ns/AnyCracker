package cn.coderstory.anycracker.bean;

import lombok.Data;

import java.util.List;

@Data
public class GeneralBean {

    List<String> mediaList;
    VersionBean version;
    long endTime;
    List<String> ignoreList;
}

