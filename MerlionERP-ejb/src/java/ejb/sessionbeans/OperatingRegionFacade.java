/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.CustomerFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import ejb.sessionbeans.interfaces.OperatingRegionFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Customer;
import org.persistence.OperatingRegion;

@Stateless
public class OperatingRegionFacade extends AbstractFacade<OperatingRegion> implements OperatingRegionFacadeLocal {

    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    @EJB
    AccountFacadeLocal accountFacade;
    @EJB
    CustomerFacadeLocal customerFacade;
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public OperatingRegionFacade() {
        super(OperatingRegion.class);
    }

    //getOnlyWholesaler
    @Override
    public int countFilteredWholesalers(int page, int rows, String sort, String order, String wholesaler_company) {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.company_name LIKE ?2 AND c.cust_type =?1 AND c.soleDistribution IS EMPTY ORDER BY c." + sort + " " + order);
        query.setParameter(1, Customer.CustomerType.valueOf("Wholesale"));
                query.setParameter(2, "%"+wholesaler_company+"%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredWholesalers(int page, int rows, String sort, String order, String wholesaler_company) {
        //Query query = em.createQuery("SELECT c FROM Customer c");
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.company_name LIKE ?2 AND c.cust_type =?1 AND c.soleDistribution IS EMPTY ORDER BY c." + sort + " " + order);
        query.setParameter(1, Customer.CustomerType.valueOf("Wholesale"));
        query.setParameter(2, "%"+wholesaler_company+"%");
        return (Collection<Customer>) query.getResultList();
    }

    @Override
    public Collection findFilteredSoleDistributors(int rows, int page, String f_id, String f_name, String f_region, String sort, String order) {
        //Query query = em.createQuery("SELECT c FROM Customer c");
        //Query query = em.createQuery("SELECT c FROM Customer c WHERE c.cust_type =?1 AND c.soleDistribution IS NOT EMPTY");

        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.cust_type =?1 AND c.soleDistribution IS NOT EMPTY AND c.inquirer_id LIKE ?2 AND c.company_name LIKE ?3 AND c.inquirer_id IN (SELECT DISTINCT s.customer.inquirer_id FROM SoleDistribution s WHERE s.region LIKE?4 ) ORDER BY c." + sort + " " + order);

        query.setParameter(1, Customer.CustomerType.valueOf("Wholesale"));
        query.setParameter(2, "%" + f_id + "%");
        query.setParameter(3, "%" + f_name + "%");
        query.setParameter(4, "%" + f_region + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Customer>) query.getResultList();
    }

    @Override
    public int countFilteredSoleDistributors(String f_id, String f_name, String f_region) {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.cust_type =?1 AND c.soleDistribution IS NOT EMPTY AND c.inquirer_id LIKE ?2 AND c.company_name LIKE ?3 AND c.inquirer_id IN (SELECT DISTINCT s.customer.inquirer_id FROM SoleDistribution s WHERE s.region LIKE?4)");
        query.setParameter(1, Customer.CustomerType.valueOf("Wholesale"));
        query.setParameter(2, "%" + f_id + "%");
        query.setParameter(3, "%" + f_name + "%");
        query.setParameter(4, "%" + f_region + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findSoleDistributorsByInquier(long inquirer_id) {
        Query query = em.createQuery("SELECT s FROM OperatingRegion s WHERE s.customer.inquirer_id=?1");
        query.setParameter(1, inquirer_id);
        return (Collection<Customer>) query.getResultList();
    }

    @Override
    public boolean addSoleDistribution(String region, long custId) {
        Customer c = customerFacade.find(custId);

        OperatingRegion sd = new OperatingRegion();
        sd.setRegion(region);
        sd.setCustomer(c);
        create(sd);

        c.getOperatingRegions().add(sd);
        customerFacade.edit(c);

        return true;
    }

    @Override
    public boolean removeSoleDistribution(long sdId) {
        OperatingRegion sd = find(sdId);
        remove(sd);
        return true;
    }

    @Override
    public String checkSoleDistributionExist(String region) {
        String textToTokenize = region;
        Scanner scanner = new Scanner(textToTokenize);
        scanner.useDelimiter(",");



        ArrayList regionList = new ArrayList();
        while (scanner.hasNext()) {
            //System.out.println(scanner.next());
            regionList.add(scanner.next());
        }

        String ret = "";

        ArrayList regionListFinal = new ArrayList();

        //ret=checkRegionExist(region);

        String queryStr = (String) regionList.get(regionList.size() - 1);
        ret = checkRegionExist(queryStr);
        System.out.println(queryStr);
        if (!ret.equals("")) {
            ret = queryStr;
        }
        //System.out.println("!!!!!!!!" + ret);

        for (int i = regionList.size() - 1; i > 0; i--) {
            //regionListFinal.add(i-1);
            queryStr = regionList.get(i - 1) + "," + queryStr;
            System.out.println(queryStr);


            String result = checkRegionExist(queryStr);
            if (!result.equals("")) {
                ret = queryStr;
            }
            System.out.println("!!!!!!!!" + ret);

        }

        //check child
        if (ret.equals("")) {
            ret = checkChildRegionExist(region);
        }



        return ret;
    }

    private String checkRegionExist(String region) {


        Query query = em.createQuery("SELECT s FROM SoleDistribution s WHERE s.region =?1");
        query.setParameter(1, region);
        //query.setParameter(2, inquirer_id);

        ArrayList<OperatingRegion> col = new ArrayList((Collection<OperatingRegion>) query.getResultList());
        if (col.size() > 0) {
            OperatingRegion sd = col.get(0);
            return sd.getRegion();
        } else {
            return "";
        }



    }

    private String checkChildRegionExist(String region) {

//        Customer cust=customerFacade.find(inquirer_id);
//        ArrayList<SoleDistribution> sdList = new ArrayList(cust.getSoleDistribution());

//        ArrayList<Long> idList = new ArrayList();
//        for (OperatingRegion sd : sdList) {
//            idList.add(sd.getSole_dis_id());
//        }


        Query query = em.createQuery("SELECT s FROM SoleDistribution s WHERE s.region LIKE ?1 ");
        query.setParameter(1, "%" + region);
        // query.setParameter(2, inquirer_id);
        ArrayList<OperatingRegion> col = new ArrayList((Collection<OperatingRegion>) query.getResultList());
        if (col.size() > 0) {
            OperatingRegion sd = col.get(0);
            return sd.getRegion();
        } else {
            return "";
        }
    }
}
