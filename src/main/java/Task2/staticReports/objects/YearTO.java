package Task2.staticReports.objects;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Year-2021")
@XmlAccessorType(XmlAccessType.FIELD)
public class YearTO {
    @XmlElement(name = "holydays")
    private List<Holyday> holydays;

    public YearTO() {
    }

    public YearTO(List<Holyday> holydays) {
        this.holydays = holydays;
    }

    public List<Holyday> getHolydays() {
        return holydays;
    }

    public void setHolydays(List<Holyday> holydays) {
        this.holydays = holydays;
    }
}
