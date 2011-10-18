/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.ProductionLine;

/**
 *
 * @author Randy
 */
@Local
public interface ProductionLineFacadeLocal {

    void create(ProductionLine productionLine);

    void edit(ProductionLine productionLine);

    void remove(ProductionLine productionLine);

    ProductionLine find(Object id);

    List<ProductionLine> findAll();

    List<ProductionLine> findRange(int[] range);

    int count();

    boolean checkPLNameExist(String production_line_name);
    
}
