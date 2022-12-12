import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import model.JAX.JAXGroup;
import model.JAX.JAXSubj;

import java.io.File;
import java.util.Scanner;

public class ReaderJax {
    public static void main(String[] args) {
        System.out.println("Введите название файла с которого нужно читать: ");
        String fileName = new Scanner(System.in).nextLine();

        var groupStudents = fromXmlToObject("src/main/resources/" + fileName + ".xml");
        assert groupStudents != null;

        groupStudents.getStudents().stream().filter(y -> y.getSubject() != null).forEach(student -> {
            var avg = student.getSubject().size() != 0 ? student.getSubject().stream().mapToInt(JAXSubj::getMark).sum() / student.getSubject().size() : 0;
            if (avg != student.getAvg()) student.setAvg(avg);
        });
        System.out.println("Введите название файла для записи: ");
        fileName = new Scanner(System.in).nextLine();
        convertObjectToXml(groupStudents, "src/main/resources/" + fileName + ".xml");
    }

    private static JAXGroup fromXmlToObject(String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXGroup.class);
            Unmarshaller un = jaxbContext.createUnmarshaller();

            return (JAXGroup) un.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void convertObjectToXml(JAXGroup student, String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(JAXGroup.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // маршаллинг объекта в файл
            marshaller.marshal(student, new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
