/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.RawMaterialDetail;

/**
 *
 * @author alyssia
 */
@Local
public interface RawMaterialDetailFacadeLocal {
    void create(RawMaterialDetail rawMaterialDetail);

    void edit(RawMaterialDetail rawMaterialDetail);

    void remove(RawMaterialDetail rawMaterialDetail);

    RawMaterialDetail find(Object id);

    List<RawMaterialDetail> findAll();

    List<RawMaterialDetail> findRange(int[] range);

    int count();
}
