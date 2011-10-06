/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DepartmentFacadeLocal;
import ejb.sessionbeans.interfaces.RoleFacadeLocal;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.persistence.Department;

/**
 *
 * @author alyssia
 */
@Stateless
public class DepartmentFacade extends AbstractFacade<Department> implements DepartmentFacadeLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    @EJB
    RoleFacadeLocal roleFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartmentFacade() {
        super(Department.class);
    }

    @Override
    public void createDept(String dept_name) {
        Department d = new Department();
        d.setDepartment_name(dept_name);
        create(d);
    }

    @Override
    public boolean departmentNameExist(String deptName) {
        Query query = em.createQuery("SELECT d FROM Department d WHERE d.department_name=?1");
        query.setParameter(1, deptName);
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }

    @Override
    public Department retrieveDepartmentId(String deptName) {
        try {
            Query query = em.createQuery("SELECT d FROM Department d WHERE d.department_name='" + deptName + "'");
            Department dept = (Department) query.getSingleResult();
            return dept;
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public String updateDeptName(String deptID, String deptName) {
        String content = "";
        Department dept = em.find(Department.class, Long.valueOf(deptID));

        dept.setDepartment_name(deptName);
        content = "Department name has been changed to " + deptName + " successfully.";
        edit(dept);
        return content;
    }

    @Override
    public int countFilteredDepartment(int page, int rows, String sort, String order, String deptId, String deptName) {
        String queryStr = "SELECT d FROM Department d WHERE d.department_id LIKE ?1 AND d.department_name LIKE ?2 ORDER BY d." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + deptId + "%");
        query.setParameter(2, "%" + deptName + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredDepartment(int page, int rows, String sort, String order, String deptId, String deptName) {
        String queryStr = "SELECT d FROM Department d WHERE d.department_id LIKE ?1 AND d.department_name LIKE ?2 ORDER BY d." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + deptId + "%");
        query.setParameter(2, "%" + deptName + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Department>) query.getResultList();
    }

    @Override
    public String deleteDepartment(String deptID) {
        Department d = find(Long.valueOf(deptID));

        //Collection col= roleFacade.retrieveRoles(d.getDepartment_id()+"");
        em.refresh(d);
        //Query q = em.createQuery("SELECT r FROM Role r WHERE r.role_id=?1");
        //q.setParameter(1, Long.valueOf(roleID));
        String content = "";
     
            if (!d.getRoles().isEmpty()) {
                content = "";
                return content;
            }
   
        content = d.getDepartment_name() + " has been deleted successfully.";

        remove(d);
        return content;

    }
}
