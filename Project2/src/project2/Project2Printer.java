package project2;
//Created by Sean C. on 2/13/2018

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Project2Printer {
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

        ArrayList<Item> products = new ArrayList<>();

        String productName;
        boolean productInList = false;
        double totalPrice = 0.0;
        double payment = 0.0;
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
                            Item placeholder = new Item(productName, Double.parseDouble(fileMatch.group(4)), fileMatch.group(3).trim());
                            if(products.contains(placeholder)){
                                products.get(products.indexOf(placeholder)).stepQuantity();
                            }else{
                                products.add(placeholder);
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

        for(Item i : products){
            fileBuffer.add(i.toString());
            totalPrice += i.getTotalPrice();
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
