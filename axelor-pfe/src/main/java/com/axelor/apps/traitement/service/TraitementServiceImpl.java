package com.axelor.traitement.service;

import com.axelor.traitement.service.tools.*;
import java.io.IOException;
import java.sql.SQLException;

public class TraitementServiceImpl implements TraitementService {

  @Override
  public void treat() throws IOException, SQLException {
    // Initialize the data
    DataOrganization.organizeData();
    System.out.println("Data Extracted from CSVs");
    // Create a csv file and add profiles generated
    CSVExtraction.writeCsvFile(DataExtraction.extractDataFromFiles());
    System.out.println("Data treated");
    DataExport.exportProfiles();
    System.out.println("Profiles exported");

    System.out.println("Done");
  }
}
