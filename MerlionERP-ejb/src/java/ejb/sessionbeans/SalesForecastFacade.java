/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.SalesForecastFacadeLocal;
import java.text.DateFormatSymbols;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Product;
import org.persistence.SalesForecast;

/**
 *
 * @author Randy
 */
@Stateless
public class SalesForecastFacade extends AbstractFacade<SalesForecast> implements SalesForecastFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SalesForecastFacade() {
        super(SalesForecast.class);
    }

    @Override
    public String lastDone(String product_id) {
        String lastDone = "None";
        
        Query query;
        query = em.createQuery("SELECT MAX(sf.year) FROM SalesForecast sf WHERE sf.product.product_id=?1");
        query.setParameter(1, Long.parseLong(product_id));
        
        int year = 0;
        if (query.getSingleResult() != null) {
            year = Integer.parseInt(query.getSingleResult().toString());
        }
        
        if (year != 0) {
            query = em.createQuery("SELECT sf FROM SalesForecast sf WHERE sf.product.product_id=?1 AND sf.year=?2");
            query.setParameter(1, Long.parseLong(product_id));
            query.setParameter(2, year);

            int month = 0;
            for (Object o: query.getResultList()) {
                SalesForecast sf = (SalesForecast)o;
                if (Integer.parseInt(sf.getMonth()) > month) {
                    month = Integer.parseInt(sf.getMonth());
                }
            }
            
            lastDone = new DateFormatSymbols().getMonths()[month-1] + ", " + year;
        }
        
        return lastDone;
    }

    @Override
    public String create(Long product_id, String month, Integer year, Double yoy, Long forecast) {
        Product p = em.find(Product.class, product_id);
        
        SalesForecast sf = new SalesForecast();
        sf.setMonth(month);
        sf.setYear(year);
        sf.setYoy_growth(yoy);
        sf.setAmt_forecasted(forecast);
        sf.setProduct(p);
        
        create(sf);
        
        return p.getProduct_name();
    }

    @Override
    public Boolean checkSFDone(Long product_id, String month, Integer year) {
        Query query = em.createQuery("SELECT sf FROM SalesForecast sf WHERE sf.product.product_id=?1 AND sf.month=?2 AND sf.year=?3");
        query.setParameter(1, product_id);
        query.setParameter(2, month);
        query.setParameter(3, year);
        
        if (query.getResultList().isEmpty()) {
            return false;
        } else {
            return true;
        }   
    }
    
    @Override
    public String getForecast(Long product_id, String month, Integer year) {
        Query query = em.createQuery("SELECT sf FROM SalesForecast sf WHERE sf.product.product_id=?1 AND sf.month=?2 AND sf.year=?3");
        query.setParameter(1, product_id);
        query.setParameter(2, month);
        query.setParameter(3, year);
        
        if (query.getResultList().isEmpty()) {
            return "No Forecast Done";
            
        } else {
            return ((SalesForecast) query.getSingleResult()).getAmt_forecasted().toString();
        }
    }
    
}
