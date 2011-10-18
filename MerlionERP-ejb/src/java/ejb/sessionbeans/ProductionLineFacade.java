/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.ProductionLineFacadeLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.ProductionLine;

/**
 *
 * @author Randy
 */
@Stateless
public class ProductionLineFacade extends AbstractFacade<ProductionLine> implements ProductionLineFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductionLineFacade() {
        super(ProductionLine.class);
    }

    @Override
    public boolean checkPLNameExist(String production_line_name) {
        Query query = em.createQuery("SELECT pl FROM ProductionLine pl WHERE pl.production_line_name=?1");
        query.setParameter(1, production_line_name);

        if (!query.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
}
