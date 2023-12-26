package Task2.staticReports;

import Task2.staticReports.MapBasedDataset.DataSourcesTypes;
import Task2.staticReports.MapBasedDataset.ReportCreator;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {

        ReportCreator creator = new ReportCreator();

//        Preparing parameters
        Map<String, Object> parameters = new TreeMap<>();
        parameters.put("Year", "2021");
        creator.setParameters(parameters);

//        Preparing datasource
//        DataSourcesTypes type = DataSourcesTypes.MAP;
//        DataSourcesTypes type = DataSourcesTypes.COLLECTION;
        DataSourcesTypes type = DataSourcesTypes.DATABASE;

        switch(type){
            case MAP:
                creator.prepareMapBasedDataSource();
                break;
            case COLLECTION:
                creator.prepareBeanCollectionBasedDataSource();
                break;
            case DATABASE:
                String url = "jdbc:mysql://localhost:3306/holydays";
                String login = "root";
                String password = "224486";
                String sql = "select * from holydays;";
                creator.prepareDataBaseBasedDataSource(url, login, password, sql);
                break;
        }



//        Saving report
        String ReportOut = "src/main/resources/reports/out/Task2/JasperReport"+type.name()+"Based.pdf";
        creator.saveReportToPDF(ReportOut, type);





//        YearTO data;
//        try {
//            JAXBContext context = JAXBContext.newInstance(YearTO.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            data = (YearTO) unmarshaller.unmarshal(new File("src/main/resources/MyDataBase.xml"));
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }




//        List<Holyday> holydays = new ArrayList<>();
//        holydays.add(new Holyday("AAAA", "12121112", "AAAA Holiday"));
//        holydays.add(new Holyday("BBBB", "111", "BBBB Holiday"));
//        holydays.add(new Holyday("CCCC", "@@@@@@@", "CCCC Holiday"));
//        holydays.add(data.getHolydays().get(0));
//        holydays.add(data.getHolydays().get(1));
//        holydays.add(data.getHolydays().get(2));

//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(holydays);


//        JRDataSource dataSource = new JRBeanCollectionDataSource(data.getHolydays());
//
//        try {
//            JasperReport report = JasperCompileManager.compileReport("src/main/resources/reports/templates/JasperReport.jrxml");
//            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
//            JasperExportManager.exportReportToPdfFile(print, "src/main/resources/reports/out/JasperReportBEANCOLLECTIONBased.pdf");
//        } catch (JRException e) {
//            throw new RuntimeException(e);
//        }


    }
}
