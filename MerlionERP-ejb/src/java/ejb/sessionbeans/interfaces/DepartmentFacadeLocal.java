/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.Department;

/**
 *
 * @author alyssia
 */
@Local
public interface DepartmentFacadeLocal {

    Department find(Object id);

    List<Department> findAll();

    List<Department> findRange(int[] range);

    Department retrieveDepartmentId(String deptName);

    void createDept(String dept_name);

    boolean departmentNameExist(String deptName);

    String updateDeptName(String deptID, String deptName);

    public java.util.Collection findFilteredDepartment(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String deptID, java.lang.String deptName);

    public int countFilteredDepartment(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String deptID, java.lang.String deptName);

    int count();

    public java.lang.String deleteDepartment(java.lang.String deptID);
}
