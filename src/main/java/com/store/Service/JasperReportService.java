package com.store.Service;

import Constant.Iconstant;
import com.store.DAO.ProfitDAO;
import com.store.DTO.ReportDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportService {
    private final HttpServletResponse response;
    private final String path = Iconstant.FILE_TEMP_PATH;
    private final ProfitDAO profitDAO;

    public JasperReportService(HttpServletResponse response) {
        this.response = response;
        this.profitDAO = new ProfitDAO();
    }

    public void report(String format, String filter_value, String category_name, Date start_date, Date end_date, String report_type, Integer product_id, Integer step) throws JRException, IOException {
        if(category_name == null) category_name = "";

        List<ReportDTO> reportList = profitDAO.getReport(filter_value, category_name, start_date, end_date, report_type, product_id, step);

        // Tải file jrxml
        File file = ResourceUtils.getFile("classpath:revenueReports.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Tạo dữ liệu cho báo cáo
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportList);

        // Tham số truyền vào báo cáo
        Map<String, Object> params = new HashMap<>();

        // Lấp đầy báo cáo
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        // Lấy ngày và giờ hiện tại để thêm vào tên tệp
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateTimeFormat.format(new Date());

        String fileName = getReportName(report_type, filter_value, product_id, currentDateTime, category_name);
        String outputPath = "";

        if ("pdf".equalsIgnoreCase(format)) {
            outputPath = path + "\\" + fileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            giveFileToBrowser(fileName + ".pdf");
        } else {
            outputPath = path + "\\" + fileName + ".xlsx";
            JRXlsxExporter exporter = getJrXlsxExporter(jasperPrint, outputPath);
            exporter.exportReport();
            giveFileToBrowser(fileName + ".xlsx");
        }
    }

    private static String getReportName(String reportType, String filterType, int productId, String date, String category_name) {
        String reportName;

        if ("product".equalsIgnoreCase(filterType)) {
            if (productId == 0) {
                reportName = reportType + "_report_for_all_product_at_" + date;
            } else {
                reportName = reportType + "_report_for_product_" + "_with_id=" + productId + "_at_" + date;
            }
        } else if ("category".equalsIgnoreCase(filterType)) {
            reportName = reportType + "_report_for_" + category_name + "_category_at_" + date;
        } else {
            reportName = reportType + "_at_" + date;
        }

        return reportName;
    }

    private static JRXlsxExporter getJrXlsxExporter(JasperPrint jasperPrint, String outputPath) {
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

        // Cấu hình xuất file Excel
        SimpleXlsxReportConfiguration xlsxConfig = new SimpleXlsxReportConfiguration();
        xlsxConfig.setDetectCellType(true); // Phát hiện kiểu dữ liệu trong ô
        xlsxConfig.setCollapseRowSpan(false); // Giữ nguyên định dạng row span
        xlsxConfig.setOnePagePerSheet(false); // Không ép mỗi trang thành 1 sheet

        exporter.setConfiguration(xlsxConfig);
        return exporter;
    }

    private void giveFileToBrowser(String fileName) throws IOException {
        // Thiết lập response để trình duyệt tải file về
        response.setContentType("application/pdf");  // Dùng đúng loại content type, ví dụ pdf hoặc excel
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Mở file cần gửi
        FileInputStream fileInputStream = new FileInputStream(path + "\\" + fileName);

        // Sử dụng OutputStream để ghi dữ liệu từ file vào response
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[4096]; // Dùng buffer để đọc file theo khối
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        // Đóng các stream
        fileInputStream.close();
        outputStream.close();

        deleteFile(path + "\\" + fileName);
    }

    private void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
