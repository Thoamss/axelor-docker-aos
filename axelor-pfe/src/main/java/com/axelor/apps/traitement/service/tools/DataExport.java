package com.axelor.traitement.service.tools;

import java.io.IOException;
import java.sql.SQLException;

public class DataExport {

  public static void exportProfiles() throws SQLException, IOException {
    // ProfilesExport.initProfiles();

    // Insert profiles from output CSV to DB
    ProfilesExport.executeQueries(CSVExtraction.getOutputProfiles());
  }

  public static void exportCities() throws SQLException, IOException {
    CityExport.initCities();

    // Insert cities from CSV to DB
    CityExport.executeCityQuery();
  }
}
