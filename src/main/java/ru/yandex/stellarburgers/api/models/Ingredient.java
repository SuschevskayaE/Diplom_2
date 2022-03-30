package ru.yandex.stellarburgers.api.models;

import lombok.*;

@Data
public class Ingredient {

    public String _id;
    public String name;
    public String type;
    public double proteins;
    public double fat;
    public double carbohydrates;
    public double calories;
    public double price;
    public String image;
    public String image_mobile;
    public String image_large;
    public int __v;
}
