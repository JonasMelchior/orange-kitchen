package com.application.views.tests;

import com.application.entity.kitchen.store.DailyRevenue;
import com.application.entity.kitchen.store.PurchasedBrand;
import com.application.service.foodclub.IFoodClubEventService;
import com.application.service.store.IPurchaseService;
import com.application.views.MainLayout;
import com.storedobject.chart.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@PageTitle("Kitchen Statistics")
@Route(value = "kitchen-statistics", layout = MainLayout.class)
public class KitchenStatisticsView extends VerticalLayout {

    @Autowired
    private IFoodClubEventService foodClubEventService;

    @Autowired
    private IPurchaseService purchaseService;

    List<DailyRevenue> dailyRevenues;
    SOChart soChart = new SOChart();
    SOChart soChart1 = new SOChart();
    TextField monthlyRevenueField = new TextField("Total Revenue this month");
    LineChart lineChart;

    public KitchenStatisticsView() {

        monthlyRevenueField.setReadOnly(true);

        HorizontalLayout monthlyRevenueLayout = new HorizontalLayout(new Icon(VaadinIcon.ARROW_RIGHT), monthlyRevenueField);
        monthlyRevenueLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        HorizontalLayout monthlyRevenueLayoutWrapper = new HorizontalLayout(soChart, monthlyRevenueLayout);
        monthlyRevenueLayoutWrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        add(monthlyRevenueLayoutWrapper, soChart1);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @PostConstruct
    public void postConstructor() {
        monthlyRevenueField.setValue(purchaseService.getTotalRevenueCurrentMonth() + " DKK");

        dailyRevenues = purchaseService.getDailyRevenue();
        dailyRevenues.sort(Comparator.comparingInt(DailyRevenue::getDayOfMonth));

        soChart.setSize("900px", "500px");

        // Generating some random values for a LineChart
        Random random = new Random();
        Data xValues = new Data(), yValues = new Data();
        for(DailyRevenue dailyRevenue : dailyRevenues) {
            xValues.add(dailyRevenue.getDayOfMonth());
            yValues.add(dailyRevenue.getRevenue());
        }

        // Line chart is initialized with the generated XY values
        lineChart = new LineChart(xValues, yValues);
        lineChart.setName("Kitchen income this month!");

        // Line chart needs a coordinate system to plot on
        // We need Number-type for both X and Y axes in this case
        XAxis xAxis = new XAxis(DataType.NUMBER);
        xAxis.setName("Day of Month");
        YAxis yAxis = new YAxis(DataType.NUMBER);
        yAxis.setName("Amount purchased for (DKK)");
        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);
        lineChart.plotOn(rc);

        // Add to the chart display area with a simple title
        soChart.add(lineChart);

        List<PurchasedBrand> purchasedBrands = purchaseService.getRevenueBrandBased();

        PieChart pieChart = new PieChart();
        pieChart.setName("Purchased brands");

        CategoryData brandNames = new CategoryData();
        Data purchasedQuantities = new Data();

        for (PurchasedBrand purchasedBrand : purchasedBrands) {
            brandNames.add(purchasedBrand.getBrand());
            purchasedQuantities.add(purchasedBrand.getPurchasedAmount());
        }


        pieChart.setItemNames(brandNames);
        pieChart.setData(purchasedQuantities);

        soChart1.add(pieChart);

    }



}
