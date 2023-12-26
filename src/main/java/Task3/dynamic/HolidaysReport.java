package Task3.dynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.PaddingBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.crosstabs.base.CrosstabBaseCloneable;
import net.sf.jasperreports.engine.export.oasis.BorderStyle;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        report.summary(getCrosstab());
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

        CrosstabColumnGroupBuilder columnGroup = ctab.columnGroup("montf", Integer.class).setTotalHeader("Total"); //column group
        CrosstabRowGroupBuilder rowGroup = ctab.rowGroup("country", String.class).setTotalHeader("Total"); //row group

        //Setting table header cell
        crosstab.headerCell(cmp.text("State / Mese"));



        crosstab.addColumnGroup(columnGroup);
        crosstab.addRowGroup(rowGroup);
        crosstab.addMeasure(ctab.measure("name", String.class, Calculation.COUNT));

        crosstab.setDataSource("select country, name, cast(substring(date, 4, 2) as signed) as montf from holydays order by montf", connection);

        crosstab.setCellWidth(25);

        return crosstab;
    }



    private Connection connect(String url, String user, String password) throws SQLException {
            return DriverManager.getConnection(url, user, password);
    }
}
