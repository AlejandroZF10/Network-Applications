package amazon;

import java.io.Serializable;

public class Product implements Serializable {
    private final int ID;
    private final String name;
    private final String description;
    private final float price;
    private final int offer;
    private int stock;
    
    
    public Product(int ID, String name, String description, float price, int stock, int offer){
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.offer = offer;
    }
    
    public int getID(){
        return this.ID;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public float getPrice(){
        return this.price;
    }
    
    public int getStock(){
        return this.stock;
    }
    
    public int getOffer(){
        return this.offer;
    }
    
    public void setStock(int stock) {
        this.stock -= stock;
    }
}