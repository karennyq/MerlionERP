package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Product;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(SalesForecast.class)
public class SalesForecast_ { 

    public static volatile SingularAttribute<SalesForecast, Product> product;
    public static volatile SingularAttribute<SalesForecast, Double> yoy_growth;
    public static volatile SingularAttribute<SalesForecast, String> month;
    public static volatile SingularAttribute<SalesForecast, Long> amt_forecasted;
    public static volatile SingularAttribute<SalesForecast, Long> sales_forecast_id;
    public static volatile SingularAttribute<SalesForecast, Integer> year;

}