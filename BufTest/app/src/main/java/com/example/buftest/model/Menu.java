package com.example.buftest.model;

/**
 * Created by User on 18/11/2559.
 */

public class Menu {
    private String MenuName;
    private Integer Max, Min;

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public Integer getMin() {
        return Min;
    }

    public void setMin(Integer min) {
        Min = min;
    }

    public Integer getMax() {
        return Max;
    }

    public void setMax(Integer max) {
        Max = max;
    }
}
