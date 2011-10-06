/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.persistence.PublicHoliday;

/**
 *
 * @author Randy
 */
@Local
public interface PublicHolidayFacadeLocal {

    //
    void create(PublicHoliday publicHoliday);

    void edit(PublicHoliday publicHoliday);

    void remove(PublicHoliday publicHoliday);

    PublicHoliday find(Object id);

    List<PublicHoliday> findAll();

    List<PublicHoliday> findRange(int[] range);

    int count();
    
    boolean findUsedDate(Date date);

    public List<PublicHoliday> getPHInPeriod(Date min, Date max);
    
}
