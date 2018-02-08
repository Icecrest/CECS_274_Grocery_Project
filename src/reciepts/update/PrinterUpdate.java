package reciepts.update;
//Created by Sean C. on 2/8/2018

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrinterUpdate {
    public static void main(String[] args) throws FileNotFoundException {
        File inFile = new File("resources/PriceList.txt");
        Scanner fileReader = new Scanner(inFile);
        Scanner userIn = new Scanner(System.in);
        String userInput;

        String noPriceRegex = "([\\w |\\W]+[s|\\w])\\s+(\\w[\\w |\\W]+\\S)\\s+([\\w |\\W]+\\s[\\w]+\\w)";
        String fullRegex =    "([\\w |\\W]+[s|\\w])\\s+(\\w[\\w |\\W]+\\S)\\s+([\\w |\\W]+\\s[\\w]+\\w)\\s+([\\w |\\W]+)";
        Pattern fullPattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Pattern noPricePattern = Pattern.compile(noPriceRegex, Pattern.CASE_INSENSITIVE);
        Matcher fileMatch;
        Matcher userMatch;

        System.out.print("Please enter what filename you would like to export to:\n>>");
        String filename = userIn.nextLine();
        PrintWriter fileOut = new PrintWriter("resources/"+filename+".txt");
        StringBuilder returnString = new StringBuilder();
        final int maxLen = 60;

        ArrayList<String> productNames = new ArrayList<>();
        HashMap<String, Item> products = new HashMap<>();

        String productName;
        boolean productInList = false;
        double totalPrice = 0.0;
        double payment = 0.0;
        int numItems;
        String quantifier;
        String itemPrice;
        ArrayList<String> fileBuffer = new ArrayList<>();
        NumberFormat nf = NumberFormat.getCurrencyInstance();


        /*
            Begin User Input Sequence
         */
        while(true) {
            System.out.print("Enter which product you would like to add to your cart:\t\tEnter \"DONE\" when finished.\n>>");
            userInput = userIn.nextLine().trim();
            if(!userInput.equalsIgnoreCase("done")){
                userMatch = noPricePattern.matcher(userInput);
            }else{
                break;
            }

            if(userMatch.find()){
                /*
                    If match, Begin matching process
                 */
                while(fileReader.hasNextLine()){

                    fileMatch = fullPattern.matcher(fileReader.nextLine());

                    if(fileMatch.find()){
                        if(fileMatch.group(1).trim().equals(userMatch.group(1)) && fileMatch.group(2).trim().equals(userMatch.group(2)) && fileMatch.group(3).trim().equals(userMatch.group(3))){
                            productName = fileMatch.group(1)+" "+fileMatch.group(2).trim();
                            if(products.containsKey(productName) && products.get(productName).getAmt().equals(userMatch.group(3))){
                                products.get(productName).setQuantity(1);
                            }else{
                                productNames.add(productName+" "+userMatch.group(3));
                                products.put(productName+" "+userMatch.group(3), new Item(Double.parseDouble(fileMatch.group(4)), userMatch.group(3),1));
                            }
                            productInList = true;
                        }
                    }
                }
            }
            if(!productInList){
                System.out.println("No such product in the inventory.  Please try again."); //EXTRA CREDIT
            }else{
                productInList = false;
            }
            fileReader.close();
            fileReader = new Scanner(inFile);
        }
        /*
            Begin String building sequence
         */

        fileOut.println("Sean's Food Emporium\n42 Answer Ln.\nChatanooga TN, 37341\n\n");
        fileOut.println("Product                                             Subtotal");
        fileOut.println("____________________________________________________________");

        for(String i : productNames){
            numItems = products.get(i).getQuantity();
            totalPrice += Double.parseDouble(String.format("%.2f", products.get(i).getTotalPrice()));
            itemPrice = String.format("$%.2f", products.get(i).getTotalPrice());
            returnString.append(i);
            if(numItems > 1){
                quantifier = String.valueOf(numItems)+"(@"+nf.format(products.get(i).getPrice())+")   ";

                for(int j = 0; j < maxLen-itemPrice.length()-i.length()-quantifier.length(); j++){
                    returnString.append(" ");
                }
                returnString.append(quantifier);
                returnString.append(itemPrice);
            }else{
                for(int j = 0; j < maxLen-itemPrice.length()-i.length(); j++){
                    returnString.append(" ");
                }
                returnString.append(itemPrice);
            }
            fileBuffer.add(returnString.toString());
            returnString = new StringBuilder();
        }

        Collections.sort(fileBuffer);
        for(String i : fileBuffer){
            fileOut.println(i);
        }
        fileOut.println();

        /*
            Begin Payment Sequence
         */
        fileOut.println();
        fileOut.print("Total");
        for(int i = 0; i < maxLen-5-nf.format(totalPrice).length(); i++){
            fileOut.print(" ");
        }
        fileOut.print(nf.format(totalPrice));
        fileOut.print("\n");

        while(payment < totalPrice){
            System.out.print("Payment Due: "+String.format("$%.2f", totalPrice-payment)+"\nPlease enter amount of payment:\n>>");
            payment += userIn.nextDouble();
        }


        String paymentStr = nf.format(payment);
        returnString.append("Payment");
        for(int i = 0; i < maxLen-paymentStr.length()-7; i++){
            returnString.append(" ");
        }
        returnString.append(paymentStr);
        fileOut.println(returnString);


        String changeStr = nf.format(payment - totalPrice);
        returnString = new StringBuilder();
        returnString.append("Change");
        for(int i = 0; i < maxLen-changeStr.length()-6; i++){
            returnString.append(" ");
        }
        returnString.append(changeStr);
        fileOut.println(returnString);

        fileOut.close();
        fileReader.close();
    }
}
