package model.JAX;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;

@XmlRootElement(name = "student")
@XmlType(propOrder = {"firstname", "lastname", "groupNumber", "subject", "avg"})
public class JAXStudent {
    private String firstname = "";
    private String lastname = "";
    private int avg = 0;

    private ArrayList<JAXSubj> subject;

    public ArrayList<JAXSubj> getSubject() {
        return subject;
    }

    @XmlElement(name = "subject")
    public void setSubject(ArrayList<JAXSubj> subject) {
        this.subject = subject;
    }

    private String groupNumber = "";


    public String getGroupNumber() {
        return groupNumber;
    }

    @XmlAttribute(name = "groupNumber")
    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }


    @XmlAttribute(name = "firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @XmlAttribute(name = "lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    @XmlElement(name = "average")
    public void setAvg(int avg) {
        this.avg = avg;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }


    public int getAvg() {
        return avg;
    }

    public JAXStudent() {

    }

    @Override
    public String toString() {
        return "StudentJAXB{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", avg=" + avg +
                ", subject=" + subject +
                ", groupNumber='" + groupNumber + '\'' +
                '}';
    }
}
