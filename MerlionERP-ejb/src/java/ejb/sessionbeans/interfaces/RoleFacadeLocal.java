/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.Role;

/**
 *
 * @author alyssia
 */
@Local
public interface RoleFacadeLocal {
   Role find(Object id);
     
   List<Role> findAll();

   List<Role> findRange(int[] range);
    
   //void createRole(String role_name, String department_name);
   
   //boolean roleNameExist(String deptName);
   
   //String updateRoleName(String roleID, String roleName);
   
   Collection retrieveRoles(String departmentID);
   
   String deleteRole(String roleID);
   
   public java.util.Collection findFilteredRole(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String roleID, java.lang.String roleName);

   public int countFilteredRole(int page, int rows, java.lang.String sort, java.lang.String order,java.lang.String roleID, java.lang.String roleName);
   
  int count();

    public void createRole(java.lang.String role_name, java.lang.String department_id, java.lang.String[] accessRights);


    public java.lang.String updateRoleName(java.lang.String roleID, java.lang.String roleName, java.lang.String[] accessRight);

    public boolean roleNameExist(java.lang.String roleName, long role_id);

    public void refresh(org.persistence.Role r);

    public java.lang.String[] getAccessRightByRole(long rid);
}
