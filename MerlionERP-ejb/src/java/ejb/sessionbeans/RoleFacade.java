/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DepartmentFacadeLocal;
import ejb.sessionbeans.interfaces.RoleFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Department;
import org.persistence.Role;

/**
 *
 * @author alyssia
 */
@Stateless
public class RoleFacade extends AbstractFacade<Role> implements RoleFacadeLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    @EJB
    DepartmentFacadeLocal departmentFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
@Override
    public void refresh(Role r) {
        em.refresh(r);
    }

    public RoleFacade() {
        super(Role.class);
    }
    
    
    @Override
    public String[] getAccessRightByRole(long rid) {
        Query query = em.createQuery("SELECT r.accessRights FROM Role r WHERE r.role_id=?1");
        query.setParameter(1, rid);
       String [] ar= (String[]) query.getSingleResult();
       return ar;
        
    }

    @Override
    public void createRole(String role_name, String department_id, String[] accessRights) {
        Role r = new Role();
        Department d = departmentFacade.find(Long.valueOf(department_id));

        r.setRole_name(role_name);

        r.setDepartment(d);
        r.setAccessRights(accessRights);
        create(r);
    }

    @Override
    public boolean roleNameExist(String roleName, long role_id) {

        Query query = em.createQuery("SELECT r FROM Role r WHERE r.role_name=?1");
        query.setParameter(1, roleName);
        ArrayList<Role> rList = new ArrayList((Collection<Role>) query.getResultList());

        if (rList.isEmpty()) {
            return false;
        } else if (rList.size() == 1) {
            Role r = rList.get(0);
            if (r.getRole_id() == role_id) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
        //boolean r = query.getResultList().isEmpty();

    }

    @Override
    public String updateRoleName(String roleID, String roleName, String[] accessRight) {

        String content = "";
        Role role = em.find(Role.class, Long.valueOf(roleID));

        role.setRole_name(roleName);
        role.setAccessRights(accessRight);
        content = "Role Updated Successfully.";
        edit(role);
        return content;
    }

    @Override
    public Collection retrieveRoles(String departmentID) {
        Query query = em.createQuery("SELECT r FROM Role r WHERE r.department.department_id=?1");
        query.setParameter(1, Long.valueOf(departmentID));
        return (Collection<Role>) query.getResultList();
    }

    @Override
    public String deleteRole(String roleID) {
        Role r = find(Long.valueOf(roleID));

        //Query q = em.createQuery("SELECT r FROM Role r WHERE r.role_id=?1");
        //q.setParameter(1, Long.valueOf(roleID));
        String content = "";

        em.refresh(r);

        if (r.getEmployees() != null) {
            if (!r.getEmployees().isEmpty()) {
                content = "";
                return content;
            }
        }

        content = r.getRole_name() + " has been deleted successfully.";
        r.setDepartment(null);
        edit(r);
        remove(r);
        return content;

    }

    @Override
    public int countFilteredRole(int page, int rows, String sort, String order, String roleId, String roleName) {
        String queryStr = "SELECT r FROM Role r WHERE r.role_id LIKE ?1 AND r.role_name LIKE ?2 ORDER BY r." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + roleId + "%");
        query.setParameter(2, "%" + roleName + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredRole(int page, int rows, String sort, String order, String roleId, String roleName) {
        String queryStr = "SELECT r FROM Role r WHERE r.role_id LIKE ?1 AND r.role_name LIKE ?2 ORDER BY r." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + roleId + "%");
        query.setParameter(2, "%" + roleName + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Role>) query.getResultList();
    }
}
