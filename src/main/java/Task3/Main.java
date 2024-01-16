package Task3;

import Task3.dynamic.HolidaysReport;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        HolidaysReport holidaysReport = new HolidaysReport();
        try {
            holidaysReport.buildReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
