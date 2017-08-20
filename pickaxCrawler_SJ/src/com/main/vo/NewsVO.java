package com.main.vo;

import java.sql.Date;

public class NewsVO {
    private String dist_date;
    private String title;
    private String dist_time;
    private String writer_name;
    private String text_body;
    private String url;
    private String content_id;
    private String real_text_body;


    public String getReal_text_body() {
        return real_text_body;
    }

    public void setReal_text_body(String real_text_body) {
        this.real_text_body = real_text_body;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDist_date() {
        return dist_date;
    }

    public void setDist_date(String dist_date) {
        this.dist_date = dist_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDist_time() {
        return dist_time;
    }

    public void setDist_time(String dist_time) {
        this.dist_time = dist_time;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getText_body() {
        return text_body;
    }

    public void setText_body(String text_body) {
        this.text_body = text_body;
    }


}
