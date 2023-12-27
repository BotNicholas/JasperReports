package Task3.dynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
//import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
//import static net.sf.dynamicreports.report.builder.DynamicReports.col;
//...
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class HolidaysReport {
    private String url;
    private String user;
    private String password;

    private Connection connection;

    public HolidaysReport() {
//        this.url="jdbc:mysql://localhost:3306/holydays?user=root&password=224486";
        this.url="jdbc:mysql://localhost:3306/holydays";
        this.user="root";
        this.password="224486";

        try {
            connection = connect(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public HolidaysReport(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        try {
            connection = connect(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void buildReport(){
        JasperReportBuilder report = DynamicReports.report();

        //styles section
        StyleBuilder headerStyle = stl.style().setLeftPadding(12).setTopPadding(15).setRightPadding(12)/*.setBackgroundColor(Color.BLUE)*/;
        StyleBuilder detailStyle = stl.style().setLeftPadding(12).setRightPadding(12);
        StyleBuilder columnHeaderStyle = stl.style().setLeftPadding(12).setTopPadding(15).setRightPadding(12);
        StyleBuilder summaryStyle = stl.style().setLeftPadding(12).setTopPadding(15).setRightPadding(12).setBottomPadding(15).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        StyleBuilder headerTextStyle = stl.style().bold().setFontSize(20).setHorizontalAlignment(HorizontalAlignment.LEFT)/*.setBackgroundColor(Color.cyan)*/;
        StyleBuilder headerImageStyle = stl.style().setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT)/*.setVerticalImageAlignment(VerticalImageAlignment.MIDDLE)*/;

        StyleBuilder columnStyle = stl.style().setBorder(stl.penThin()).setLeftPadding(2).setBottomPadding(10);
        StyleBuilder columnTitleStyle = stl.style(columnStyle).setBackgroundColor(new Color(235, 243, 252)).setBottomPadding(20).setLeftPadding(3);




        //building report
        report.title(cmp.horizontalList(cmp.text("Holydays").setStyle(headerTextStyle).setFixedHeight(50),
                cmp.image("src/main/resources/img/logo.png").setFixedDimension(280, 60).setStyle(headerImageStyle))).setTitleStyle(headerStyle);


        report.columns(col.column("country", "country", DataTypes.stringType()).setWidth(50),
                       col.column("name", "name", DataTypes.stringType()),
                       col.column("date", "date", DataTypes.stringType()).setWidth(90));
        report.columns().setColumnStyle(columnStyle).setColumnTitleStyle(columnTitleStyle);

        report.pageFooter(cmp.pageXofY());


        report.setTitleStyle(headerStyle);
        report.setDetailStyle(detailStyle);
        report.setColumnHeaderStyle(columnHeaderStyle);
        report.setSummaryStyle(summaryStyle);

        report.summary(getCrosstab(), getBarChart());
        report.summary().setSummaryOnANewPage(true);


        //Setting datasource
        String sql = "select * from holydays";
        report.setDataSource(sql, connection);




        //Output report
        try {
//            report.show();
            String path = "src/main/resources/reports/out/Task3/dynamicHolydaysReport.pdf";
            report.toPdf(new FileOutputStream(path));
            System.out.println("Report has been created!");
        } catch (DRException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    private CrosstabBuilder getCrosstab(){
        CrosstabBuilder crosstab = ctab.crosstab();

        //Setting table cells' width and style
        crosstab.setCellWidth(25)
                .setCellStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                         .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                         .setPadding(2)
                                         .setBorder(stl.penThin()));


        Color totalColor = new Color(134, 177, 247);
        Color headerColor = new Color(189, 213, 252);
        Color headerCellColor = new Color(215, 229, 252);
        StyleBuilder totalStyle = stl.style().setBackgroundColor(totalColor)
                                             .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                             .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                             .setBorder(stl.penThin());
        StyleBuilder headerStyle = stl.style().setBackgroundColor(headerColor)
                                              .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                              .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                              .setBorder(stl.penThin());
        StyleBuilder headerCellStyle = stl.style().setBackgroundColor(headerCellColor)
                                                  .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                                  .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                                  .setBorder(stl.penThin());




        CrosstabColumnGroupBuilder columnGroup = ctab.columnGroup("montf", Integer.class).setTotalHeader("Total"); //column group

        //setting styles for header group
        columnGroup.setTotalHeaderWidth(40)
                   .setHeaderStyle(stl.style(headerStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                      .setBottomPadding(7));
//                   .setTotalHeaderStyle(stl.style(totalStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

        CrosstabRowGroupBuilder rowGroup = ctab.rowGroup("country", String.class).setTotalHeader("Total"); //row group


        //setting styles for row group
        rowGroup.setHeaderWidth(70)
                .setHeaderStyle(stl.style(headerStyle).setLeftPadding(5));
//                .setTotalHeaderStyle(stl.style(totalStyle));


        //Setting table header cell and also setting it's style
        crosstab.headerCell(cmp.text("State/Mese").setStyle(headerCellStyle));

        //setting style to all total components column header + values and row header + values
        crosstab.setGrandTotalStyle(totalStyle);



        crosstab.addColumnGroup(columnGroup);
        crosstab.addRowGroup(rowGroup);
        crosstab.addMeasure(ctab.measure("name", String.class, Calculation.COUNT));

        crosstab.setDataSource("select country, name, cast(substring(date, 4, 2) as signed) as montf from holydays order by montf", connection);


        return crosstab;
    }


    private BarChartBuilder getBarChart(){
        TextColumnBuilder<String> countryColumn = col.column("country", "country", type.stringType());
        TextColumnBuilder<Integer> holidaysColumn = col.column("hcount", "hcount", type.integerType());
        TextColumnBuilder<String> monthsColumn = col.column("montf", "montf", type.stringType());

        BarChartBuilder barChart = cht.barChart();

        barChart.customizers(new ChartCustomizer());

        barChart.setCategory(monthsColumn);
        barChart.series(cht.serie(holidaysColumn).setSeries(countryColumn));
        //То есть мы говорим, что - да, значения будут holidaysColumn, но сами данные - это сами страны (countryColumn)
        //Грубо говоря мы говорим: "сами столбики - это страны, а их высота - количество праздников в этих странах + мы группируем по месяцам в Category-ях"

        barChart.setDataSource("select country, cast(substring(date, 4, 2) as signed) as montf, count(name) as hcount from holydays group by montf, country", connection);

        barChart.setStyle(stl.style().setTopPadding(100));
        barChart.setFixedHeight(250);
        barChart.setShowLabels(true);

        return barChart;
    }


    private Connection connect(String url, String user, String password) throws SQLException {
            return DriverManager.getConnection(url, user, password);
    }
}



//To show the numbers we should use this method
class ChartCustomizer implements DRIChartCustomizer, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public void customize(JFreeChart chart, ReportParameters reportParameters) {
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
    }
}

