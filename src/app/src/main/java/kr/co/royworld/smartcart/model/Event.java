package kr.co.royworld.smartcart.model;

import java.util.HashMap;
import java.util.List;

public class Event {
    private String date;
    private Product[] prod;
    public Event(String date, Product[] prod){
        this.date = date;
        this.prod = prod;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Product[] getProd() {
        return prod;
    }

    public void setProd(Product[] prod) {
        this.prod = prod;
    }
}
