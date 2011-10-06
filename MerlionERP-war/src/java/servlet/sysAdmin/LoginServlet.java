/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.sysAdmin;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.EmployeeFacadeLocal;
import ejb.sessionbeans.interfaces.RoleFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.Employee;
import org.persistence.Employee.ActiveStatus;
import org.persistence.Role;
import util.GVACCESS;
import util.JsonReturnMsg;

/**
 *
 * @author karennyq
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
    RoleFacadeLocal roleFacade;    
    
    Gson gson = new Gson();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            
            if (action.equals("login")) {
                login(request, response, out);
            } else if (action.equals("logOut")) {
                logOut(request, response, out);
                
            } else if (action.equals("checkSessionTimeOut")) {
                checkSessionTimeOut(request, response, out);
                
            }else if (action.equals("relogin")) {
                relogin(request, response, out);
                
            }else if (action.equals("checkReport")) {
               checkReport(request, response, out);
            }

//            if (action.equals("activate")) {
//                activate();
//            }

        } catch (Exception ex) {
            String content = ex.getMessage();
            String json = gson.toJson(new JsonReturnMsg("Error", content, "error"));
            out.println(json);
            System.out.print(ex);
        } finally {
            out.close();
        }
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("pwd");
        String verificationExist = request.getParameter("verificationExist");
        
        if (verificationExist.equals("true")) { //verfication Code shown
            String c = (String) session.getAttribute(CaptchaServlet.CAPTCHA_KEY);
            String verify = request.getParameter("verify");
            if (!verify.equals(c)) { //verification code is incorrect
                String json = gson.toJson(new JsonReturnMsg("Login", "The code you entered did not match up with the image provided. Please try again with the new image!", "Error"));
                out.print(json);
            } else { //verification code is correct
                checkLoginValues(request, response, username, password, out);
            }
        } else { //verfication Code not shown
            checkLoginValues(request, response, username, password, out);
        }
        
        
    }
    private void relogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("pwd");
       // String verificationExist = request.getParameter("verificationExist");
        
        
            checkLoginValues(request, response, username, password, out);
        
        
        
    }
    
    private void checkSessionTimeOut(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("loginUserId") == null) {
            String json = gson.toJson(new JsonReturnMsg("Session Expired", "Session Expired, Please login again.", "warning"));
            out.print(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Session Expired", "Session exist", "info"));
            out.print(json);
            System.out.println("check!!!!");
        }
        
        
    }
    
    private void logOut(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        System.out.println("Log out!!!!!");
        
        session.setAttribute("loginUserId", null);
        session.invalidate();
        System.out.println("Log out!!!!!");
        String json = gson.toJson(new JsonReturnMsg("logout", "logout", "info"));
        out.print(json);
    }
    
    private void checkLoginValues(HttpServletRequest request, HttpServletResponse response, String username, String password, PrintWriter out) throws Exception {
        Employee emp = employeeFacade.checkExistence(username);
       
        
        if (emp != null) { //employee's username exist
            System.out.println("------active-----" + emp.getActive_status());
            if (emp.getActive_status().equals(ActiveStatus.Active)) {
                boolean passwordCorrect = employeeFacade.checkPassword(emp, password);
                if (passwordCorrect != true) { //password incorrect
                    String json = gson.toJson(new JsonReturnMsg("Login", "Your username and/or password are invalid!", "error"));
                    employeeFacade.updateNoOfAttempts(emp, passwordCorrect);
                    out.print(json);
                } else { //password correct
                    HttpSession session = request.getSession();
                    //save
                    session.setAttribute("loginUserId", emp.getEmp_id());
                    mgmtAccess(request, response, out, emp);
                    String json = gson.toJson(new JsonReturnMsg("Information", "Success", "info"));
                    employeeFacade.updateNoOfAttempts(emp, passwordCorrect);
                    out.print(json);
                }
            } else {
                String json = gson.toJson(new JsonReturnMsg("Warning", "Your account is locked!", "warning"));
                out.print(json);
            }
        } else { //employee's username not exist
            String json = gson.toJson(new JsonReturnMsg("Login", "Your username and/or password are invalid!", "error"));
            out.print(json);
        }
    }
    
    private void mgmtAccess(HttpServletRequest request, HttpServletResponse response, PrintWriter out, Employee e) throws Exception {
        
        
        
        ArrayList<Role> rList = new ArrayList(e.getRoles());
        GVACCESS gva = new GVACCESS();
        HashMap hm = gva.getAccessMap();
        for (Role r : rList) {
          
         
         //Role ar=roleFace.find(ir.getRole_id());
         try{
              r.setAccessRights(roleFacade.getAccessRightByRole(r.getRole_id()));
         }catch(NullPointerException npe){
              r.setAccessRights(new String[] {});
         }
        
            
            for (int i = 0; i < r.getAccessRights().length; i++) {
                
                if ((!hm.get(r.getAccessRights()[i]).equals("")) && (!hm.get(r.getAccessRights()[i]).equals("true"))) {
                    
                    String parent = (String) hm.get(hm.get(r.getAccessRights()[i]));
                    if ((!parent.equals("")) && (!parent.equals("true"))) {
                        hm.remove(parent);
                        hm.put(parent, "true");
                        System.out.print(parent);
                    }
                    
                    hm.remove(hm.get(r.getAccessRights()[i]));
                    hm.put(hm.get(r.getAccessRights()[i]), "true");
                    
                    
                    System.out.print(hm.get(r.getAccessRights()[i]));
                }
                if (!hm.get(r.getAccessRights()[i]).equals("true")) {
                    hm.remove(r.getAccessRights()[i]);
                    hm.put(r.getAccessRights()[i], "true");
                    System.out.print(r.getAccessRights()[i]);
                }
            }
            
        }
        
        HttpSession session = request.getSession();
        //save

        
        session.setAttribute("acessRightsHm", hm);

//        ServletContext context = getServletContext();
//        RequestDispatcher dispatcher = context.getRequestDispatcher("/global/global_layout.jsp");
//        dispatcher.forward(request, response);
    }
    
    
    
//    private HashMap recurtionRights(String val, HashMap hm) {
//        
//        
//        if ((!hm.get(val).equals("")) && (!hm.get(val).equals("true"))) {
//            hm.remove(hm.get(val));
//            hm.put(hm.get(val), "true");
//            System.out.print(hm.get(val));
//        }
//        
//        
//    }
    
    private void checkReport(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        String quotation_id = request.getParameter("pre_sale_doc_id");
        if (session.getAttribute("quotation"+quotation_id) == null) {
            String json = gson.toJson(new JsonReturnMsg("", "", "info"));
            out.print(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("", "done", "info"));
            out.print(json);
            System.out.println("checked Report!!!!");
            session.setAttribute("quotation"+quotation_id,null);
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
