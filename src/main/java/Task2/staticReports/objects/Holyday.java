package Task2.staticReports.objects;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "holydays")
@XmlAccessorType(XmlAccessType.FIELD)
public class Holyday {
    @XmlElement(name = "COUNTRY")
    private String country;
    @XmlElement(name = "DATE")
    private String date;
    @XmlElement(name = "NAME")
    private String name;

    public Holyday() {
    }

    public Holyday(String country, String date, String name) {
        this.country = country;
        this.date = date;
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Holyday{" +
                "country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
