package reciepts;
//Created by Sean C. on 2/1/2018

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecieptPrinter {

    public static void main(String[] args) throws FileNotFoundException{
        File inFile = new File("C:\\Users\\Sean\\IdeaProjects\\CECS 274 Project 1\\resources\\PriceList.txt");
        Scanner fileReader = new Scanner(inFile);
        Scanner userIn = new Scanner(System.in);
        String userInput;

        String noPriceRegex = "([\\w]+)\\s+([\\w | \\s+]+)\\s+([\\w | \\W]+\\s[\\w]+\\w)";
        String fullRegex = "([\\w]+)\\s+([\\w | \\s+]+)\\s+([\\w | \\W]+\\s[\\w]+\\w)\\s+([\\w | \\W]+)";
        Pattern fullPattern = Pattern.compile(fullRegex);
        Pattern noPricePattern = Pattern.compile(noPriceRegex);
        Matcher fileMatch;
        Matcher userMatch;

        System.out.print("Please enter what filename you would like to export to:\n>>");
        String filename = userIn.nextLine();
        PrintWriter fileOut = new PrintWriter("C:\\Users\\Sean\\IdeaProjects\\CECS 274 Project 1\\resources\\"+filename+".txt");
        StringBuilder returnString = new StringBuilder();
        int maxLen = 60;

        HashMap<String, Double> recieptList = new HashMap<String, Double>();
        HashMap<String, Integer> quantityList = new HashMap<String, Integer>();
        ArrayList<String> productNames = new ArrayList<String>();

        String productName;
        int placeholderQuantitiy;
        boolean productInList = false;

        while(true) {
            System.out.print("Enter which product you would like to add to your cart:\t\tEnter \"DONE\" when finished.\n>>");
            userInput = userIn.nextLine();
            if(!userInput.equalsIgnoreCase("done")){
                userMatch = noPricePattern.matcher(userInput);
            }else{
                break;
            }

            if(userMatch.find()){
                while(fileReader.hasNextLine()){

                    fileMatch = fullPattern.matcher(fileReader.nextLine());

                    if(fileMatch.find()){
                        if(fileMatch.group(1).equals(userMatch.group(1)) && fileMatch.group(2).trim().equals(userMatch.group(2)) && fileMatch.group(3).equals(userMatch.group(3))){

                            System.out.println("~~ REGEX MATCH CHECK PASSED");

                            productName = fileMatch.group(1)+" "+fileMatch.group(2).trim();
                            recieptList.put(productName, Double.parseDouble(fileMatch.group(4)));
                            productNames.add(productName);

                            if(!quantityList.containsKey(productName)){
                                quantityList.put(productName, 1);
                            }else{
                                placeholderQuantitiy = quantityList.get(productName);
                                quantityList.replace(productName, placeholderQuantitiy, placeholderQuantitiy+1);
                            }
                            fileOut.println("PRODUCT FOUND");

                            productInList = true;

                        }
                    }
                }
            }
            if(!productInList){
                System.out.println("~~ Bool set to False, test failure".toUpperCase());  // DEBUG CODE LINE
                System.out.println("No such product in the inventory.  Please try again."); //EXTRA CREDIT
            }
            fileReader.close();
            fileReader = new Scanner(inFile);
        }
        // String itemName;
        String numItems;
        String itemPrice;

        for(String i : productNames){
            numItems = quantityList.get(i).toString();
            itemPrice = String.format("%.2f", recieptList.get(i));
            returnString.append(i);
            if(quantityList.get(i) > 1){

            }else{
                for(int j = 0; j < itemPrice.length(); j++){
                    returnString.append(" ");
                    // Continue from here with StringBuilder appends
                    // Build string iterant for each line of reciept
                }
            }

        }
        fileOut.println("Sean's Food Emporium\n42 Answer Ln.\nChatanooga TN, 37341");
        // DEBUG
        System.out.println(recieptList.toString());
        System.out.println(quantityList.toString());
        // END DEBUG
        fileOut.close();
        fileReader.close();
    }

}
