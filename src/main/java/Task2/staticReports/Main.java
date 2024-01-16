package Task2.staticReports;

import Task2.staticReports.MapBasedDataset.DataSourcesTypes;
import Task2.staticReports.MapBasedDataset.ReportCreator;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        ReportCreator creator = new ReportCreator();

        Map<String, Object> parameters = new TreeMap<>();
        parameters.put("Year", "2021");
        creator.setParameters(parameters);

        DataSourcesTypes type = DataSourcesTypes.DATABASE;

        switch(type){
            case MAP:
                creator.prepareMapBasedDataSource();
                break;
            case COLLECTION:
                creator.prepareBeanCollectionBasedDataSource();
                break;
            case DATABASE:
                creator.prepareDataBaseBasedDataSource();
                break;
        }

        String ReportOut = "src/main/resources/reports/out/Task2/JasperReport"+type.name()+"Based.pdf";
        creator.saveReportToPDF(ReportOut);
    }
}
