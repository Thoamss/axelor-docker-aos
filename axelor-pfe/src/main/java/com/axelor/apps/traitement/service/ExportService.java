package com.axelor.apps.traitement.service;

import java.io.IOException;
import java.sql.SQLException;

public interface ExportService {

  public void exportCities() throws IOException, SQLException;

  public void exportProfiles() throws IOException, SQLException;
}
