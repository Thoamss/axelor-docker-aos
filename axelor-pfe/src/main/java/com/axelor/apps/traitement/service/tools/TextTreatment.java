package com.axelor.traitement.service.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class TextTreatment {

  // Method to get text from PDF file
  public static String getTextFromPdf(PDDocument document) throws IOException {
    // Extract text from the PDF file
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    return text;
  }

  // Method to clean some text
  public static String cleanText(String text) {
    text = text.replaceAll("\\s", ",");
    text = text.replaceAll(",,,,,", ",");
    text = text.replaceAll(",,,,", ",");
    text = text.replaceAll(",,,", ",");
    text = text.replaceAll(",,", ",");
    return text;
  }

  // Method to make a List of String items from a text
  public static List<String> makeList(String text) {
    // Create a bounded Arrays$ArrayList and put every comma-separated data from the text
    List<String> tempItems = Arrays.asList(text.split(","));
    // Create a new ArrayList
    List<String> items = new ArrayList<String>();
    // Add to the ArrayList every item from the temporary Arrays$ArrayList
    for (String item : tempItems) {
      items.add(item);
    }
    // Clean the list
    items = cleanList(items);
    return items;
  }

  // Method to clean a list
  private static List<String> cleanList(List<String> items) {
    // Running through the list
    for (int i = 1; i < items.size(); i++) {
      // If an item is not beginning with an upper case character and is not an email
      if (!items.get(i).matches("^[A-Z]\\w+||^[A-Z]\\S+") && !items.get(i).matches("^(.+)@(.+)$")) {
        // Concatenate the item with the previous one
        items.set(i - 1, items.get(i - 1) + " " + items.get(i));
        // Remove it from the list
        items.remove(i);
        // Go back to the previous item
        i = i - 1;
      }
    }
    return items;
  }

  public static List<String> makeStringList(String text) {
    List<String> tempList = Arrays.asList(text.split("\\W"));
    // Create a new ArrayList
    List<String> items = new ArrayList<String>();
    // Add to the ArrayList every item from the temporary Arrays$ArrayList
    for (String item : tempList) {
      items.add(item);
    }
    return items;
  }
}
