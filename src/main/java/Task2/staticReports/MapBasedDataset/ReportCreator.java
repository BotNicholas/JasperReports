package Task2.staticReports.MapBasedDataset;

import Task2.staticReports.objects.YearTO;
import connection.ConnectionManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.*;
import java.util.*;

import static connection.Constants.SQL_QUERY;

public class ReportCreator {
    private JRDataSource dataSource;
    private Map<String, Object> parameters;
    private final YearTO data;


    // After creating class object you have to prepare parameters and datasource manually
    public ReportCreator(){
        dataSource = new JREmptyDataSource();
        parameters = new TreeMap<>();

        data = parseData();
    }

    private YearTO parseData(){
        try {
            JAXBContext context = JAXBContext.newInstance(YearTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (YearTO) unmarshaller.unmarshal(new File(Constants.XML_FILE_PATH));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParameters(Map<String, Object> parameters){
        this.parameters = parameters;
    }

    public void prepareMapBasedDataSource(){
        List<Map<String, ?>> lines = new ArrayList<>();
        fillMapBasedDataSourceLines(lines);
        dataSource = new JRMapCollectionDataSource(lines);
    }

    private void fillMapBasedDataSourceLines(List<Map<String, ?>> lines){
        data.getHolydays().stream().forEach((h)->{
            Map<String, Object> row = new TreeMap<>();
            row.put("country", h.getCountry());
            row.put("date", h.getDate());
            row.put("name", h.getName());
            lines.add(row);
        });
    }

    public void prepareBeanCollectionBasedDataSource(){
        dataSource = new JRBeanCollectionDataSource(data.getHolydays());
    }

    public void prepareDataBaseBasedDataSource(){
        try {
            Statement statement = ConnectionManager.getStatement();
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            dataSource = new JRResultSetDataSource(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveReportToPDF(String outPath){
        try {
            JasperReport report = JasperCompileManager.compileReport(Constants.TEMPLATE_PATH);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            JasperExportManager.exportReportToPdfFile(print, outPath);
            System.out.println("Report has been created!");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
