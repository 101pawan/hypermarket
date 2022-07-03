package com.hypermarket_android.ui.home;

import org.jetbrains.annotations.NotNull;

public class Trending {

    String url, category,price;
    int image;

    public Trending(String url, String category, String price) {
        this.url = url;
        this.category = category;
        this.price = price;
    }

    public Trending(int bag, @NotNull String s, @NotNull String s1) {
        this.image = bag;
        this.category = s;
        this.price = s1;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
