package com.axelor.traitement.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.traitement.service.TraitementService;
import java.io.IOException;
import java.sql.SQLException;
import javax.inject.Inject;

// @RequestScope
public class TraitementController {
  @Inject private TraitementService service;

  public void treat(ActionRequest request, ActionResponse response)
      throws IOException, SQLException {

    service.treat();
  }
}
