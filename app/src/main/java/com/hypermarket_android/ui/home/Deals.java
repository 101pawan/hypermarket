package com.hypermarket_android.ui.home;

public class Deals {
    int image;
    String prev_price, next_price, discount;

    public Deals(int image, String prev_price, String next_price, String discount) {
        this.image = image;
        this.prev_price = prev_price;
        this.next_price = next_price;
        this.discount = discount;
    }


    public void setImage(int image) {
        this.image = image;
    }

    public String getPrev_price() {
        return prev_price;
    }

    public void setPrev_price(String prev_price) {
        this.prev_price = prev_price;
    }

    public String getNext_price() {
        return next_price;
    }

    public void setNext_price(String next_price) {
        this.next_price = next_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getImage() {
        return image;
    }
}
