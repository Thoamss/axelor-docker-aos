package com.axelor.apps.traitement.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.apps.traitement.service.ExportService;
import java.io.IOException;
import java.sql.SQLException;
import javax.inject.Inject;

// @RequestScope
public class ExportController {
  @Inject private ExportService service;

  public void exportProfiles(ActionRequest request, ActionResponse response)
      throws IOException, SQLException {
    service.exportProfiles();
  }

  public void exportCities(ActionRequest request, ActionResponse response)
      throws IOException, SQLException {
    service.exportCities();
  }
}
