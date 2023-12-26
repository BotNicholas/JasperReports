package Task3.dynamic;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleReport {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public SimpleReport(){
        this.url = "jdbc:mysql://localhost:3306/holydays";
        this.user = "root";
        this.password = "224486";

        try {
//            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    url,user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SimpleReport(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;


        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void BuildReport(){
        JasperReportBuilder report = DynamicReports.report();

//        report.columns(Columns.column("Country", "country", DataTypes.stringType()),
//                       Columns.column("Date", "date", DataTypes.stringType()),
//                       Columns.column("Holiday name", "name", DataTypes.stringType()));
            report.columns(col.column("Country", "country", DataTypes.stringType()),
                           col.column("Date", "date", DataTypes.stringType()),
                           col.column("Holiday name", "name", DataTypes.stringType()));

        report.title(cmp.text("My first simple report!")
                .setHorizontalAlignment(HorizontalAlignment.CENTER));


        report.pageFooter(cmp.pageXofY());

        String sql = "select * from holydays";

        report.setDataSource(sql, connection);

        report.setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY));

        try {
            report.show();

//            report.toPdf(new FileOutputStream("src/main/resources/report.pdf"));

        } catch (DRException e) {
            throw new RuntimeException(e);
        }
//        catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
