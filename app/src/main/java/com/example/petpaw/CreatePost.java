package com.example.petpaw;

public class CreatePost {
    private String username;
    private String phone;
    private String time;
    private String description;
    private String type;
    private String category;
    private String imageUri;

    public CreatePost() {
    }

    public CreatePost(String username, String phone,String time, String description, String type, String category, String imageUri) {
        this.username = username;
        this.phone = phone;
        this.time = time;
        this.description = description;
        this.type = type;
        this.category = category;
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
