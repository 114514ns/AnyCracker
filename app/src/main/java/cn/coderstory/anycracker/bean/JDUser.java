package cn.coderstory.anycracker.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data

public class JDUser {

    private String id;
    private String username;
    private String nickname;
    private String phone;
    private String img;
    private String sex;
    private String shareNum;
    private String parentName;
    private String nicknameUpdated;
    private String accountImg;
    private String showCity;
    private String balance;
    private String sendBalance;
    private String isPlus;
    private String isV;
    private String vExpiredStr;
    private String plusIcon;
    private String plusGroupEnd;
    private Object description;
    private String isRecharge;
    private Integer bloggerLevel;
    private Integer bloggerType;
    private String hasNoRead;
    private String isUnlock;
    private Integer unfinishOrders;
    private Integer unusedCoupons;
    private Object officialGroupLink;
    private String uploadUrl;
    private String uploadToken;
    private List<Banner> banner;

    @NoArgsConstructor
    @Data
    public static class Banner {
        private String id;
        private String name;
        private String link;
        private String img;
        private String time;
        private String type;
        private String position;
    }
}
