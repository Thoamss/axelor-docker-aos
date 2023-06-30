package com.axelor.traitement.module;

import com.axelor.app.AxelorModule;
import com.axelor.traitement.service.TraitementService;
import com.axelor.traitement.service.TraitementServiceImpl;

public class TraitementModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(TraitementService.class).to(TraitementServiceImpl.class);
  }
}
