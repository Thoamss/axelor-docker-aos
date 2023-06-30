package com.axelor.traitement.module;

import com.axelor.app.AxelorModule;
import com.axelor.traitement.service.ExportService;
import com.axelor.traitement.service.ExportServiceImpl;

public class ExportModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(ExportService.class).to(ExportServiceImpl.class);
  }
}
