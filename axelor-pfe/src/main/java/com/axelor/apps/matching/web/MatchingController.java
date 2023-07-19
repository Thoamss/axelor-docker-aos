package com.axelor.apps.matching.web;

import com.axelor.apps.matching.service.MatchingService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.sql.SQLException;
import javax.inject.Inject;

// @RequestScope
public class MatchingController {
  @Inject private MatchingService service;

  public void match(ActionRequest request, ActionResponse response) throws SQLException {

    service.match();
  }
}
