/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2023 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.stock.web;

import com.axelor.apps.stock.db.StockLocationLine;
import com.axelor.apps.stock.db.repo.StockLocationLineRepository;
import com.axelor.apps.stock.service.WapHistoryService;
import com.axelor.apps.stock.service.WeightedAveragePriceService;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class StockLocationLineController {

  public void addWapHistoryLine(ActionRequest request, ActionResponse response) {
    try {
      StockLocationLine stockLocationLine = request.getContext().asType(StockLocationLine.class);
      stockLocationLine =
          Beans.get(StockLocationLineRepository.class).find(stockLocationLine.getId());
      Beans.get(WeightedAveragePriceService.class)
          .computeAvgPriceForProduct(stockLocationLine.getProduct());
      Beans.get(WapHistoryService.class).saveWapHistory(stockLocationLine);
      response.setReload(true);
    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
