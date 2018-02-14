package project2;
//Created by Sean C. on 2/8/2018

import java.text.NumberFormat;

public class Item {

    private StringBuilder builder = new StringBuilder();
    private NumberFormat format = NumberFormat.getCurrencyInstance();
    private String name = "N/A";
    private String amt = "N/A";
    private String quantifier = "";
    private double price = 0.0;
    private int quantity = 1;

    public Item(String name, double price, String amt){
        this.name = name;
        this.amt = amt;
        this.price = price;
    }

    public String getName() { return name; }

    public double getPrice(){ return price; }

    public String getAmt(){
        return amt;
    }

    public int getQuantity(){ return quantity; }

    public void stepQuantity(){
        quantity ++;
        quantifier = String.valueOf(quantity)+"@("+format.format(price)+")";
    }

    public double getTotalPrice(){ return quantity*price; }

    public boolean equals(Object other) {
        Item obj;
        if(other != null && Item.class.isAssignableFrom(other.getClass())){
            obj = (Item) other;
        }else{
            return false;
        }
        return name.equals(obj.getName()) && amt.equals(obj.getAmt());
    }

    public String toString(){
        builder.append(name);
        for(int i = 0; i < 60-name.length()-quantifier.length()-format.format(getTotalPrice()).length()-3; i++){
            builder.append(" ");
        }
        String finalUpdate = quantifier+"   "+format.format(getTotalPrice());
        builder.append(finalUpdate);
        return builder.toString();
    }
}
