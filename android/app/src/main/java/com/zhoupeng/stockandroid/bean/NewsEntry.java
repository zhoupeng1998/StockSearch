package com.zhoupeng.stockandroid.bean;

public class NewsEntry {
    private String category;
    private Long datetime;
    private String headline;
    private String image;
    private String source;
    private String summary;
    private String url;

    public NewsEntry() {
    }

    public NewsEntry(String category, Long datetime, String headline, String image, String source, String summary, String url) {
        this.category = category;
        this.datetime = datetime;
        this.headline = headline;
        this.image = image;
        this.source = source;
        this.summary = summary;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsEntry{" +
                "category='" + category + '\'' +
                ", datetime=" + datetime +
                ", headline='" + headline + '\'' +
                ", image='" + image + '\'' +
                ", source='" + source + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
