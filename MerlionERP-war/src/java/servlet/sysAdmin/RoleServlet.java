/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.sysAdmin;

import com.google.gson.Gson;
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
import org.persistence.Role;
import util.ConvertToJsonObject;
import util.GVACCESS;
import util.GVAccessRight;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author alyssia
 */
@WebServlet(name = "RoleServlet", urlPatterns = {"/RoleServlet"})
public class RoleServlet extends HttpServlet {

    @EJB
    RoleFacadeLocal roleFacade;
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
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("createRole")) {
                createRole(request, response, out);
            } else if (action.equals("updateRole")) {
                updateRole(request, response, out);
            } else if (action.equals("retrieveRoles")) {
                retrieveRoles(request, response, out);
            } else if (action.equals("deleteRole")) {
                deleteRole(request, response, out);
            }

        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Role Management", content, "error"));
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
            String sort = (request.getParameter("sort") != null) ? request.getParameter("sort") : "role_id";
            String order = (request.getParameter("order") != null) ? request.getParameter("order") : "asc";

            //filter
            String roleID = (request.getParameter("role_id") != null) ? request.getParameter("role_id") : "";
            String roleName = (request.getParameter("role_name") != null) ? request.getParameter("role_name") : "";

            ArrayList<Role> roleList = new ArrayList<Role>(roleFacade.findFilteredRole(page, rows, sort, order, roleID, roleName));

            for (Role r : roleList) {
                r = (Role) ConvertToJsonObject.convert(r);
            }

            int totalRecord = roleFacade.countFilteredRole(page, rows, sort, order, roleID, roleName);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", roleList));
            //System.out.println(json);
            out.println(json);
        } else if (content.equals("details")) {
            long roleID = Long.parseLong(request.getParameter("role_id"));
            Role r = roleFacade.find(roleID);
            r = (Role) ConvertToJsonObject.convert(r);
            String json = gson.toJson(r);
            // System.out.println(json);
            out.println(json);
        } else if (content.equals("accessRights")) {
            ArrayList list = new GVACCESS().getRights();
            String json = gson.toJson(new JsonReturnTable(list.size() + "", list));
            out.println(json);
        } else if (content.equals("updateAccessRights")) {
            //System.out.println("!!!!!");
            long roleID = Long.parseLong(request.getParameter("role_id"));
            Role r = roleFacade.find(roleID);
            //System.out.println(r.getAccessRights().length);

            String[] ar = r.getAccessRights();
            ArrayList<GVAccessRight> list = new GVACCESS().getRights();

            for (String i : ar) {
                //System.out.println(i);
                for (GVAccessRight arm : list) {
                    //System.out.println(arm.getId());
                    if (arm.getId().trim().equals(i.trim())) {
                        arm.setChecked(true);
                    }
                }


            }

            String json = gson.toJson(new JsonReturnTable(list.size() + "", list));
            System.out.println(json);
            out.println(json);
        }
    }

    private void createRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String role_name = request.getParameter("role_name");
        String department_id = request.getParameter("department_id");

        String[] accessRight = (request.getParameterValues("arCheckBox") == null) ? null : request.getParameterValues("arCheckBox");

        //System.out.println(accessRight.length);

        String content = "";

        if (roleFacade.roleNameExist(role_name, -1)) {
            content = "Role name already exist.";
            String json = gson.toJson(new JsonReturnMsg("Create Role", content, "error"));
            out.println(json);
        } else if (accessRight == null) {
            content = "Please Select At Least One Access Right.";
            String json = gson.toJson(new JsonReturnMsg("Create Role", content, "error"));
            out.println(json);
        } else if (accessRight.length == 0) {
            content = "Please Select At Least One Access Right.";
            String json = gson.toJson(new JsonReturnMsg("Create Role", content, "error"));
            out.println(json);
        } else {
            roleFacade.createRole(role_name, department_id, accessRight);
            content = "Create Role Successful.";
            String json = gson.toJson(new JsonReturnMsg("Create Role", content, "info"));
            out.println(json);
        }

    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String action = request.getParameter("confirm");
        String role_id = request.getParameter("role_id");
        String[] accessRight = (request.getParameterValues("arCheckBox") == null) ? null : request.getParameterValues("arCheckBox");

        String name = "";
        String content = "";
        String json = "";

        if (action.equals("updateRole")) {
            name = request.getParameter("role_name");
            if (roleFacade.roleNameExist(name, Long.parseLong(role_id))) {
                content = "Role name already exist.";
                json = gson.toJson(new JsonReturnMsg("Update Role", content, "error"));
            } else if (accessRight == null) {

                content = "Please Select At Least One Access Right.";
                json = gson.toJson(new JsonReturnMsg("Update", content, "error"));
                System.out.println(json);
                //out.println(json);
            } else {

                content = roleFacade.updateRoleName(role_id, name, accessRight);
                json = gson.toJson(new JsonReturnMsg("Update Role", content, "info"));
            }
        }
        out.println(json);
    }

    private void retrieveRoles(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String deptId = request.getParameter("department_id");

        ArrayList<Role> roleList = new ArrayList<Role>(roleFacade.retrieveRoles(deptId));

        for (Role r : roleList) {
            r = (Role) ConvertToJsonObject.convert(r);
        }

        String json = gson.toJson(new JsonReturnTable(roleList.size() + "", roleList));
        //System.out.println(json);
        out.println(json);
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {

        String roleId = request.getParameter("role_id");
        String content = roleFacade.deleteRole(roleId);

        if (content.equals("")) {
            content="Role alredy assigned to employee, delete failed.";
            String json = gson.toJson(new JsonReturnMsg("Delete Role", content, "error"));
            //System.out.println(json);
            out.println(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Delete Role", content, "info"));
            //System.out.println(json);
            out.println(json);

        }



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
