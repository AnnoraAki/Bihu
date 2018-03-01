package com.example.cynthia.bihu.Data;

import java.util.List;

/**
 * Created by Cynthia on 2018/2/11.
 */

public class Question {

    private int id;
    private String title;
    private String content;
    private String images;
    private String date;
    private int exciting;
    private int naive;
    private String recent;
    private int answerCount;
    private int authorId;
    private String authorName;
    private String authorAvatar;
    private boolean is_exciting;
    private boolean is_naive;
    private boolean is_favorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public int getExciting() {
        return exciting;
    }

    public int getNaive() {
        return naive;
    }

    public String getContent() {
        return content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public boolean isIs_exciting() {
        return is_exciting;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public String getDate() {
        return date;
    }

    public String getImages() {
        return images;
    }

    public boolean isIs_naive() {
        return is_naive;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public String getRecent() {
        return recent;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setExciting(int exciting) {
        this.exciting = exciting;
    }

    public void setIs_exciting(boolean is_exciting) {
        this.is_exciting = is_exciting;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public void setIs_naive(boolean is_naive) {
        this.is_naive = is_naive;
    }

    public void setNaive(int naive) {
        this.naive = naive;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

}
