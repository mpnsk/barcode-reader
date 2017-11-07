package com.example.mpnsk.barcodereader;


import java.util.List;

public class Article {

    int quantity;
    String title;
    List<String> features;
    String productGroup;
    String ean;
    String asin;

    @Override
    public String toString() {
        return "Article{" +
                "quantity=" + quantity +
                ", title='" + title + '\'' +
                ", features=" + features +
                ", productGroup='" + productGroup + '\'' +
                ", ean='" + ean + '\'' +
                ", asin='" + asin + '\'' +
                '}';
    }

    public void addToQunatity(int i) {
        quantity += i;
    }
}