/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.persistence.LineItem;

/**
 *
 * @author karennyq
 */
@Local
public interface LineItemFacadeLocal {

    void create(LineItem lineItem);

    void edit(LineItem lineItem);

    void remove(LineItem lineItem);

    LineItem find(Object id);

    List<LineItem> findAll();

    List<LineItem> findRange(int[] range);

    int count();

    public ArrayList findLineItems(Long id);
    
}
