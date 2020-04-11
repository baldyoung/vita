package com.baldyoung.vita.common.pojo.dto.menu;

import java.util.List;
import java.util.Map;

@Deprecated
public class NewMenuDto {
    List<Map> offList;
    List<Map> onList;

    @Override
    public String toString() {
        return "NewMenuDto{" +
                "offList=" + offList.toString() +
                ", onList=" + onList.toString() +
                '}';
    }
}
