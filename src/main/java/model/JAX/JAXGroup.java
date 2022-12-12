package model.JAX;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;

@XmlRootElement(name = "group")
@XmlType(propOrder = {"students"})
public class JAXGroup {
    private ArrayList<JAXStudent> students;

    @Override
    public String toString() {
        return "GroupJAXB{" +
                "students=" + students +
                '}';
    }

    public ArrayList<JAXStudent> getStudents() {
        return students;
    }

    @XmlElement(name = "student")
    public void setStudents(ArrayList<JAXStudent> students) {
        this.students = students;
    }

    public JAXGroup() {

    }
}
