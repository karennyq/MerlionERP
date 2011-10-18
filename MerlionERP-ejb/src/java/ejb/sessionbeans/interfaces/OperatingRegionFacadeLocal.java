/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.OperatingRegion;

@Local
public interface OperatingRegionFacadeLocal {

    void create(OperatingRegion soleDistribution);

    void edit(OperatingRegion soleDistribution);

    void remove(OperatingRegion soleDistribution);

    OperatingRegion find(Object id);

    List<OperatingRegion> findAll();

    List<OperatingRegion> findRange(int[] range);

    int count();

    public Collection findFilteredWholesalers(int page, int rows, String sort, String order, String wholesaler_company);

    public int countFilteredWholesalers(int page, int rows, String sort, String order, String wholesaler_company);

    public int countFilteredSoleDistributors(String f_id,String f_name,String f_region);

    public java.util.Collection findFilteredSoleDistributors(int rows, int page,String f_id,String f_name,String f_region,String sort, String order);


    
    public java.util.Collection findSoleDistributorsByInquier(long inquirer_id);


    public java.lang.String checkSoleDistributionExist(java.lang.String region);


    public boolean addSoleDistribution(java.lang.String region, long custId);


    public boolean removeSoleDistribution(long sdId);
}
