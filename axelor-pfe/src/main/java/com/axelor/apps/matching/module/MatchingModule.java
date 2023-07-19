package com.axelor.apps.matching.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.matching.service.MatchingService;
import com.axelor.apps.matching.service.MatchingServiceImpl;

public class MatchingModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(MatchingService.class).to(MatchingServiceImpl.class);
  }
}
