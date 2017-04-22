package com.capstone.naexpire.naexpirebusiness;

public class MenuItem{
    private String name, description, image;
    private double price, deal;
    private int id, quantity;

    MenuItem(){
        id = 0;
        name = "";
        price = 0.00;
        description = "";
        quantity = 0;
        deal = 0.0;
        image = "";
    }

    MenuItem(int id, String name, Double price, String description, int quantity, double deal, String image){
        this.id = id;
        this.name = name;
        this.price  = price;
        this.description = description;
        this.quantity = quantity;
        this.deal = deal;
        this.image = image;
    }
    Integer getId(){return id;}
    String getName(){return name;}
    Double getPrice(){return price;}
    String getDescription(){return description;}
    Integer getQuantity(){return quantity;}
    Double getDeal(){return deal;}
    String getImage(){return image;}

    void setName(String n){name = n;}
    void setPrice(Double p){price = p;}
    void setDescription(String d){description = d;}
    void setQuantity(Integer quantity){this.quantity = quantity;}
    void setDeal(Double deal){this.deal = deal;}
    void setImage(String i){image = i;}
}
