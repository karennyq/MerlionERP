/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.sysAdmin;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.DepartmentFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.Department;
import org.persistence.Role;
import util.ConvertToJsonObject;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "DepartmentServlet", urlPatterns = {"/DepartmentServlet"})
public class DepartmentServlet extends HttpServlet {

    @EJB
    DepartmentFacadeLocal departmentFacadeLocal;
    Gson gson = new Gson();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("createDept")) {
                createDept(request, response, out);
            } else if (action.equals("update")) {
                update(request, response, out);
            } else if (action.equals("getDepts")) {
                getDepts(request, response, out);
            }else if (action.equals("deleteDepartment")) {
                deleteDepartment(request, response, out);
            }
        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Department Management", content, "error"));
            out.println(json);
            System.out.print(ex);
        } finally {
            out.close();
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String content = request.getParameter("content");

        if (content.equals("table")) {

            //paging
            int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
            int rows = (request.getParameter("rows") != null) ? Integer.parseInt(request.getParameter("rows")) : 10;
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "department_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String deptID = (request.getParameter("department_id") != null) ? request.getParameter("department_id") : "";
            String deptName = (request.getParameter("department_name") != null) ? request.getParameter("department_name") : "";

            ArrayList<Department> deptList = new ArrayList<Department>(departmentFacadeLocal.findFilteredDepartment(page, rows, sort, order, deptID, deptName));

            for (Department d : deptList) {
                //d.setRoles(null);
                d = (Department) ConvertToJsonObject.convert(d);
            }

            int totalRecord = departmentFacadeLocal.countFilteredDepartment(page, rows, sort, order, deptID, deptName);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", deptList));
            System.out.println(json);
            out.println(json);
        } else if (content.equals("roleDetails")) {
            long dId = Long.parseLong(request.getParameter("department_id"));

            Department d = departmentFacadeLocal.find(dId);

            ArrayList<Role> roles = new ArrayList<Role>(d.getRoles());

            for (Role r : roles) {
                //r.getDepartment().setRoles(null);
                r = (Role) ConvertToJsonObject.convert(r);
            }

            String json = gson.toJson(new JsonReturnTable(roles.size() + "", roles));
            out.println(json);
        } else if (content.equals("details")) {
            long deptId = Long.parseLong(request.getParameter("department_id"));
            Department d = departmentFacadeLocal.find(deptId);
            d = (Department) ConvertToJsonObject.convert(d);
            String json = gson.toJson(d);
            System.out.println(json);
            out.println(json);
        }
    }

    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String deptId = request.getParameter("department_id");
        String content = departmentFacadeLocal.deleteDepartment(deptId);

        if (content.equals("")) {
            content = "Role alredy assigned to this department, delete failed.";
            String json = gson.toJson(new JsonReturnMsg("Delete Role", content, "error"));
            //System.out.println(json);
            out.println(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Delete Role", content, "info"));
            //System.out.println(json);
            out.println(json);

        }

    }

    private void createDept(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String dept_name = request.getParameter("department_name");

        if (departmentFacadeLocal.departmentNameExist(dept_name)) {
            String content = "Department name already exist.";
            String json = gson.toJson(new JsonReturnMsg("Create Department", content, "error"));
            out.println(json);
        } else {
            departmentFacadeLocal.createDept(dept_name);
            String content = "Create Department Successful.";
            String json = gson.toJson(new JsonReturnMsg("Create Department", content, "info"));
            out.println(json);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String action = request.getParameter("confirm");
        String dept_id = request.getParameter("department_id");
        String row_id = request.getParameter("row_id");
        String name = "";
        String content = "";
        String json = "";

        if (action.equals("updateDept")) {
            name = request.getParameter("department_name");
            if (departmentFacadeLocal.departmentNameExist(name)) {
                content = "Department name already exist.";
                json = gson.toJson(new JsonReturnMsg("Update Department", content, "error"));
            } else {
                content = departmentFacadeLocal.updateDeptName(dept_id, name);
                json = gson.toJson(new JsonReturnMsg("Update Department", content, "info"));
            }
        }
        out.println(json);
    }

    private void getDepts(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        ArrayList<Department> dList = new ArrayList<Department>(departmentFacadeLocal.findAll());

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<departmentlist>");

        for (Department d : dList) {
            response.getWriter().write("<department>");
            response.getWriter().write("<department_id>" + d.getDepartment_id() + "</department_id>");
            response.getWriter().write("<department_name>" + d.getDepartment_name() + "</department_name>");
            response.getWriter().write("</department>");
        }
        response.getWriter().write("</departmentlist>");
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
}
