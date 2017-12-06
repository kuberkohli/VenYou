package com.venyou.venyou.Model;

import java.io.Serializable;

/**
 * Created by kupal on 12/3/2017.
 */

public class Image implements Serializable {

    private String url;

    public Image(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
