/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.PlannedDemand;

/**
 *
 * @author Randy
 */
@Local
public interface PlannedDemandFacadeLocal {

    void create(PlannedDemand plannedDemand);

    void edit(PlannedDemand plannedDemand);

    void remove(PlannedDemand plannedDemand);

    PlannedDemand find(Object id);

    List<PlannedDemand> findAll();

    List<PlannedDemand> findRange(int[] range);

    int count();
    
}
