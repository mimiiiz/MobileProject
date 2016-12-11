package com.example.buftest.model;

import java.io.Serializable;

/**
 * Created by User on 18/11/2559.
 */

public class Menu implements Serializable {
    private String MenuName;
    private String key;
    private Integer Max;
    private Integer Min;
    private Integer amount;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

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
