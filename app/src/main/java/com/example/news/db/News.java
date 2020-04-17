package com.example.news.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "news_title")
    private String title;

    @ColumnInfo(name = "news_source")
    private String source;

    @ColumnInfo(name = "news_source_url")
    private String sourceURL;

    @ColumnInfo(name = "news_image_url")
    private String imageURL;

    public News(String title, String source, String sourceURL, String imageURL) {
        this.title = title;
        this.source = source;
        this.sourceURL = sourceURL;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}
