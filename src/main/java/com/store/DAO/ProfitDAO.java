package com.store.DAO;

import com.store.DTO.ReportDTO;
import com.store.Entity.Profit;

import java.util.*;

public class ProfitDAO extends JPADAO<Profit> implements GenericDAO<Profit>{
    @Override
    public Profit get(Object id) {
        return super.find(Profit.class, id);
    }

    @Override
    public void delete(Object id) {
        super.delete(Profit.class, id);
    }

    @Override
    public List<Profit> listAll() {
        return super.findWithNamedQuery("Profit.listAll");
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Profit create(Profit profit) {
        return super.create(profit);
    }

    @Override
    public Profit update(Profit profit) {
        return super.update(profit);
    }

    public void saveAll(List<Profit> profits) {
        for (Profit profit : profits) {
            super.create(profit);
        }
    }

    public List<ReportDTO> getReport(String filter_value, String category_name, Date start_date, Date end_date, String report_type, Integer product_id, Integer step) {
        Map<String, Object> params = new HashMap<>();
        params.put("filter_value", filter_value);
        params.put("category_name", category_name);
        params.put("start_date", start_date);
        params.put("end_date", end_date);
        params.put("report_type", report_type);
        params.put("product_id", product_id);
        params.put("step", step);

        List<Object[]> results = callStoredProcedure("Profit.viewReport", params);

        List<ReportDTO> reportList = new ArrayList<>();
        for (Object[] row : results) {
            ReportDTO reportDTO = new ReportDTO();

            reportDTO.setStart_date((Date)row[0]);
            reportDTO.setEnd_date((Date)row[1]);
            reportDTO.setTotal(row[2] != null ? ((Number) row[2]).doubleValue() : 0.0);

            reportList.add(reportDTO);
        }

        return reportList;
    }
}
