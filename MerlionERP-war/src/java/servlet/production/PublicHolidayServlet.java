/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ejb.sessionbeans.interfaces.PublicHolidayFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.PublicHoliday;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Randy
 */
@WebServlet(name = "PublicHolidayServlet", urlPatterns = {"/PublicHolidayServlet"})
public class PublicHolidayServlet extends HttpServlet {

    @EJB
    PublicHolidayFacadeLocal phFacade;
    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {
            if (action.equals("loadTable")) {
                loadTable(request, response, out);
                
            } else if (action.equals("validatePHDate")) {
                validatePHDate(request, response, out);
                
            } else if (action.equals("createPH")) {
                createPH(request, response, out);
                
            } else if (action.equals("getDetails")) {
                getDetails(request, response, out);
                
            } else if (action.equals("updatePH")) {
                updatePH(request, response, out);
                
            } else if (action.equals("deletePH")) {
                deletePH(request, response, out);
                
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
    
    private void loadTable(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        ArrayList<PublicHoliday> phList = new ArrayList<PublicHoliday>(phFacade.findAll());
        int totalRecord = phFacade.count();

        String json = gson.toJson(new JsonReturnTable(totalRecord + "", phList));
        out.println(json);
    }
    
    private void getDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        String phID = request.getParameter("pub_holi_id");
        
        PublicHoliday ph = phFacade.find(Long.parseLong(phID));
        
        String json = gson.toJson(ph);
        out.println(json);
    }
    
    private void validatePHDate(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String result = "";
        
        try {
            String phDate = request.getParameter("ph_date");
            
            Date date = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(phDate);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            if (cal.get(Calendar.YEAR) > 9999) {
                result = "Year cannot exceed 9999!";
                
            } else if (date.before(new Date()) || date.equals(new Date())) {
                result = "Please choose a date after today!";
                
            } else if (phFacade.getPH(date) != null) {
                result = "Date is in use! Please choose another date.";
                
            } else {
                result = "true";
            }

        } catch (ParseException pe) {
            result = "Invalid Date format! Correct format (DD/MM/YYYY).";
        }
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<result>" + result + "</result>");
    }
    
    private void createPH(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String phDate = request.getParameter("ph_date");
        String phName = request.getParameter("ph_name");

        PublicHoliday ph = new PublicHoliday();
        ph.setPh_date(new java.text.SimpleDateFormat("dd/MM/yyyy").parse(phDate));
        ph.setPh_name(phName);

        phFacade.create(ph);
        content = "Created Public Holiday on " + phDate + " Successfully.";

        json = gson.toJson(new JsonReturnMsg("Create Public Holiday", content, "info"));
        out.println(json);
    }
    
    private void updatePH(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String phID = request.getParameter("pub_holi_id");
        String phName = request.getParameter("ph_name");    
            
        PublicHoliday ph = phFacade.find(Long.parseLong(phID));
        ph.setPh_name(phName);

        phFacade.edit(ph);
        content = "Updated Public Holiday " + phID + " Successfully.";
        
        json = gson.toJson(new JsonReturnMsg("Update Public Holiday", content, "info"));
        out.println(json);
    }
    
    private void deletePH(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String phID = request.getParameter("pub_holi_id");
        
        try {       
            PublicHoliday ph = phFacade.find(Long.parseLong(phID));
            phFacade.remove(ph);
            content = "Deleted Public Holiday " + phID + " Successfully.";
            json = gson.toJson(new JsonReturnMsg("Public Holiday Management", content, "info"));
            
        } catch (Exception e) {
            content = "Unexpected Error!";
            json = gson.toJson(new JsonReturnMsg("Error", content, "error"));
        }
        
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
}
