package com.example.stefanovic.kemijskaindustrija.Model;

public class Notificaiton<T> {
    private T notificationType;
    private String title;
    private String description;

    public Notificaiton(T notificationType, String title, String description) {
        this.notificationType = notificationType;
        this.title = title;
        this.description = description;
    }

    public T getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(T notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
