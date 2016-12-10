package com.example.buftest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by User on 8/12/2559.
 */

public class Order implements Serializable {
    private Date timestamp;
    private Code code;
    private Map<String, Menu> menuMap;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Map<String, Menu> getMenus() {
        return menuMap;
    }

    public void setMenus(Map<String, Menu> menuMap) {
        this.menuMap = menuMap;
    }
}
