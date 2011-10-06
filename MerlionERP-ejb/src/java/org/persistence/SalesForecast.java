package org.persistence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SALESFORECAST")
public class SalesForecast implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long sales_forecast_id;
	
	@Basic
	private String month;
	
	@ManyToOne
	private Product product;
	
	@Basic
	private Long amt_forecasted;
	
	@Basic
	private Double yoy_growth;
	
	@Basic
	private Integer year;
	
	public void setSales_forecast_id(Long param) {
		this.sales_forecast_id = param;
	}

	public Long getSales_forecast_id() {
		return sales_forecast_id;
	}

	public void setMonth(String param) {
		this.month = param;
	}

	public String getMonth() {
		return month;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}

	public void setAmt_forecasted(Long param) {
		this.amt_forecasted = param;
	}

	public Long getAmt_forecasted() {
		return amt_forecasted;
	}

	public void setYoy_growth(Double param) {
		this.yoy_growth = param;
	}

	public Double getYoy_growth() {
		return yoy_growth;
	}

	public void setYear(Integer param) {
		this.year = param;
	}

	public Integer getYear() {
		return year;
	}

}