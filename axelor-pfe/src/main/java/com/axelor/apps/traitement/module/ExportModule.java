package com.axelor.apps.traitement.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.traitement.service.ExportService;
import com.axelor.apps.traitement.service.ExportServiceImpl;

public class ExportModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(ExportService.class).to(ExportServiceImpl.class);
  }
}
