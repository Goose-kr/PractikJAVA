package model.JAX;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "subject")
@XmlType(propOrder = {"title", "mark"})
public class JAXSubj {
    private String title;
    private Integer mark;

    public String getTitle() {
        return title;
    }

    @XmlAttribute(name = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMark() {
        return mark;
    }

    @XmlAttribute(name = "mark")
    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Subjects{" +
                "title='" + title + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
