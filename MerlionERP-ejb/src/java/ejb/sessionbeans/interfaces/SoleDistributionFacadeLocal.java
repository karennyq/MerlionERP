/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.SoleDistribution;

@Local
public interface SoleDistributionFacadeLocal {

    void create(SoleDistribution soleDistribution);

    void edit(SoleDistribution soleDistribution);

    void remove(SoleDistribution soleDistribution);

    SoleDistribution find(Object id);

    List<SoleDistribution> findAll();

    List<SoleDistribution> findRange(int[] range);

    int count();

    public Collection findFilteredWholesalers();

    public int countFilteredWholesalers();

    public int countFilteredSoleDistributors(String f_id,String f_name,String f_region);

    public java.util.Collection findFilteredSoleDistributors(int rows, int page,String f_id,String f_name,String f_region,String sort, String order);


    
    public java.util.Collection findSoleDistributorsByInquier(long inquirer_id);


    public java.lang.String checkSoleDistributionExist(java.lang.String region);


    public boolean addSoleDistribution(java.lang.String region, long custId);


    public boolean removeSoleDistribution(long sdId);
}
