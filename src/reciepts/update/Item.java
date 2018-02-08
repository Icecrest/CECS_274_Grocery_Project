package reciepts.update;
//Created by Sean C. on 2/8/2018

public class Item {
    private final String amt;
    private final double price;
    private int quantity;

    public Item(double price, String amt,int quantity){
        this.amt = amt;
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice(){
        return price;
    }
    public String getAmt(){
        return amt;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int step){
        quantity += step;
    }
    public double getTotalPrice(){
        return quantity*price;
    }
    public String toString(){
        return String.format("[%s, %f, %d]", amt, price, quantity);
    }
}
