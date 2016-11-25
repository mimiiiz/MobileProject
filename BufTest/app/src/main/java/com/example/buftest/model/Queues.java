package com.example.buftest.model;

import java.util.ArrayList;

/**
 * Created by User on 26/11/2559.
 */

public class Queues {
    private String code;
    private String tableNo;
    private String timestamp;
    private ArrayList<Order> menuOrdered;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Order> getMenuOrdered() {
        return menuOrdered;
    }

    public void setMenuOrdered(ArrayList<Order> menuOrdered) {
        this.menuOrdered = menuOrdered;
    }
}
