package com.capstone.naexpire.naexpirebusiness;

import java.util.ArrayList;

/**
 * Created by graemedrucker on 3/14/17.
 */

public class Restaurant {
    ArrayList<MenuItem> items;
    ArrayList<String> foodTypes;

    private String name;
    private String address;
    private String phone;
    private String username;
    private String password;

    Restaurant(){
        name = "";
        address = "";
        phone = "";
        username = "";
        password = "";
    }

    Restaurant(String n, String a, String p, String u, String pass){
        name = n;
        address = a;
        phone = p;
        username = u;
        password = pass;
    }

    public void setFoodTypes(ArrayList<String> ft){foodTypes = ft;}
    public void setItems(ArrayList<MenuItem> i){items = i;}

    public String getName(){return name;}
    public String getAddress(){return address;}
    public String getPhone(){return phone;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public ArrayList<String> getFoodTypes(){return foodTypes;}
    public ArrayList<MenuItem> getItems(){return items;}

    public class MenuItem{
        private String name;
        private double price;
        private String description;

        MenuItem(){
            name = "";
            price = 0.00;
            description = "";
        }

        MenuItem(String n, Double p, String d){
            name = n;
            price  = p;
            description = d;
        }
        String getName(){return name;}
        Double getPrice(){return price;}
        String getDescription(){return description;}

        void setName(String n){name = n;}
        void setPrice(Double p){price = p;}
        void setDescription(String d){description = d;}
    }
}
