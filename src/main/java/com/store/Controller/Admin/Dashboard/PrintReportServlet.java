package com.store.Controller.Admin.Dashboard;

import com.store.Service.DashboardService;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.text.ParseException;

@WebServlet("/admin/print_report")
public class PrintReportServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DashboardService dashboardService = new DashboardService(request, response);
        try {
            dashboardService.printReport();
        } catch (ParseException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
