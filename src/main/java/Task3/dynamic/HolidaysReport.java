package Task3.dynamic;

import connection.ConnectionManager;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.SQLException;

import static Task3.dynamic.Constants.*;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class HolidaysReport {
    public void buildReport() throws SQLException {
        JasperReportBuilder report = DynamicReports.report();

        report.title(cmp.horizontalList(cmp.text("Holydays").setStyle(Constants.HEADER_TEXT_STYLE)
                                                            .setFixedHeight(50),
                                        cmp.image(Constants.IMAGE_PATH).setFixedDimension(280, 60)
                                                                       .setStyle(Constants.HEADER_IMAGE_STYLE)));

        report.columns(col.column("country", "country", DataTypes.stringType()).setWidth(50),
                       col.column("name", "name", DataTypes.stringType()),
                       col.column("date", "date", DataTypes.stringType()).setWidth(90));

        report.pageFooter(cmp.pageXofY());

        setSectionsStyles(report);

        report.summary(getCrosstab(), getBarChart());
        report.summary().setSummaryOnANewPage(true);

        report.setDataSource(connection.Constants.SQL_QUERY, ConnectionManager.openConnection());

        showTheReport(report);
        exportToPdf(report);
    }

    private void setSectionsStyles(JasperReportBuilder report){
        report.columns().setColumnStyle(Constants.COLUMN_STYLE)
                .setColumnTitleStyle(Constants.COLUMN_TITLE_STYLE);
        report.setTitleStyle(Constants.HEADER_STYLE);
        report.setDetailStyle(Constants.DETAILS_STYLE);
        report.setColumnHeaderStyle(COLUMN_HEADER_STYLE);
        report.setSummaryStyle(SUMMARY_STYLE);
    }

    private void showTheReport(JasperReportBuilder report){
        try {
            report.show();
        } catch (DRException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportToPdf(JasperReportBuilder report){
        try {
            report.toPdf(new FileOutputStream(PDF_OUT_PATH));
            System.out.println("Report has been created!");
        } catch (DRException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private CrosstabBuilder getCrosstab() throws SQLException {
        CrosstabBuilder crosstab = ctab.crosstab();

        crosstab.setCellWidth(25).setCellStyle(CROSSTAB_CELL_STYLE);
        crosstab.headerCell(cmp.text("State/Mese").setStyle(CROSSTAB_HEADERCELL_STYLE));
        crosstab.setGrandTotalStyle(CROSSTAB_TOTAL_STYLE);

        crosstab.addColumnGroup(getColumnGroup());
        crosstab.addRowGroup(getRowGroup());
        crosstab.addMeasure(ctab.measure("name", String.class, Calculation.COUNT));

        crosstab.setDataSource(CROSSTAB_SQL, ConnectionManager.openConnection());

        return crosstab;
    }

    private CrosstabColumnGroupBuilder getColumnGroup(){
        CrosstabColumnGroupBuilder columnGroup = ctab.columnGroup("montf", Integer.class).setTotalHeader("Total");

        columnGroup.setTotalHeaderWidth(40)
                .setHeaderStyle(stl.style(CROSSTAB_HEADER_STYLE).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setBottomPadding(7));

        return columnGroup;
    }

    private CrosstabRowGroupBuilder getRowGroup(){
        CrosstabRowGroupBuilder rowGroup = ctab.rowGroup("country", String.class).setTotalHeader("Total");

        rowGroup.setHeaderWidth(70)
                .setHeaderStyle(stl.style(CROSSTAB_HEADER_STYLE).setLeftPadding(5));

        return rowGroup;
    }


    private BarChartBuilder getBarChart() throws SQLException {
        TextColumnBuilder<String> countryColumn = col.column("country", "country", type.stringType());
        TextColumnBuilder<Integer> holidaysColumn = col.column("hcount", "hcount", type.integerType());
        TextColumnBuilder<String> monthsColumn = col.column("montf", "montf", type.stringType());

        BarChartBuilder barChart = cht.barChart();

        barChart.customizers(new ChartCustomizer());

        barChart.setCategory(monthsColumn);
        barChart.series(cht.serie(holidaysColumn).setSeries(countryColumn));

        barChart.setDataSource(BARCHART_SQL, ConnectionManager.openConnection());

        barChart.setStyle(stl.style().setTopPadding(100));
        barChart.setFixedHeight(250);
        barChart.setShowLabels(true);

        return barChart;
    }
}


//To show numbers on chart labels
class ChartCustomizer implements DRIChartCustomizer, Serializable {
    private static final long serialVersionUID = 1L;
    @Override
    public void customize(JFreeChart chart, ReportParameters reportParameters) {
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
    }
}

