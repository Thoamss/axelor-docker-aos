/**
 * Copyright (c) 2012-2014 Axelor. All Rights Reserved.
 *
 * The contents of this file are subject to the Common Public
 * Attribution License Version 1.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://license.axelor.com/.
 *
 * The License is based on the Mozilla Public License Version 1.1 but
 * Sections 14 and 15 have been added to cover use of software over a
 * computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is part of "Axelor Business Suite", developed by
 * Axelor exclusively.
 *
 * The Original Developer is the Initial Developer. The Initial Developer of
 * the Original Code is Axelor.
 *
 * All portions of the code written by Axelor are
 * Copyright (c) 2012-2014 Axelor. All Rights Reserved.
 */
package com.axelor.apps.production.service;
/**
 * Copyright (c) 2012-2014 Axelor. All Rights Reserved.
 *
 * The contents of this file are subject to the Common Public
 * Attribution License Version 1.0 (the “License”); you may not use
 * this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://license.axelor.com/.
 *
 * The License is based on the Mozilla Public License Version 1.1 but
 * Sections 14 and 15 have been added to cover use of software over a
 * computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an “AS IS”
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is part of "Axelor Business Suite", developed by
 * Axelor exclusively.
 *
 * The Original Developer is the Initial Developer. The Initial Developer of
 * the Original Code is Axelor.
 *
 * All portions of the code written by Axelor are
 * Copyright (c) 2012-2014 Axelor. All Rights Reserved.
 */


import java.math.BigDecimal;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.base.db.IAdministration;
import com.axelor.apps.production.exceptions.IExceptionMessage;
import com.axelor.apps.base.service.administration.SequenceService;
import com.axelor.apps.organisation.db.Project;
import com.axelor.apps.production.db.BillOfMaterial;
import com.axelor.apps.production.db.ManufOrder;
import com.axelor.apps.production.db.ProductionOrder;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.IException;
import com.axelor.meta.service.MetaTranslations;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ProductionOrderService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	private MetaTranslations metaTranslations;
	
	@Inject
	private ManufOrderService manufOrderService;
	
	@Inject
	private SequenceService sequenceService;
	
	@Transactional(rollbackOn = {AxelorException.class, Exception.class})
	public void propagateIsToInvoice(ProductionOrder productionOrder) {

		logger.debug("{} is to invoice ? {}", productionOrder.getProductionOrderSeq(), productionOrder.getIsToInvoice());
		
		boolean isToInvoice = productionOrder.getIsToInvoice();
		
		if(productionOrder.getManufOrderList() != null)  {
			for(ManufOrder manufOrder : productionOrder.getManufOrderList())  {
				
				manufOrder.setIsToInvoice(isToInvoice);
				
				manufOrderService.propagateIsToInvoice(manufOrder);
			}
		}
		
		productionOrder.save();
		
	}

	
	public ProductionOrder createProductionOrder(Project businessProject, boolean isToInvoice) throws AxelorException  {
		
		return new ProductionOrder(
				this.getProductionOrderSeq(), 
				isToInvoice, 
				businessProject);
		
		
	}
	
	
	public String getProductionOrderSeq() throws AxelorException  {
		
		String seq = sequenceService.getSequence(IAdministration.PRODUCTION_ORDER, false);
		
		if(seq == null)  {
			throw new AxelorException(metaTranslations.get(IExceptionMessage.PRODUCTION_ORDER_SEQ), IException.CONFIGURATION_ERROR);
		}
		
		return seq;
	}
	
	
	@Transactional(rollbackOn = {AxelorException.class, Exception.class})
	public ProductionOrder generateProductionOrder(BillOfMaterial billOfMaterial, BigDecimal qtyRequested) throws AxelorException  {
		
		ProductionOrder productionOrder = this.createProductionOrder(null, false);
		
		BigDecimal qty = qtyRequested.divide(billOfMaterial.getQty());
		
		ManufOrder manufOrder = manufOrderService.generateManufOrder(
				qty, 
				ManufOrderService.DEFAULT_PRIORITY, 
				ManufOrderService.IS_TO_INVOICE, 
				billOfMaterial.getCompany(), 
				billOfMaterial, 
				new LocalDateTime());
		
		
		
				
		
		productionOrder.addManufOrderListItem(manufOrder);
		
		return productionOrder.save();
		
	}
	
	
	
	
	
}
