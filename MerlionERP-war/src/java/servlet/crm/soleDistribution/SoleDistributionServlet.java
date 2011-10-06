/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.crm.soleDistribution;

import com.google.gson.Gson;
import ejb.sessionbeans.interfaces.CustomerFacadeLocal;
import ejb.sessionbeans.interfaces.SoleDistributionFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.persistence.Customer;
import org.persistence.LineItem;
import org.persistence.SoleDistribution;
import util.ConvertToJsonObject;
import util.JsonReturnDropDown;
import util.JsonReturnMsg;
import util.JsonReturnTable;

/**
 *
 * @author Ken
 */
@WebServlet(name = "SoleDistributionServlet", urlPatterns = {"/SoleDistributionServlet"})
public class SoleDistributionServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    CustomerFacadeLocal customerFacade;
    @EJB
    SoleDistributionFacadeLocal soleDistributionFacade;
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        try {

            if (action.equals("loadPage")) {
                loadPage(request, response, out);
            } else if (action.equals("addDistribution")) {
                addDistribution(request, response, out);
            } else if (action.equals("removeDistribution")) {
                removeDistribution(request, response, out);
            } else if (action.equals("createSoleDistributor")) {
                createSoleDistributor(request, response, out);
            } else if (action.equals("addUpdateDistribution")) {
                addUpdateDistribution(request, response, out);
            } else if (action.equals("removeUpdateDistribution")) {
                removeUpdateDistribution(request, response, out);
            }

//removeDistribution
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
        if (content.equals("table")) {
            
            //paging
            int page= (request.getParameter("page")!=null)?Integer.parseInt(request.getParameter("page")):1;
            int rows= (request.getParameter("rows")!=null)?Integer.parseInt(request.getParameter("rows")):10;
            String sort=(request.getParameter("sort")!=null)?request.getParameter("sort"):"inquirer_id";
            String order=(request.getParameter("order")!=null)?request.getParameter("order"):"asc";
            
            //filter
            String f_id= (request.getParameter("f_id")!=null)?request.getParameter("f_id"):"";
            String f_name= (request.getParameter("f_name")!=null)?request.getParameter("f_name"):"";
            String f_region= (request.getParameter("f_region")!=null)?request.getParameter("f_region"):"";
            
            
            
            ArrayList<Customer> customerList = new ArrayList(soleDistributionFacade.findFilteredSoleDistributors(rows,page,f_id,f_name,f_region,sort,order));

            for (Customer c : customerList) {
                c = (Customer) ConvertToJsonObject.convert(c);
            }


            System.out.println(customerList.size());
            int totalRecord = soleDistributionFacade.countFilteredSoleDistributors(f_id,f_name,f_region);
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", customerList));
            out.println(json);
        } else if (content.equals("details")) {
            long cId = Long.parseLong(request.getParameter("inquirer_id"));
            Customer cl = customerFacade.find(cId);

            cl = (Customer) ConvertToJsonObject.convert(cl);

            String json = gson.toJson(cl);
            out.println(json);
        } else if (content.equals("dialog")) {
            ArrayList<Customer> customerList = new ArrayList(soleDistributionFacade.findFilteredWholesalers());

            for (Customer c : customerList) {
                c = (Customer) ConvertToJsonObject.convert(c);
            }


            System.out.println(customerList.size());
            int totalRecord = soleDistributionFacade.countFilteredWholesalers();
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", customerList));
            out.println(json);
        } else if (content.equals("dropdown")) {
            //String selected= request.getParameter("selected");
            String json = JsonReturnDropDown.populate(Customer.CustomerType.values());
            out.println(json);
        } else if (content.equals("addDistributorShipTable")) {
            HttpSession session = request.getSession();
            System.out.println("eeeee");
            if (request.getParameter("reset") != null) {

                String reset = request.getParameter("reset");
                if (reset.equals("true")) {

                    session.setAttribute("soleDistributionList", new ArrayList());

                }
            }

            ArrayList soleDisList = (ArrayList<SoleDistribution>) session.getAttribute("soleDistributionList");
            int totalRecord = (soleDisList != null) ? soleDisList.size() : 0;
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", soleDisList));
            //System.out.println(json);
            out.println(json);
        } else if (content.equals("updateDistributorShipTable")) {
            System.out.println(request.getParameter("inquirer_id")+"!!!!!");
            long cId = Long.parseLong(request.getParameter("inquirer_id"));
            ArrayList<SoleDistribution> soleDisList = new ArrayList<SoleDistribution>(soleDistributionFacade.findSoleDistributorsByInquier(cId));
            int totalRecord = (soleDisList != null) ? soleDisList.size() : 0;
            //System.out.println(totalRecord);
            
            for (SoleDistribution sd : soleDisList) {
                sd = (SoleDistribution) ConvertToJsonObject.convert(sd);
            }
            String json = gson.toJson(new JsonReturnTable(totalRecord + "", soleDisList));
            //System.out.println(json);
            out.println(json);

        }
    }

    private void addUpdateDistribution(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        String region = request.getParameter("region");
        String exist = soleDistributionFacade.checkSoleDistributionExist(region);
        
        System.out.println("++++"+exist);
        long cId = Long.parseLong(request.getParameter("inquirer_id"));


        if (exist.equals("")) {
            soleDistributionFacade.addSoleDistribution(region, cId);
            String json = gson.toJson(new JsonReturnMsg("Info", "Sole Distributionship Added.", "info"));
            out.println(json);
        } else {
            String json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "<span style='color: red'>" + exist + "'s</span> Sole Distributionship Already Taken.", "error"));
            out.println(json);
        }


    }

    private void removeUpdateDistribution(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {

        long sId = Long.parseLong(request.getParameter("sole_dis_id"));
        soleDistributionFacade.removeSoleDistribution(sId);

        String json = gson.toJson(new JsonReturnMsg("Create Sole Distriputor", "Distributionship Removed.", "info"));
        out.println(json);
    }

    private void addDistribution(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        ArrayList<SoleDistribution> soleDisList = new ArrayList<SoleDistribution>();
        String region = request.getParameter("region");
        String json;
        if (region == null || region.isEmpty()) {
            json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "Please enter a valid City, State(Province) or Country.", "error"));
            out.println(json);
        } else {
            soleDisList = (ArrayList) session.getAttribute("soleDistributionList");


            boolean sdExist = false;
            for (SoleDistribution sd : soleDisList) {
                if (sd.getRegion().equals(region)) {
                    sdExist = true;
                    json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "Region Already Added.", "error"));
                    out.println(json);
                    break;
                }
            }

            if (!sdExist) {
                String custId = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
                if (custId.equals("")) {
                    json = gson.toJson(new JsonReturnMsg("Create Sole Distributor", "Please Chose A Whole Saler First. ", "error"));
                    out.println(json);
                } else {

                    String existInList = checkDistribution(request, region);

                    if (!existInList.equals("")) {
                        json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "<span style='color: red'>" + existInList + "</span> Already Exist In Your List.", "error"));
                        out.println(json);
                    } else {

                        String exist = soleDistributionFacade.checkSoleDistributionExist(region);
                        if (exist.equals("")) {
                            SoleDistribution sd = new SoleDistribution();
                            sd.setRegion(region);
                            soleDisList.add(sd);
                            session.setAttribute("soleDistributionList", soleDisList);
                            json = gson.toJson(new JsonReturnMsg("Info", "Sole Distributionship Added.", "info"));
                            out.println(json);
                        } else {
                            json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "<span style='color: red'>" + exist + "'s</span> Sole Distributionship Already Taken.", "error"));
                            out.println(json);
                        }

                    }



                }
            }
        }
    }

    private void removeDistribution(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        ArrayList<SoleDistribution> soleDisList = new ArrayList<SoleDistribution>();

        int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        soleDisList = (ArrayList) session.getAttribute("soleDistributionList");
        //SoleDistribution sd = soleDisList.get(listIndex);
        soleDisList.remove(listIndex);
        session.setAttribute("soleDistributionList", soleDisList);
        String json = gson.toJson(new JsonReturnMsg("Create Sole Distriputor", "Distributionship Removed From List.", "info"));
        out.println(json);
    }

    private void createSoleDistributor(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        HttpSession session = request.getSession();
        ArrayList<SoleDistribution> soleDisList = new ArrayList<SoleDistribution>();
        //int listIndex = Integer.parseInt(request.getParameter("listIndex"));
        soleDisList = (ArrayList) session.getAttribute("soleDistributionList");
        String custId = (request.getParameter("inquirer_id") != null) ? request.getParameter("inquirer_id") : "";
        if (custId.equals("")) {
            String json = gson.toJson(new JsonReturnMsg("Create Sole Distributor", "Please Chose A Whole Saler First. ", "error"));
            out.println(json);
        } else {
            Customer c = customerFacade.find(Long.parseLong(custId));
            String exist = "";
            for (SoleDistribution sd : soleDisList) {

                exist = soleDistributionFacade.checkSoleDistributionExist(sd.getRegion());
                if (!exist.equals("")) {
                    String json = gson.toJson(new JsonReturnMsg("Create Sole Distribution", "<span style='color: red'>" + exist + "'s</span> Sole Distributionship Already Taken.", "error"));
                    out.println(json);
                    break;
                }
            }

            if (exist.equals("")) {
                for (SoleDistribution sd : soleDisList) {
                    soleDistributionFacade.addSoleDistribution(sd.getRegion(), c.getInquirer_id());
//                    //check
//                    soleDistributionFacade.create(sd);
//                    sd.setCustomer(c);
//                    soleDistributionFacade.edit(sd);
//
//                    c.getSoleDistribution().add(sd);
//                    customerFacade.edit(c);
                }
                String json = gson.toJson(new JsonReturnMsg("Create Sole Distributor", "Sole Distributor Created.", "info"));
                out.println(json);
            }

        }


    }

    private String checkDistribution(HttpServletRequest request, String region) {
        String textToTokenize = region;
        Scanner scanner = new Scanner(textToTokenize);
        scanner.useDelimiter(",");


        ArrayList regionList = new ArrayList();
        while (scanner.hasNext()) {
            //System.out.println(scanner.next());
            regionList.add(scanner.next());
        }

        String ret = "";

        ret = checkChildRegionExist(request, region);

        return ret;

    }

    private String checkChildRegionExist(HttpServletRequest request, String region) {
        HttpSession session = request.getSession();
        ArrayList<SoleDistribution> soleDisList = new ArrayList<SoleDistribution>();
        soleDisList = (ArrayList) session.getAttribute("soleDistributionList");
        String ret = "";

        for (SoleDistribution sd : soleDisList) {
            if (region.endsWith(sd.getRegion())) {
                ret = sd.getRegion();
                break;
            }
            if (sd.getRegion().endsWith(region)) {
                ret = region;
                break;
            }
        }

        return ret;
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
