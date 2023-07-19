package com.axelor.apps.traitement.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.traitement.service.TraitementService;
import com.axelor.apps.traitement.service.TraitementServiceImpl;

public class TraitementModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(TraitementService.class).to(TraitementServiceImpl.class);
  }
}
