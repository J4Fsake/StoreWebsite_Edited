package com.store.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "profits")
@NamedQueries({
        @NamedQuery(name = "Profit.listAll", query = "SELECT p FROM Profit p")
})
@NamedStoredProcedureQuery(
        name = "Profit.viewReport",
        procedureName = "view_profit_report",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "filter_value", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "category_name", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "start_date", type = java.sql.Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "end_date", type = java.sql.Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "report_type", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "product_id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "step", type = Integer.class)
        }
)
public class Profit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    BigDecimal revenue;

    BigDecimal profit;

    Date date;

    public Profit() {}

    public Profit(Product product, BigDecimal revenue, BigDecimal profit, Date date) {
        this.product = product;
        this.revenue = revenue;
        this.profit = profit;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
