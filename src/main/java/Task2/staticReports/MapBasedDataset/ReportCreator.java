package Task2.staticReports.MapBasedDataset;

import Task2.staticReports.objects.YearTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.*;
import java.util.*;

public class ReportCreator {
    private JRDataSource dataSource;
    private Map<String, Object> parameters;
    private final String xmlFilePath;
    private final YearTO data;
    private final String templatePath;


    /**
     * After creating class object you have to prepare parameters and datasource manually
     */
    public ReportCreator(){
        dataSource = new JREmptyDataSource();
        parameters = new TreeMap<>();

        xmlFilePath = "src/main/resources/MyDataBase.xml";
        templatePath = "src/main/resources/reports/templates/JasperReport.jrxml";

        //parsing data from xml file
        try {
            JAXBContext context = JAXBContext.newInstance(YearTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            data = (YearTO) unmarshaller.unmarshal(new File(xmlFilePath));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }



    public void setParameters(Map<String, Object> parameters){
        this.parameters = parameters;
    }




    public void prepareMapBasedDataSource(){
        //Creating Map based datasource
        List<Map<String, ?>> lines = new ArrayList<>();

        //Filling map
        data.getHolydays().stream().forEach((h)->{
            Map<String, Object> row = new TreeMap<>();
            row.put("country", h.getCountry());
            row.put("date", h.getDate());
            row.put("name", h.getName());

            lines.add(row);
        });

        //Converting it to JRDataSource
        dataSource = new JRMapCollectionDataSource(lines);
    }

    public void prepareBeanCollectionBasedDataSource(){
        dataSource = new JRBeanCollectionDataSource(data.getHolydays());
    }

    public void prepareDataBaseBasedDataSource(String url, String login, String password, String sql){
        try {
            Connection connection = DriverManager.getConnection(url, login, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            dataSource = new JRResultSetDataSource(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }




    public void saveReportToPDF(String outPath, DataSourcesTypes type){
        try {
            //compiling report from template
            JasperReport report = JasperCompileManager.compileReport(templatePath);

            //Filling compiled report with data
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

            JasperExportManager.exportReportToPdfFile(print, outPath);
//            JasperExportManager.exportReportToPdfFile(print, "src/main/resources/reports/out/JasperReportBEANCOLLECTIONBased.pdf");
            System.out.println("Report has been created!");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

}
