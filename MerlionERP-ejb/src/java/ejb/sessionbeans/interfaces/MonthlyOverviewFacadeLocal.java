/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;

/**
 *
 * @author Randy
 */
@Local
public interface MonthlyOverviewFacadeLocal {

    void create(MonthlyOverview monthlyOverview);

    void edit(MonthlyOverview monthlyOverview);

    void remove(MonthlyOverview monthlyOverview);

    MonthlyOverview find(Object id);

    List<MonthlyOverview> findAll();

    List<MonthlyOverview> findRange(int[] range);

    int count();
    
    boolean checkPPDone(String month, Integer year);

    public void createMO(MonthlyOverview mo, ArrayList<PlannedDemand> pdList);
}
