package cn.coderstory.anycracker.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserBean {
    private String serial;
    private long endTime;
    private long lastLogin;
    private List<String> ips;
}
