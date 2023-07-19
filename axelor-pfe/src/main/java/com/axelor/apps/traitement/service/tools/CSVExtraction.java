package com.axelor.apps.traitement.service.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVExtraction {

  // Build a first name list from the treated INSEE CSV file
  public static List<String> getFirstNamesFromCsv() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\forenames\\forenamesOutput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      String value = values[1].charAt(0) + "";
      for (int c = 1; c < values[1].length(); c++) {
        value += values[1].toLowerCase().charAt(c);
      }
      records.add(value);
    }
    return records;
  }

  // Build a name list from the treated INSEE CSV file
  public static List<String> getLastNamesFromCsv() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\surnames\\surnamesOutput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      records.add(values[0]);
    }
    return records;
  }

  // Build a gender list associated with first names
  public static List<String> getGendersFromCsv() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\forenames\\forenamesOutput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      records.add(values[0] + ";" + values[1] + ";" + values[3]);
    }
    return records;
  }

  // Build a role list from a CSV
  public static List<String> getRolesFromCsv() throws IOException {
    List<String> records = new ArrayList<>();
    records.add("DEV");
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\roles\\rolesInput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      records.add(values[0]);
    }
    return records;
  }

  // Build a competence list from a CSV
  public static List<String> getCompFromCsv() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\competences\\competencesInput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      records.add(values[0].toUpperCase());
    }
    return records;
  }

  // Get cities list from a CSV file
  public static List<String> getCities() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\locations\\townsInput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      records.add(line);
    }
    return records;
  }

  // Write the data in a CSV file
  public static void writeCsvFile(List<HashMap<String, String>> contacts)
      throws FileNotFoundException, IOException {
    FileWriter csvWriter =
        new FileWriter("C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\output.csv");
    csvWriter.write(
        "Gender;First Name;Last Name;Mail;Phone;Roles;Competences;Localisation;Adresse;Experience"
            + "\n");
    for (HashMap<String, String> contact : contacts) {
      csvWriter.write(
          contact.get("gender")
              + ";"
              + contact.get("firstName").toLowerCase()
              + ";"
              + contact.get("lastName")
              + ";"
              + contact.get("mail")
              + ";"
              + contact.get("phone")
              + ";"
              + contact.get("roles")
              + ";"
              + contact.get("competences")
              + ";"
              + contact.get("localisation")
              + ";"
              + contact.get("address")
              + ";"
              + contact.get("exp")
              + "\n");
    }
    csvWriter.close();
  }

  public static List<String> getOutputProfiles() throws IOException {
    List<String> records = new ArrayList<>();
    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\output.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      records.add(line);
    }
    return records;
  }
}
