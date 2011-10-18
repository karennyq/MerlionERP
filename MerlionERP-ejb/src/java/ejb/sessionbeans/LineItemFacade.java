/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.LineItemFacadeLocal;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.LineItem;

/**
 *
 * @author karennyq
 */
@Stateless
public class LineItemFacade extends AbstractFacade<LineItem> implements LineItemFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public LineItemFacade() {
        super(LineItem.class);
    }

    @Override
    public ArrayList findLineItems(Long id) {
        Query query = em.createQuery("SELECT lineItems FROM SalesInquiry s WHERE s.pre_sale_doc_id=?1");
        query.setParameter(1, id);
        return (ArrayList<LineItem>) query.getResultList();
    }
}
