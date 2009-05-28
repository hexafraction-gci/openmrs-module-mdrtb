package org.openmrs.module.mdrtb.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.openmrs.Order;
import org.openmrs.api.OrderService.ORDER_STATUS;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.HibernateOrderDAO;
import org.openmrs.module.mdrtb.OrderExtension;
import org.openmrs.module.mdrtb.db.OrderExtensionDAO;
import org.openmrs.api.context.Context;

public class HibernateOrderExtensionDAO implements OrderExtensionDAO {

    protected static final Log log = LogFactory.getLog(HibernateOrderDAO.class);
    
    /**
     * Hibernate session factory
     */
    private SessionFactory sessionFactory;
    
    
    public void setSessionFactory(SessionFactory sessionFactory) { 
        this.sessionFactory = sessionFactory;
    }
    
 @SuppressWarnings("unchecked")
 public List<OrderExtension> getOrderExtension(Order o, boolean includeVoided) throws DAOException{
     
     Criteria crit = sessionFactory.getCurrentSession().createCriteria(OrderExtension.class);
     
     if (includeVoided == false)
         crit.add(Expression.eq("voided", false));
     
     if (o != null)
         crit.add(Expression.eq("order", o));
     
     crit.addOrder( org.hibernate.criterion.Order.desc("dateCreated") );
     
     return crit.list();
 }
    
    public void purgeOrderException(OrderExtension oe) throws DAOException{
        sessionFactory.getCurrentSession().delete(oe);
    }
    
    public OrderExtension saveOrderExtension(OrderExtension oe) throws DAOException{ 
       sessionFactory.getCurrentSession().saveOrUpdate(oe);
        return oe;
    }
    
    
    
}
