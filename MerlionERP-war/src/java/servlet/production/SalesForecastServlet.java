/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ejb.sessionbeans.interfaces.SalesForecastFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.persistence.SalesForecast;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Randy
 */
@WebServlet(name = "SalesForecastServlet", urlPatterns = {"/SalesForecastServlet"})
public class SalesForecastServlet extends HttpServlet {
    
    @EJB
    SalesForecastFacadeLocal sfFacade;
    
    @EJB
    SalesOrderFacadeLocal soFacade;
    
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
                
            } else if (action.equals("getLastDone")) {
                getLastDone(request, response, out);
                
            } else if (action.equals("checkSFDone")) {
                checkSFDone(request, response, out);
                
            } else if (action.equals("getBase")) {
                getBase(request, response, out);
                
            } else if (action.equals("createSF")) {
                createSF(request, response, out);
                
            } else if (action.equals("getDetails")) {
                getDetails(request, response, out);
                
            } else if (action.equals("updateSF")) {
                updateSF(request, response, out);
                
            } else if (action.equals("getForecast")) {
                getForecast(request, response, out);
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
        ArrayList<SalesForecast> sfList = new ArrayList<SalesForecast>(sfFacade.findAll());
        int totalRecord = sfFacade.count();
        
        for (int i=0; i<sfList.size(); i++) {
            SalesForecast sf = sfList.get(i);
            sf.getProduct().setSalesForecasts(null);
            sf.getProduct().setDeliveryOrderDetails(null);
            sf.setMonth(new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(sf.getMonth())-1]);
        }
        
        String json = gson.toJson(new JsonReturnTable(totalRecord + "", sfList));
        out.println(json);
    }
    
    private void getLastDone(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String product_id = request.getParameter("product_id");
        String lastDone = sfFacade.lastDone(product_id);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<last_done>" + lastDone + "</last_done>");
    }
    
    private void getBase(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String product_id = request.getParameter("product_id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        int baseYear = Integer.parseInt(year)-1;
        
        Calendar cal = Calendar.getInstance();
        cal.set(baseYear, Integer.parseInt(month)-1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Date min = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(1 + "/" + month + "/" + baseYear);
        Date max = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(maxDay + "/" + month + "/" + baseYear);
        
        int base = soFacade.getBaseAmt(Long.parseLong(product_id), min, max);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<base>" + base + "</base>");
    }
    
    private void checkSFDone(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String product_id = request.getParameter("product_id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        boolean result = sfFacade.checkSFDone(Long.parseLong(product_id), month, Integer.parseInt(year));
        System.out.println(result);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<result>" + result + "</result>");
    }
    
    private void createSF(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        boolean error = false;
        
        String product_id = request.getParameter("product_id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String yoy = request.getParameter("yoy");
        String amt_forecasted = request.getParameter("amt_forecasted");
        
        String product_name = sfFacade.create(Long.parseLong(product_id), month, Integer.parseInt(year), Double.parseDouble(yoy), Long.parseLong(amt_forecasted));
        
        content = "Created Sales Forcast for " + product_name + " on " + new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(month)-1] + ", " + year + " successfully.";
        
        json = gson.toJson(new JsonReturnMsg("Create Sales Forecast", content, "info"));
        out.println(json);
    }
    
    private void getDetails(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {       
        String sales_forecast_id = request.getParameter("sales_forecast_id");
        
        SalesForecast sf = sfFacade.find(Long.parseLong(sales_forecast_id));
        
        int baseYear = sf.getYear()-1;
        
        Calendar cal = Calendar.getInstance();
        cal.set(baseYear, Integer.parseInt(sf.getMonth())-1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Date min = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(1 + "/" + sf.getMonth() + "/" + baseYear);
        Date max = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(maxDay + "/" + sf.getMonth() + "/" + baseYear);
        
        int base = soFacade.getBaseAmt(sf.getProduct().getProduct_id(), min, max);
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<detail>");
        response.getWriter().write("<sales_forecast_id>" + sf.getSales_forecast_id() + "</sales_forecast_id>");
        response.getWriter().write("<product_name>" + sf.getProduct().getProduct_name() + "</product_name>");
        response.getWriter().write("<year>" + sf.getYear() + "</year>");
        response.getWriter().write("<month>" + new java.text.DateFormatSymbols().getMonths()[Integer.parseInt(sf.getMonth())-1] + "</month>");
        response.getWriter().write("<base>" + base + "</base>");
        response.getWriter().write("<yoy>" + sf.getYoy_growth() + "</yoy>");
        response.getWriter().write("<amt_forecasted>" + sf.getAmt_forecasted() + "</amt_forecasted>");
        response.getWriter().write("</detail>");
    }
    
    private void updateSF(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String content = "";
        String json = "";
        
        String sales_forecast_id = request.getParameter("sales_forecast_id");
        String yoy = request.getParameter("yoy");
        String amt_forecasted = request.getParameter("amt_forecasted");

        SalesForecast sf = sfFacade.find(Long.parseLong(sales_forecast_id));
        sf.setYoy_growth(Double.parseDouble(yoy));
        sf.setAmt_forecasted(Long.parseLong(amt_forecasted));

        sfFacade.edit(sf);
        content = "Updated Sales Forecast " + sales_forecast_id + " Successfully.";

        json = gson.toJson(new JsonReturnMsg("Update Sales Forecast", content, "info"));
        out.println(json);
    }
    
    private void getForecast(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception {
        
        String product_id = request.getParameter("product_id");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        
        String forecasted = sfFacade.getForecast(Long.parseLong(product_id), month, Integer.parseInt(year));
        
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<forecasted>" + forecasted + "</forecasted>");
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
