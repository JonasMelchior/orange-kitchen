package com.application.views.statistics;

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
import java.util.*;

@PageTitle("Kitchen Statistics")
@Route(value = "kitchen-statistics", layout = MainLayout.class)
public class KitchenStatisticsView extends VerticalLayout {

    @Autowired
    private IFoodClubEventService foodClubEventService;

    @Autowired
    private IPurchaseService purchaseService;

    List<DailyRevenue> dailyRevenues;
    List<DailyRevenue> dailyRevenuesBrandBased;
    SOChart soChart = new SOChart();
    SOChart soChart1 = new SOChart();
    SOChart soChart2 = new SOChart();
    TextField monthlyRevenueField = new TextField("Total Revenue this month");
    LineChart lineChart;
    List<Chart> charts = new ArrayList<>();

    public KitchenStatisticsView() {


        monthlyRevenueField.setReadOnly(true);

        HorizontalLayout monthlyRevenueLayout = new HorizontalLayout(new Icon(VaadinIcon.ARROW_RIGHT), monthlyRevenueField);
        monthlyRevenueLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        HorizontalLayout monthlyRevenueLayoutWrapper = new HorizontalLayout(soChart, monthlyRevenueLayout);
        monthlyRevenueLayoutWrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        add(monthlyRevenueLayoutWrapper, soChart2, soChart1);
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

        List<PurchasedBrand> purchasedBrands = purchaseService.getQuantityPurchasedBrandBased();

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

        dailyRevenuesBrandBased = purchaseService.getDailyRevenueBrandBased();
        dailyRevenuesBrandBased.sort(Comparator.comparingInt(DailyRevenue::getDayOfMonth));
        List<String> relevantDates = new ArrayList<>();
        for (DailyRevenue dailyRevenue : dailyRevenuesBrandBased) {
            relevantDates.add(String.valueOf(dailyRevenue.getDayOfMonth()));
        }

        DataMatrix dailyRevenueBrandBasedData = new DataMatrix("Total purchased amount (DKK)");
        dailyRevenueBrandBasedData.setColumnDataName("Revenue");
        dailyRevenueBrandBasedData.setRowDataName("Day of month");
        CategoryData categoryDataColumn = new CategoryData();
        CategoryData categoryDataRow= new CategoryData();
        categoryDataColumn.addAll(getBrands());
        categoryDataRow.addAll(relevantDates);
        dailyRevenueBrandBasedData.setColumnNames(categoryDataColumn);
        dailyRevenueBrandBasedData.setRowNames(categoryDataRow);



        for (DailyRevenue dailyRevenue : dailyRevenuesBrandBased) {
            List<Double> currentDailyRevenueBrandBased = new ArrayList<>();
            Iterator<String> itr=dailyRevenue.getBrandRevenues().keySet().iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                currentDailyRevenueBrandBased.add(dailyRevenue.getBrandRevenues().get(key));
            }
            Data tmpData = new Data();
            tmpData.addAll(currentDailyRevenueBrandBased);
            dailyRevenueBrandBasedData.addRow(tmpData);
        }

        XAxis xAxisBc = new XAxis(DataType.CATEGORY);
        xAxisBc.setName(dailyRevenueBrandBasedData.getRowDataName());
        YAxis yAxisBc = new YAxis(DataType.NUMBER);
        yAxisBc.setName(dailyRevenueBrandBasedData.getName());

        RectangularCoordinate rcBc = new RectangularCoordinate();
        rcBc.addAxis(xAxisBc, yAxisBc);

        BarChart bc;

        // Create a bar chart for each row
        for (int i = 0; i < dailyRevenueBrandBasedData.getColumnCount(); i++) {
            // Bar chart for the row
            bc = new BarChart(dailyRevenueBrandBasedData.getRowNames(), dailyRevenueBrandBasedData.getColumn(i));
            bc.setName(dailyRevenueBrandBasedData.getColumnName(i));
            // Plot that to the coordinate system defined
            bc.plotOn(rcBc);
            // Add that to the chart list
            charts.add(bc);
        }
        charts.forEach(soChart2::add);

        List<List<DailyRevenue>> list = purchaseService.getDailyRevenueRoomNumberBased();

        for (int i = 0; i < list.size(); i++) {
            for (DailyRevenue dailyRevenue : list.get(i)) {
                System.out.println("Day of month: " + dailyRevenue.getDayOfMonth());
                System.out.println("Purchased amount (DKK): " + dailyRevenue.getRevenue());
            }
        }
    }

    private List<String> getBrands() {
        List<String> brands = new ArrayList<>();

        brands.add("Carlsberg");
        brands.add("Heineken");
        brands.add("Tuborg");
        brands.add("Royal");
        brands.add("Coke");
        brands.add("Wine");
        brands.add("Vodka");
        brands.add("Fanta");
        brands.add("Pepsi");
        brands.add("Egekilde");
        brands.add("Faxe Kondi");
        brands.add("Other");

        brands.sort(String::compareTo);

        return brands;
    }


}
