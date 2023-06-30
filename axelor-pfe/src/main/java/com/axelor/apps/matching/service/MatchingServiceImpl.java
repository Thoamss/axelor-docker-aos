package com.axelor.matching.service;

import com.axelor.matching.service.tools.*;
import java.sql.SQLException;

public class MatchingServiceImpl implements MatchingService {

  @Override
  public void match() throws SQLException {
    Matching.match();
  }
}
