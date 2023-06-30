package com.axelor.traitement.service;

import com.axelor.traitement.service.tools.*;
import java.io.IOException;
import java.sql.SQLException;

public class ExportServiceImpl implements ExportService {

  @Override
  public void exportCities() throws IOException, SQLException {
    DataExport.exportCities();
    System.out.println("Cities Exported");
  }

  @Override
  public void exportProfiles() throws IOException, SQLException {
    DataExport.exportProfiles();
    System.out.println("Profiles Exported");
  }
}
