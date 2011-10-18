/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.SalesForecast;

/**
 *
 * @author Randy
 */
@Local
public interface SalesForecastFacadeLocal {

    void create(SalesForecast monthlySalesForecast);

    void edit(SalesForecast monthlySalesForecast);

    void remove(SalesForecast monthlySalesForecast);

    SalesForecast find(Object id);

    List<SalesForecast> findAll();

    List<SalesForecast> findRange(int[] range);

    int count();
    
    String lastDone(String product_id);
    
    String create(Long product_id, String month, Integer year, Double yoy, Long forecast);
    
    Boolean checkSFDone(Long product_id, String month, Integer year);

    public String getForecast(Long product_id, String month, Integer year);
    
}
