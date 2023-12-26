package Task3;

import Task3.dynamic.CrosstabReport;
import Task3.dynamic.HolidaysReport;
import Task3.dynamic.SimpleReport;

public class Main {
    public static void main(String[] args) {
//        SimpleReport report = new SimpleReport();
//        report.BuildReport();


        HolidaysReport holidaysReport = new HolidaysReport();
        holidaysReport.buildReport();


//        CrosstabReport report = new CrosstabReport();
//
//        report.buildReport();
    }
}
