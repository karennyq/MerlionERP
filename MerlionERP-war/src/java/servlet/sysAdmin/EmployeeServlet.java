/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.sysAdmin;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.DepartmentFacadeLocal;
import ejb.sessionbeans.interfaces.EmployeeFacadeLocal;
import ejb.sessionbeans.interfaces.RoleFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.Department;
import org.persistence.Employee;
import org.persistence.Role;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Ken
 */
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    EmployeeFacadeLocal employeeFacade;
    @EJB
    DepartmentFacadeLocal deptFacade;
    @EJB
    RoleFacadeLocal roleFacade;
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {

            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("createEmployee")) {
                createEmployee(request, response, out);
            } else if (action.equals("updateEmployee")) {
                updateEmployee(request, response, out);
            } else if (action.equals("addRole")) {
                addRole(request, response, out);

            } else if (action.equals("removeRole")) {
                removeRole(request, response, out);
            } else if (action.equals("addUpdateRole")) {
                addUpdateRole(request, response, out);

            } else if (action.equals("removeUpdateRole")) {
                removeUpdateRole(request, response, out);
            } else if (action.equals("removeUpdateRole")) {
                removeUpdateRole(request, response, out);
            } else if (action.equals("activate")) {
                activate(request, response, out);
            } else if (action.equals("resetPassword")) {
                resetPassword(request, response, out);
            } else if (action.equals("validPassword")) {
                validPassword(request, response, out);
            } else if (action.equals("changePassword")) {
                changePassword(request, response, out);
            }



        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Error", content, "error"));
            out.println(json);
            System.out.print(ex);
        } finally {
            out.close();
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = request.getParameter("content");
        HttpSession session = request.getSession();
        if (content.equals("table")) {

            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "emp_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String nric = (request.getParameter("nric") != null) ? request.getParameter("nric") : "";
            String emp_name = (request.getParameter("emp_name") != null) ? request.getParameter("emp_name") : "";
            String status = (request.getParameter("status") != null) ? request.getParameter("status") : "";

            //-1 for dropdown
            try {
                if (status.equals("0")) {
                    status = "";
                } else {
                    status = (Integer.parseInt(status.trim()) - 1) + "";
                }
            } catch (Exception e) {
                status = "";
            }



            //System.out.println(nric+" "+emp_name+" "+status);

            ArrayList<Employee> empList = new ArrayList<Employee>(employeeFacade.findFilteredEmployee(page, rows, sort, order, nric, emp_name, status));
            int totalReord = employeeFacade.countFilteredEmployee(page, rows, sort, order, nric, emp_name, status);

            ArrayList<Employee> jsonList = new ArrayList<Employee>();

            for (Employee e : empList) {
                Employee je = new Employee();

                je.setEmp_id(e.getEmp_id());
                je.setNric(e.getNric());
                je.setEmp_name(e.getEmp_name());
                je.setEmail(e.getEmail());
                je.setActive_status(e.getActive_status());

                jsonList.add(je);
            }




            String json = gson.toJson(new JsonReturnTable(totalReord + "", jsonList));
            // System.out.println(json);
            out.println(json);
        }
        if (content.equals("details")) {
            long eId = Long.parseLong(request.getParameter("emp_id"));
            Employee e = employeeFacade.find(eId);


            ArrayList<Role> roleList = new ArrayList(e.getRoles());
            ArrayList<Role> jsonList = new ArrayList<Role>();

            for (Role r : roleList) {
                Role jr = new Role();
                Department jd = new Department();
                jd.setDepartment_name(r.getDepartment().getDepartment_name());

                jr.setRole_id(r.getRole_id());
                jr.setDepartment(jd);
                jr.setRole_name(r.getRole_name());

                jsonList.add(jr);
            }

            session.setAttribute("updateEmployeeRoleList" + eId, jsonList);

            Employee je = new Employee();
            je.setEmp_id(e.getEmp_id());
            je.setEmp_name(e.getEmp_name());
            je.setActive_status(e.getActive_status());
            je.setEmail(e.getEmail());
            je.setNric(e.getNric());
            String json = gson.toJson(je);
            System.out.println("!!!!!" + json);
            out.println(json);
        }
        if (content.equals("dropdown")) {
            //String selected= request.getParameter("selected");
            String json = JsonReturnDropDown.populate(Employee.ActiveStatus.values());
            out.println(json);
        }

        if (content.equals("deptDropdown")) {
            ArrayList<Department> list = new ArrayList(deptFacade.findAll());

            ArrayList<JsonReturnDropDown> dList = new ArrayList();
            for (Department d : list) {
                JsonReturnDropDown dd = new JsonReturnDropDown();
                dd.setId(Integer.parseInt(d.getDepartment_id() + ""));
                dd.setText(d.getDepartment_name());
                dList.add(dd);
            }
            String json = gson.toJson(dList);
            out.println(json);

        }
        if (content.equals("roleDropdown")) {
            //System.out.println(request.getParameter("department_id")+"!!!");
            String dptId = (request.getParameter("department_id"));
            ArrayList<JsonReturnDropDown> dList = new ArrayList();
            if (dptId.equals("")) {
            } else {
                //Department d = deptFacade.find(Long.parseLong(dptId));
                ArrayList<Role> roles = new ArrayList(roleFacade.retrieveRoles(dptId));
                System.out.println(roles.size());
                for (Role r : roles) {
                    JsonReturnDropDown dd = new JsonReturnDropDown();
                    dd.setId(Integer.parseInt(r.getRole_id() + ""));
                    dd.setText(r.getRole_name());
                    dList.add(dd);
                }
            }

            System.out.println(dList.size());
            String json = gson.toJson(dList);
            out.println(json);
        } else if (content.equals("roleTable")) {
            if (request.getParameter("reset") != null) {
                String reset = request.getParameter("reset");
                if (reset.equals("true")) {
                    session.setAttribute("createEmployeeRoleList", new ArrayList());
                }
            }

            ArrayList<Role> roleList = (ArrayList<Role>) session.getAttribute("createEmployeeRoleList");
            //System.out.println("=======================" + lineItemList);
            ArrayList<Role> jsonList = new ArrayList<Role>();

            for (Role r : roleList) {
                Role jr = new Role();
                Department jd = new Department();
                jd.setDepartment_name(r.getDepartment().getDepartment_name());

                jr.setRole_id(r.getRole_id());
                jr.setDepartment(jd);
                jr.setRole_name(r.getRole_name());

                jsonList.add(jr);
            }

            int totalRecord = (jsonList != null) ? jsonList.size() : 0;
            //System.out.println("=======================" + totalRecord);

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("updateRoleTable")) {

            String empId = request.getParameter("emp_id");

            if (request.getParameter("reset") != null) {
                String reset = request.getParameter("reset");
                if (reset.equals("true")) {
                    session.setAttribute("updateEmployeeRoleList" + empId, new ArrayList());
                }
            }

            ArrayList<Role> roleList = (ArrayList<Role>) session.getAttribute("updateEmployeeRoleList" + empId);
            //System.out.println("=======================" + lineItemList);
            ArrayList<Role> jsonList = new ArrayList<Role>();

            for (Role r : roleList) {
                Role jr = new Role();
                Department jd = new Department();
                jd.setDepartment_name(r.getDepartment().getDepartment_name());

                jr.setRole_id(r.getRole_id());
                jr.setDepartment(jd);
                jr.setRole_name(r.getRole_name());

                jsonList.add(jr);
            }

            int totalRecord = (jsonList != null) ? jsonList.size() : 0;
            //System.out.println("=======================" + totalRecord);

            String json = gson.toJson(new JsonReturnTable(totalRecord + "", jsonList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("dialog")) {

            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "emp_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String emp_name = (request.getParameter("emp_name") != null) ? request.getParameter("emp_name") : "";
            
            ArrayList<Employee> salesExList = new ArrayList<Employee>(employeeFacade.findFilteredSalesExEmployee(page, rows, sort, order, order, emp_name));

            int totalReord = employeeFacade.countFilteredSalesExEmployee(page, rows, sort, order, emp_name);

            ArrayList<Employee> jsonList = new ArrayList<Employee>();

            for (Employee e : salesExList) {
                Employee je = new Employee();
                    je.setEmp_id(e.getEmp_id());
                    je.setEmp_name(e.getEmp_name());
                    je.setEmail(e.getEmail());
                    jsonList.add(je);
                
            }

            String json = gson.toJson(new JsonReturnTable(jsonList.size() + "", jsonList));
            // System.out.println(json);
            out.println(json);





//            ArrayList<Employee> empList = new ArrayList<Employee>(employeeFacade.salesExecutiveEmployees());
//            ArrayList<Employee> jList = new ArrayList<Employee>();
//            // int totalRecord = employeeFacade.countNotConvertedSalesLead();
//          
//           for (Employee e : empList) {
//                Employee je = new Employee();
//                je.setEmp_id(e.getEmp_id());
//                je.setEmp_name(e.getEmp_name());
//                
//                jList.add(je);
//            }
//             /* for (Employee e : empList) {
////                c.getAccount().setCustomer(null);
//                 e= (Employee) ConvertToJsonObject.convert(e);
//             
//            }*/
//            String json = gson.toJson(new JsonReturnTable(jList.size() + "", jList));
//            out.println(json);
        }
    }

    private void addRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        ArrayList<Role> roleList = new ArrayList<Role>();
        String roleId = request.getParameter("role_id");
        String json;


        if (roleId.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Error", "Please select a role.", "error"));
            out.println(json);

        } else {
            roleList = (ArrayList) session.getAttribute("createEmployeeRoleList");

            boolean exist = false;

            for (Role rr : roleList) {
                if (rr.getRole_id() == Long.parseLong(roleId)) {
                    exist = true;
                }
            }


            if (exist) {
                json = gson.toJson(new JsonReturnMsg("Create Employee", "Role Already Exist In Your List.", "error"));
                out.println(json);
            } else {
                Role r = roleFacade.find(Long.parseLong(roleId));
                roleList.add(r);
                session.setAttribute("createEmployeeRoleList", roleList);
                json = gson.toJson(new JsonReturnMsg("Create Employee", "Role Added Successfully", "info"));
                out.println(json);
            }

        }
    }

    private void removeRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        ArrayList<Role> roleList = new ArrayList<Role>();

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        roleList = (ArrayList) session.getAttribute("createEmployeeRoleList");

        roleList.remove(listIndex);
        session.setAttribute("createEmployeeRoleList", roleList);
        String json = gson.toJson(new JsonReturnMsg("Create Employee", "Role Deleted.", "info"));
        out.println(json);

    }

    private void addUpdateRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        ArrayList<Role> roleList = new ArrayList<Role>();
        String roleId = request.getParameter("role_id");
        String empId = request.getParameter("emp_id");
        String json;


        if (roleId.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Update", "Please select a role.", "error"));
            out.println(json);

        } else {
            roleList = (ArrayList) session.getAttribute("updateEmployeeRoleList" + empId);

            boolean exist = false;

            for (Role rr : roleList) {
                if (rr.getRole_id() == Long.parseLong(roleId)) {
                    exist = true;
                }
            }


            if (exist) {
                json = gson.toJson(new JsonReturnMsg("Update Employee", "Role Already Exist In Your List.", "error"));
                out.println(json);
            } else {
                Role r = roleFacade.find(Long.parseLong(roleId));
                roleList.add(r);
                session.setAttribute("updateEmployeeRoleList" + empId, roleList);
                json = gson.toJson(new JsonReturnMsg("Update Employee", "Role Added Successfully", "info"));
                out.println(json);
            }

        }
    }

    private void removeUpdateRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        HttpSession session = request.getSession();
        ArrayList<Role> roleList = new ArrayList<Role>();
        String empId = request.getParameter("emp_id");

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        roleList = (ArrayList) session.getAttribute("updateEmployeeRoleList" + empId);

        roleList.remove(listIndex);
        session.setAttribute("updateEmployeeRoleList" + empId, roleList);
        String json = gson.toJson(new JsonReturnMsg("Update Employee", "Role Deleted.", "info"));
        out.println(json);

    }

    private void createEmployee(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String emp_name = request.getParameter("emp_name");
        String email = request.getParameter("email");
        String nric = request.getParameter("nric");

        if (employeeFacade.employeeIcExist(nric)) {
            String content = "Employee Nric/Fin already exist.";
            String json = gson.toJson(new JsonReturnMsg("Create Employee", content, "error"));
            out.println(json);
        } else if (employeeFacade.employeeEmailExist(email)) {
            String content = "Employee email already exist.";
            String json = gson.toJson(new JsonReturnMsg("Create Employee", content, "error"));
            out.println(json);
        } else {
            ArrayList<Role> roleList = new ArrayList<Role>();
            roleList = (ArrayList) session.getAttribute("createEmployeeRoleList");
            if (roleList.size() == 0) {
                String content = "Employee At Least Must Has One Role.";
                String json = gson.toJson(new JsonReturnMsg("Create Employee", content, "error"));
                out.println(json);
            } else {
                employeeFacade.creatEmployee(emp_name, email, nric, roleList);
                String content = "Create Employee Successful.";
                String json = gson.toJson(new JsonReturnMsg("Create Employee", content, "info"));
                out.println(json);
            }

        }

    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        HttpSession session = request.getSession();
        String emp_name = request.getParameter("emp_name");
        String email = request.getParameter("email");
        String nric = request.getParameter("nric");
        String empId = request.getParameter("emp_id");

        if (employeeFacade.updateEmployeeIcExist(nric, empId)) {
            String content = "Employee Nric/Fin already exist.";
            String json = gson.toJson(new JsonReturnMsg("Update Employee", content, "error"));
            out.println(json);
        } else if (employeeFacade.updateEmployeeEmailExist(email, empId)) {
            String content = "Employee email already exist.";
            String json = gson.toJson(new JsonReturnMsg("Update Employee", content, "error"));
            out.println(json);
        } else {
            ArrayList<Role> roleList = new ArrayList<Role>();
            roleList = (ArrayList) session.getAttribute("updateEmployeeRoleList" + empId);
            if (roleList.isEmpty()) {
                String content = "Employee must at least have a role.";
                String json = gson.toJson(new JsonReturnMsg("Update Employee", content, "error"));
                out.println(json);
            } else {
                employeeFacade.updateEmployee(empId, emp_name, email, nric, roleList);
                String content = "Updated Employee Successful.";
                String json = gson.toJson(new JsonReturnMsg("Update Employee", content, "info"));
                out.println(json);
            }

        }

    }

    private void activate(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String emp_id = request.getParameter("emp_id");
        String status = request.getParameter("status");

        employeeFacade.updateActiveStatus(emp_id, status);
        String content = "Activate Employee Successful.";
        String json = gson.toJson(new JsonReturnMsg("Activate Employee", content, "info"));
        out.println(json);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void resetPassword(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String emp_id = request.getParameter("emp_id");
        employeeFacade.resetPassword(emp_id);
        String content = "Reset Password Successful.";
        String json = gson.toJson(new JsonReturnMsg("Reset Password", content, "info"));
        out.println(json);

    }

    private void validPassword(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String newPass = request.getParameter("new_password");
        String confirmPass = request.getParameter("confirm_password");
        if (newPass.equals(confirmPass)) {

            out.println("true");

        } else {

            out.println("false");
        }

    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String emp_id = request.getParameter("emp_id");
        String new_password = request.getParameter("new_password");
        String confirm_password = request.getParameter("confirm_password");
        String old_password = request.getParameter("old_password");

        Employee e = employeeFacade.find(Long.parseLong(emp_id));


        if (!confirm_password.equals(new_password)) {
            String content = "New Password and Confirm Password Does not Match.";
            String json = gson.toJson(new JsonReturnMsg("Change Password", content, "error"));
            out.println(json);
        } else if (!employeeFacade.checkPassword(e, old_password)) {
            String content = "Old Password wrong.";
            String json = gson.toJson(new JsonReturnMsg("Change Password", content, "error"));
            out.println(json);
        } else {
            employeeFacade.changePassword(emp_id, new_password);
            String content = "Change Password Successful.";
            String json = gson.toJson(new JsonReturnMsg("Change Password", content, "info"));
            out.println(json);
        }




    }
}
