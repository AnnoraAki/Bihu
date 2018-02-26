package com.example.cynthia.bihu.Data;

/**
 * Created by Cynthia on 2018/2/11.
 */

public class Answer {
    private int id;
    private String content;
    private String images;
    private String date;
    private int best;
    private int exciting;
    private int naive;
    private int authorId;
    private String authorName;
    private String authorAvatar;
    private boolean is_exciting;
    private boolean is_naive;

    public void setNaive(int naive) {
        this.naive = naive;
    }

    public void setExciting(int exciting) {
        this.exciting = exciting;
    }

    public void setIs_exciting(boolean is_exciting) {
        this.is_exciting = is_exciting;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImages() {
        return images;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public void setIs_naive(boolean is_naive) {
        this.is_naive = is_naive;
    }

    public String getDate() {
        return date;
    }

    public boolean isIs_naive() {
        return is_naive;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isIs_exciting() {
        return is_exciting;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public int getNaive() {
        return naive;
    }

    public int getExciting() {
        return exciting;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }
}
