package com.example.buftest.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 8/12/2559.
 */

public class Code implements Serializable {

    private String code, TableNo;
    private Date timeStamp;

    public Date getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timeStamp = timestamp;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
