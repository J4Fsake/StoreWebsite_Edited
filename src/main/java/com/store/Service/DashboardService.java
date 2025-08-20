package com.store.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.store.DAO.CategoryDAO;
import com.store.DAO.ProductDAO;
import com.store.DAO.ProfitDAO;
import com.store.DTO.CategoryDTO;
import com.store.DTO.ProductDTO;
import com.store.DTO.ReportDTO;
import com.store.Mapper.CategoryMapper;
import com.store.Mapper.ProductMapper;
import net.sf.jasperreports.engine.JRException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardService {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ProfitDAO profitDAO;
    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    public DashboardService(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.profitDAO = new ProfitDAO();
        this.categoryDAO = new CategoryDAO();
        this.productDAO = new ProductDAO();
    }

    public void report() throws ParseException, IOException {
        String filter_value = request.getParameter("reportBy");
        String category_name = request.getParameter("category_name");
        Date start_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("start_date"));
        Date end_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("end_date"));
        String report_type = request.getParameter("typeReport");
        int product_id = Integer.parseInt(request.getParameter("product_id"));
        int step = Integer.parseInt(request.getParameter("step"));

        if (category_name == null) category_name = "";

        List<ReportDTO> listReports = profitDAO.getReport(filter_value, category_name, start_date, end_date, report_type, product_id, step);

        double total = 0;
        for (ReportDTO reportDTO: listReports){
            total += reportDTO.getTotal();
        }

        double maxTotal = getMaxTotal(listReports);

        double maxY =  Math.ceil(maxTotal / 10) * 10;

        Map<String, Object> result = new HashMap<>();
        result.put("listReports", listReports);
        result.put("total", total);
        result.put("maxY", maxY);

        String json = new Gson().toJson(result);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    public void loadInterface() throws ServletException, IOException {
        List<String> filterList = new ArrayList<>(Arrays.asList("product", "category"));
        List<CategoryDTO> categoryList = CategoryMapper.INSTANCE.toDTOList(categoryDAO.loadParentCategory());
        List<ProductDTO> productList = ProductMapper.INSTANCE.toDTO(productDAO.loadIdAndName());

        request.setAttribute("filterList", filterList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("productList", productList);

        String path = "dashboard.jsp";

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }

    private double getMaxTotal(List<ReportDTO> listReports){
        double max = 100.0;
        for(ReportDTO report:listReports){
            if (report.getTotal() > max){
                max = report.getTotal();
            }
        }

        return max;
    }

    public void printReport() throws ParseException, JRException, IOException {
        String filter_value = request.getParameter("reportBy");
        String category_name = request.getParameter("category_name");
        Date start_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("start_date"));
        Date end_date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("end_date"));
        String report_type = request.getParameter("typeReport");
        int product_id = Integer.parseInt(request.getParameter("product_id"));
        int step = Integer.parseInt(request.getParameter("step"));
        String format = request.getParameter("format");

        JasperReportService jasperReportService = new JasperReportService(response);
        if ("PDF".equalsIgnoreCase(format)) {
            jasperReportService.report("pdf", filter_value, category_name, start_date, end_date, report_type, product_id, step);
        } else if ("XLSX".equalsIgnoreCase(format)) {
            jasperReportService.report("xlsx", filter_value, category_name, start_date, end_date, report_type, product_id, step);
        }
    }
}
