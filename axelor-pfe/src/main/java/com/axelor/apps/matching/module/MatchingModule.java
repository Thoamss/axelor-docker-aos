package com.axelor.matching.module;

import com.axelor.app.AxelorModule;
import com.axelor.matching.service.MatchingService;
import com.axelor.matching.service.MatchingServiceImpl;

public class MatchingModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(MatchingService.class).to(MatchingServiceImpl.class);
  }
}
