package org.openmrs.module.mdrtb.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.OrderDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mdrtb.OrderExtension;
import org.openmrs.module.mdrtb.OrderExtensionService;
import org.openmrs.module.mdrtb.db.OrderExtensionDAO;

public class OrderExtensionServiceImpl  extends BaseOpenmrsService implements OrderExtensionService {

    protected final Log log = LogFactory.getLog(getClass());

    protected OrderExtensionDAO dao;
    
    public void setOrderExtensionDAO(OrderExtensionDAO dao) {
        this.dao = dao;
    }
    
 public List<OrderExtension> getOrderExtension(Order o, boolean includeVoided) throws APIException{
     return dao.getOrderExtension(o, includeVoided);
 }
    
    public void purgeOrderException(OrderExtension oe) throws APIException{
            dao.purgeOrderException(oe);
    }
    
    public OrderExtension saveOrderExtension(OrderExtension oe) throws APIException{
        dao.saveOrderExtension(oe);
        return oe;
    }
    
    
    
    public  OrderExtension voidOrderExtension(OrderExtension oe) throws APIException{
        oe.setVoided(true);
        oe.setVoidReason(" ");
        oe.setVoidedBy(Context.getAuthenticatedUser());
        saveOrderExtension(oe);
        return oe;
    }
}
