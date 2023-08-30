package cn.coderstory.anycracker.util;

import cn.coderstory.anycracker.bean.JDUser;

public class Tool {
    public static JDUser processUserBean(JDUser user) {
        user.setIsV("y");
        user.setIsPlus("y");
        user.setBalance("1145");
        return user;
    }
}
