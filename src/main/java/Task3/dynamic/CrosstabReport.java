package Task3.dynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.crosstab.*;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.exception.DRException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class CrosstabReport {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public CrosstabReport() {
        this.url="jdbc:mysql://localhost:3306/holydays";
        this.user="root";
        this.password="224486";

        try {
            connection = connect(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CrosstabReport(String url, String user, String password) {
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

        CrosstabBuilder crosstab = ctab.crosstab();
        CrosstabColumnGroupBuilder<Integer> columnGroup = ctab.columnGroup("montf", Integer.class);
        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("country", String.class);

        CrosstabMeasureBuilder<String> measure = ctab.measure("name", String.class, Calculation.COUNT);//То есть Jasper сам возьмёт, сгруппирует по месяцам и странам и подсчитает name

        crosstab.addColumnGroup(columnGroup).addRowGroup(rowGroup).measures(measure);
        String sql = "select country, name, cast(substring(date, 4, 2) as signed) as montf from holydays order by montf;";
        crosstab.setDataSource(sql, connection);

        report.summary(crosstab);


        try {
            report.show();
        } catch (DRException e) {
            throw new RuntimeException(e);
        }

        //todo:Finish the report
    }









    public Connection connect(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
