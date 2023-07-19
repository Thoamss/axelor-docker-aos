package com.axelor.apps.traitement.service.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

public class DataExtraction {

  // Create a dictionary for each resume filled with candidates data
  public static List<HashMap<String, String>> extractDataFromFiles() throws IOException {
    // Configure the path of the PDF file
    File folder =
        new File("C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\Ressources\\2022-08\\01");
    File[] listOfFiles = folder.listFiles();

    List<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    for (File currentFile : listOfFiles) {
      // Load the PDF file as a PDDocument
      PDDocument document = PDDocument.load(currentFile);

      // Get the text
      String hardText = TextTreatment.getTextFromPdf(document);
      String text = TextTreatment.cleanText(hardText);

      // Make a list from these comma-separated elements
      List<String> items = TextTreatment.makeList(text);
      // Make list of String fields
      List<String> fields = TextTreatment.makeStringList(hardText);

      // Declare the dictionary
      HashMap<String, String> tempContact = new HashMap<String, String>();

      // Declare the mail RegEx
      String mailRegEx = "^(.+)@(.+)\\.(.+)$";

      // Declare the phone RegExs
      String phoneRegEx =
          "(.)*\\(\\+[3][3]\\)\\s+\\d+(.)*|(.)*\\+[3][3]\\s+\\d+(.)*|(.)*\\+[3][3]\\d+(.)*|(.)*\\(\\+[3][3]\\)\\d+(.)*|(.)*[0][6]\\s+\\d+(.)*|(.)*[0][6]\\d+(.)*|(.)*[0][7]\\s+\\d+(.)*|(.)*[0][7]\\d+(.)*";

      // Fill the dictionary with candidate information
      tempContact.put("firstName", getFirstName(fields));
      tempContact.put("lastName", getLastName(fields));
      tempContact.put("gender", getGender(tempContact.get("firstName")));
      tempContact.put("mail", getEmail(items, mailRegEx));
      tempContact.put("phone", getPhoneNumber(items, phoneRegEx));
      tempContact.put("roles", getRoles(items));
      tempContact.put("competences", getCompetences(items));
      tempContact.put("localisation", getLocation(items));
      tempContact.put("address", getAddress(items));
      tempContact.put("exp", getExperience(items));

      // Add the dictionary to the contact list
      contactList.add(tempContact);

      // Close the document
      document.close();
    }
    return contactList;
  }

  // Get the candidate surname from the text
  private static String getFirstName(List<String> items) throws IOException {
    String firstName = "";
    // Create a list from the CSV file
    List<String> firstNames = CSVExtraction.getFirstNamesFromCsv();
    // Find matches between first name list and resume text
    items.retainAll(firstNames);
    if (!items.isEmpty()) {
      for (String name : items) {
        if (name.length() > 2) {
          firstName = name;
          break;
        } else {
          firstName = firstNames.get(0);
        }
      }
    } else {
      firstName = "No first name found";
    }
    return firstName;
  }

  // Get the candidate name from the text
  private static String getLastName(List<String> items) throws FileNotFoundException, IOException {
    String finalName = "";
    List<String> names = CSVExtraction.getLastNamesFromCsv();
    List<String> results = new ArrayList<String>();
    for (String item : items) {
      if (item.matches("\\w+")) {
        results.add(item.toUpperCase());
      }
    }
    results.retainAll(names);
    if (!results.isEmpty()) {
      for (String name : results) {
        if (name.length() > 2) {
          finalName = name;
          break;
        } else {
          finalName = results.get(0);
        }
      }
    } else {
      for (String item : items) {
        results.add(item.toUpperCase());
      }
      results.retainAll(names);
      if (!results.isEmpty()) {
        for (String name : results) {
          if (name.length() > 2) {
            finalName = name;
            break;
          } else {
            finalName = results.get(0);
          }
        }
      } else {
        finalName = "No last name found";
      }
    }
    return finalName;
  }

  // Get the candidate gender using its first name
  private static String getGender(String firstName) throws IOException {
    int occur = 0;
    String genderTemp = "";
    List<String> genderList = CSVExtraction.getGendersFromCsv();
    for (String item : genderList) {
      String genderRegEx = "\\d(.)";
      for (int i = 0; i < firstName.length(); i++) {
        genderRegEx += "[" + firstName.toUpperCase().charAt(i) + "]";
      }
      genderRegEx += "(.)\\d+";
      if (item.matches(genderRegEx)) {
        String genderType = "";
        genderType = item.split(";")[0];
        int occurTemp = Integer.parseInt(item.split(";")[2]);
        if (genderType.equals("1")) {
          if (occurTemp > occur) {
            genderTemp = "Monsieur";
          } else {
            genderTemp = "Madame";
          }
        } else {
          if (occurTemp > occur) {
            genderTemp = "Madame";
          } else {
            genderTemp = "Monsieur";
          }
        }
        occur = occurTemp;
      }
    }
    if (!genderTemp.isEmpty()) {
      return genderTemp;
    } else {
      return "No gender found";
    }
  }

  // Get the email from the text
  private static String getEmail(List<String> items, String mailRegEx) {
    String mail = "Mail Not Found";
    for (String item : items) {
      if (item.matches(mailRegEx)) {
        String tempMail = item;
        String[] mailList;
        mailList = tempMail.split(" ");
        for (int i = 0; i < mailList.length; i++) {
          if (mailList[i].matches(mailRegEx)) {
            mail = mailList[i];
            break;
          } else if (mailList[i].matches("^(.+)@$")) {
            mail = mailList[i] + mailList[i + 1];
            break;
          }
        }
        if (!mail.isEmpty()) {
          break;
        }
      }
    }
    return mail;
  }

  // Get the phone number from the text
  private static String getPhoneNumber(List<String> items, String phoneRegEx) {
    String phone = "";
    for (String item : items) {
      if (item.matches(phoneRegEx)) {
        String tempPhone = "";
        if (item.matches(".*\\+\\d+.*")) {
          String[] splitted = item.split("\\+[3][3]");
          tempPhone = splitted[1].replaceAll("\\D", "");
          tempPhone = "+33" + tempPhone;
          tempPhone = tempPhone.substring(0, 12);

        } else if (item.matches(".*[0][0]\\s[3][3].*")) {
          String[] splitted = item.split("[0][0]\\s[3][3]");
          tempPhone = splitted[1].replaceAll("\\D", "");
          tempPhone = "+33" + tempPhone;
          tempPhone = tempPhone.substring(0, 12);
        } else {
          tempPhone = item.replaceAll("\\D", "");
          tempPhone = tempPhone.substring(0, 10);
        }
        if (tempPhone.length() > 9 && tempPhone.length() < 14) {
          phone = tempPhone;
        }
        if (!phone.isEmpty()) {
          break;
        }
      }
    }
    if (phone.isEmpty()) {
      phone = "Phone Not Found";
    }
    return phone;
  }

  // Get the roles from the text
  private static String getRoles(List<String> items) throws IOException {
    List<String> inputRoles = CSVExtraction.getRolesFromCsv();
    List<String> roles = new ArrayList<String>();
    String stringRoles = "";

    List<String> results = new ArrayList<String>();
    for (String item : items) {
      results.add(item.toUpperCase());
    }
    results.retainAll(inputRoles);
    if (!results.isEmpty()) {
      for (String role : results) {
        if (!roles.contains(role)) {
          roles.add(role);
        }
      }
    } else {
      roles.add("No roles found");
    }
    for (int i = 0; i < roles.size() - 1; i++) {
      stringRoles = stringRoles + roles.get(i) + ", ";
    }
    if (!roles.isEmpty()) {
      stringRoles += roles.get(roles.size() - 1);
    }
    return stringRoles;
  }

  // Get candidate competences from the text
  private static String getCompetences(List<String> items) throws IOException {
    List<String> inputComp = CSVExtraction.getCompFromCsv();
    List<String> comps = new ArrayList<String>();
    String stringComps = "";
    List<String> results = new ArrayList<String>();
    for (String item : items) {
      results.add(item.toUpperCase());
    }
    results.retainAll(inputComp);
    if (!results.isEmpty()) {
      for (String comp : results) {
        if (!comps.contains(comp)) {
          comps.add(comp);
        }
      }
    } else {
      comps.add("No competences found");
    }
    for (int i = 0; i < comps.size() - 1; i++) {
      stringComps = stringComps + comps.get(i) + ", ";
    }
    if (!comps.isEmpty()) {
      stringComps += comps.get(comps.size() - 1);
    }
    return stringComps;
  }

  // Get location from the text
  private static String getLocation(List<String> items) throws IOException {
    String location = "";
    List<String> locations = new ArrayList<String>();

    BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                new FileInputStream(
                    "C:\\Users\\ThomasGREBAUT\\Documents\\PROJETS\\PFE\\DATA\\locations\\townsInput.csv"),
                "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = line.split(";");
      locations.add(values[3] + ";" + values[4] + ";" + values[2] + ";" + values[1]);
    }
    for (String item : items) {
      for (int j = 0; j < locations.size(); j++) {
        String[] splittedLoc = locations.get(j).split(";");
        String cityCapitalName = splittedLoc[0];
        String cityName = splittedLoc[1];
        if (item.equals(cityCapitalName)) {
          location = cityName;
          break;
        } else if (item.equals(cityName)) {
          location = cityName;
          break;
        } else if (item.toUpperCase().equals(cityCapitalName)) {
          location = cityName;
          break;
        } else if (item.contains(cityName)) {
          location = cityName;
          break;
        } else if (item.contains(cityCapitalName)) {
          location = cityName;
          break;
        }
      }
      if (!location.isEmpty()) {
        break;
      }
    }
    return "Aucune localisation trouvée";
  }

  // Get candidate address from the text
  private static String getAddress(List<String> items) throws IOException {
    String address = "No address found";
    for (int i = 1; i < items.size(); i++) {
      String[] addressMatchesRegEx = {
        "(.)*\\s[r][u][e](.)*",
        "(.)*\\s[a][v][e][n][u][e](.)*",
        "(.)*\\s[b][o][u][l][e][v][a][r][d](.)*",
        "(.)*\\s[R][u][e](.)*",
        "(.)*\\s[A][v][e][n][u][e](.)*",
        "(.)*\\s[B][o][u][l][e][v][a][r][d](.)*",
        "(.)*\\s[R][U][E](.)*"
      };
      String[] addressContainsRegEx = {
        "(.)*\\s[A][V][E][N][U][E](.)*",
        "(.)*\\s[B][O][U][L][E][V][A][R][D](.)*",
        "(.)*\\s[a][v](.)*",
        "(.)*\\s[A][v](.)*",
        "(.)*\\s[A][V](.)*",
        "(.)*\\s[b][d](.)*",
        "(.)*\\s[B][d](.)*",
        "(.)*\\s[B][D](.)*"
      };
      for (int j = 0; j < addressMatchesRegEx.length; j++) {
        if (items.get(i).matches(addressMatchesRegEx[j])) {
          address = items.get(i);
          break;
        }
      }
      for (int j = 0; j < addressContainsRegEx.length; j++) {
        if (items.get(i).contains(addressContainsRegEx[j])) {
          address = items.get(i);
          break;
        }
      }
      if (!address.equals("No address found")) {
        address = items.get(i - 1) + " " + address + " " + items.get(i + 1);
        break;
      }
    }
    return address;
  }

  // Get the candidate's number of years of experience
  private static String getExperience(List<String> fields) {
    String xp = "0";
    List<String> years = new ArrayList<String>();
    List<String> dates = new ArrayList<String>();
    for (String field : fields) {
      if (field.matches("(.)*\\d\\d\\d\\d(.)*")) {
        years.add(field);
      }
    }
    for (int y = 0; y < years.size(); y++) {
      String newYear = years.get(y).replaceAll("\\D", ";");
      String[] yearSplitted = newYear.split("\\D");
      for (int i = 0; i < yearSplitted.length; i++) {
        if (yearSplitted[i].length() == 4) {
          dates.add(yearSplitted[i]);
        }
      }
    }
    int max = 0;
    int min = 0;
    if (!dates.isEmpty()) {
      if (dates.size() >= 2) {
        if (Integer.parseInt(dates.get(0)) > Integer.parseInt(dates.get(1))) {
          max = Integer.parseInt(dates.get(0));
          min = Integer.parseInt(dates.get(1));
        } else {
          max = Integer.parseInt(dates.get(1));
          min = Integer.parseInt(dates.get(0));
        }
        if (dates.size() > 2) {
          for (int d = 2; d < dates.size(); d++) {
            int a = Integer.parseInt(dates.get(d));
            if (a > max) {
              max = a;
            } else if (a < min) {
              min = a;
            }
          }
        }
      } else {
        max = Integer.parseInt(dates.get(0));
      }
    }
    xp = max - min + "";
    if (Integer.parseInt(xp) > 30) {
      xp = "0";
    }
    return xp;
  }
}
