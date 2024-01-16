package Task3.dynamic;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.*;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class Constants {
    public static final StyleBuilder HEADER_STYLE = stl.style().setLeftPadding(12)
                                                               .setTopPadding(15)
                                                               .setRightPadding(12);

    public static final StyleBuilder DETAILS_STYLE = stl.style().setLeftPadding(12)
                                                                .setRightPadding(12);

    public static final StyleBuilder COLUMN_HEADER_STYLE = stl.style().setLeftPadding(12)
                                                                      .setTopPadding(15)
                                                                      .setRightPadding(12);

    public static final StyleBuilder SUMMARY_STYLE = stl.style().setLeftPadding(12)
                                                                .setTopPadding(15)
                                                                .setRightPadding(12)
                                                                .setBottomPadding(15)
                                                                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

    public static final StyleBuilder HEADER_TEXT_STYLE = stl.style().bold()
                                                                    .setFontSize(20)
                                                                    .setHorizontalAlignment(HorizontalAlignment.LEFT);

    public static final StyleBuilder HEADER_IMAGE_STYLE = stl.style().setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT);

    public static final StyleBuilder COLUMN_STYLE = stl.style().setBorder(stl.penThin())
                                                               .setLeftPadding(2)
                                                               .setBottomPadding(10);

    public static final StyleBuilder COLUMN_TITLE_STYLE = stl.style(COLUMN_STYLE).setBackgroundColor(new Color(235, 243, 252))
                                                                                 .setBottomPadding(20)
                                                                                 .setLeftPadding(3);

    public static final StyleBuilder CROSSTAB_CELL_STYLE = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                                                      .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                                                      .setPadding(2)
                                                                      .setBorder(stl.penThin());

    public static final Color CROSSTAB_TOTAL_COLOR = new Color(134, 177, 247);
    public static final Color CROSSTAB_HEADER_COLOR = new Color(189, 213, 252);
    public static final Color CROSSTAB_HEADERCELL_COLOR = new Color(215, 229, 252);

    public static final StyleBuilder CROSSTAB_TOTAL_STYLE = stl.style().setBackgroundColor(CROSSTAB_TOTAL_COLOR)
                                                                       .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                                                       .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                                                       .setBorder(stl.penThin());

    public static final StyleBuilder CROSSTAB_HEADER_STYLE = stl.style().setBackgroundColor(CROSSTAB_HEADER_COLOR)
                                                                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                                                        .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                                                        .setBorder(stl.penThin());

    public static final  StyleBuilder CROSSTAB_HEADERCELL_STYLE = stl.style().setBackgroundColor(CROSSTAB_HEADERCELL_COLOR)
                                                                             .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                                                                             .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                                                                             .setBorder(stl.penThin());

    public static final String IMAGE_PATH = "src/main/resources/img/logo.png";
    public static final String PDF_OUT_PATH = "src/main/resources/reports/out/Task3/dynamicHolydaysReport.pdf";
    public static final String CROSSTAB_SQL = "select country, name, cast(substring(date, 4, 2) as signed) as montf from holydays order by montf";
    public static final String BARCHART_SQL = "select country, cast(substring(date, 4, 2) as signed) as montf, count(name) as hcount from holydays group by montf, country";
}


