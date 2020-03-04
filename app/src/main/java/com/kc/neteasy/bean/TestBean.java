package com.kc.neteasy.bean;

import java.util.List;

public class TestBean {

    private List<String> data;
    private String filename;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public TestBean(List<String> data, String filename) {
        this.data = data;
        this.filename = filename;
    }
}
