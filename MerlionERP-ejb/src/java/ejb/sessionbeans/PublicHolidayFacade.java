/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.PublicHolidayFacadeLocal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.persistence.PublicHoliday;

/**
 *
 * @author Randy
 */
@Stateless
public class PublicHolidayFacade extends AbstractFacade<PublicHoliday> implements PublicHolidayFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicHolidayFacade() {
        super(PublicHoliday.class);
    }

    /*
    @Override
    public boolean findUsedDate(Date date) {
        Query query = em.createQuery("SELECT p FROM PublicHoliday p WHERE p.ph_date=?1");
        query.setParameter(1, date);

        if (!query.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }*/
    
    @Override
    public PublicHoliday getPH(Date date) {
        Query query = em.createQuery("SELECT p FROM PublicHoliday p WHERE p.ph_date=?1");
        query.setParameter(1, date);
        
        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            return (PublicHoliday) query.getSingleResult();
        }
        
    }
    
    @Override
    public List<PublicHoliday> getPHInPeriod(Date min, Date max) {
        Query query = em.createQuery("SELECT ph FROM PublicHoliday ph WHERE ph.ph_date BETWEEN ?1 AND ?2");
        query.setParameter(1, min);
        query.setParameter(2, max);
        
        return query.getResultList();
    }

    
}
