package com.axelor.apps.traitement.service.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataOrganization {
  // Organize data to make clean CSV input files
  public static void organizeData() throws IOException {
    extractFirstNames(
        "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\forenames\\forenamesInput.csv");
    extractLastNames(
        "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\surnames\\surnamesInput.csv");
  }

  // Create a clean CSV first name file
  public static void extractLastNames(String fileName) throws IOException {
    List<String> result = new ArrayList<String>();

    BufferedReader br =
        new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
    for (String line = br.readLine(); line != null; line = br.readLine()) {
      result.add(line);
    }
    br.close();

    List<String> lastNames = new ArrayList<String>();
    for (int i = 0; i < result.size(); i++) {

      String[] tab = result.get(i).split(";");

      String lastName = tab[0];
      String occurence = tab[1];

      lastNames.add(lastName + ";" + occurence);
    }

    FileWriter csvWriter =
        new FileWriter(
            "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\surnames\\surnamesOutput.csv");
    for (String name : lastNames) {
      csvWriter.write(name + "\n");
    }
    csvWriter.close();
  }

  // Create a clean CSV last name file
  public static void extractFirstNames(String fileName) throws IOException {
    List<String> result = new ArrayList<String>();

    BufferedReader br =
        new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

    for (String line = br.readLine(); line != null; line = br.readLine()) {
      result.add(line);
    }

    br.close();

    List<String> names = new ArrayList<String>();
    names.add(result.get(0));
    names.add(result.get(1));
    for (int i = 2; i < result.size(); i++) {

      String[] previousTab = result.get(i - 1).split(";");
      String[] currentTab = result.get(i).split(";");
      int currentOccurence = Integer.parseInt(currentTab[3]);
      int prevOccurence = Integer.parseInt(previousTab[3]);

      String previousName = previousTab[1];

      if (!result.get(i).contains(previousName)) {
        names.add(result.get(i - 1));
      } else {
        currentOccurence += prevOccurence;
        result.set(
            i, currentTab[0] + ";" + currentTab[1] + ";" + currentTab[2] + ";" + currentOccurence);
      }
    }

    FileWriter csvWriter =
        new FileWriter(
            "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\forenames\\forenamesOutput.csv");
    for (String name : names) {
      csvWriter.write(name + "\n");
    }
    csvWriter.close();
  }
}
