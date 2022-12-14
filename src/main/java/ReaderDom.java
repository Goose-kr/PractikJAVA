import model.MyStudentHan;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReaderDom {
    private int indent = 0;
    final static int INDENT = 4;

    private static final String[] TYPE_NAMES = {"ELEMENT_NODE", "ATTRIBUTE_NODE", "TEXT_NODE", "CDATA_SECTION_NODE", "ENTITY_REFERENCE_NODE", "ENTITY_NODE", "PROCESSING_INSTRUCTION_NODE", "COMMENT_NODE", "DOCUMENT_NODE", "DOCUMENT_TYPE_NODE", "DOCUMENT_FRAGMENT_NODE", "NOTATION_NODE"};

    public String getTypeName(short nodeType) {
        return TYPE_NAMES[nodeType - 1];
    }

//    public void myPrint(Document document) {
//        printDocInfo(document);
//        printNodeInfo(document);
//    }

//    public void printDocInfo(Document document) {
//        printString("Имя узла документа : " + document.getNodeName());
//    }

    public void printElementInfo(Element element) {
        NamedNodeMap nnm = element.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Attr attr = (Attr) nnm.item(i);
            printString("Attribute name = " + attr.getName() + ", value = " + attr.getValue());
        }
    }

    public void printTextInfo(Text text) {
        printString("Значение поля : " + text.getData());
    }

//    public void printNodeInfo(Node node) {
//        indent += INDENT;
//        printString("Node name = " + node.getNodeName() + ", node type = " + getTypeName(node.getNodeType()));
//
//        if (node instanceof Element) {
//            printElementInfo((Element) node);
//        } else if (node instanceof Text) {
//            printTextInfo((Text) node);
//        }
//        NodeList nl = node.getChildNodes();
//        for (int i = 0; i < nl.getLength(); i++) {
//            printNodeInfo(nl.item(i));
//        }
//        indent -= INDENT;
//    }

    public void printString(String str) {
        String ind_s;
        if (indent > 0) {
            char[] ind = new char[indent];
            java.util.Arrays.fill(ind, ' ');
            ind_s = new String(ind, 0, indent);
        } else {
            ind_s = "";
        }
        System.out.println(ind_s + str);
    }

    public static ArrayList<MyStudentHan> parseInfoFromXML(NodeList list) {
        ArrayList<MyStudentHan> students = new ArrayList<>();
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var student = new MyStudentHan();
                Element element = (Element) node;
                student.setFirstname(node.getAttributes().getNamedItem("firstname").getTextContent());
                student.setLastname(node.getAttributes().getNamedItem("lastname").getTextContent());
                student.setGroupnumber(node.getAttributes().getNamedItem("groupNumber").getTextContent());
                try {
                    student.setAvg(Integer.parseInt(element.getElementsByTagName("average").item(0).getTextContent()));
                } catch (NullPointerException ignored) {
                }

                NodeList salaryNodeList = element.getElementsByTagName("subject");
                var subjects = new HashMap<String, Integer>();
                for (int i = 0; i < salaryNodeList.getLength(); i++) {
                    var title = salaryNodeList.item(i).getAttributes().getNamedItem("title").getTextContent();
                    var mark = salaryNodeList.item(i).getAttributes().getNamedItem("mark").getTextContent();
                    subjects.put(title, Integer.valueOf(mark));
                }
                student.setSubjects(subjects);
                students.add(student);
            }
        }
        return students;
    }

    public static void checkAVG(ArrayList<MyStudentHan> students) {
        for (var student : students) {
            var subjects = student.getSubjects();
            var numbers = subjects.values();

            var avg = numbers.toArray().length != 0 ? numbers.stream().reduce(Integer::sum).orElse(0) / numbers.toArray().length : 0;
            if (avg != student.getAvg()) {
                student.setAvg(avg);
            }
        }
    }

    private static void writeXml(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    public static Document formingXMLData(ArrayList<MyStudentHan> students, DocumentBuilder builder) {
        var document = builder.newDocument();
        Element rootElement = document.createElement("group");
        document.appendChild(rootElement);

        for (var stud : students) {
            Element student = document.createElement("student");
            rootElement.appendChild(student);
            student.setAttribute("firstname", stud.getFirstname());
            student.setAttribute("lastname", stud.getLastname());
            student.setAttribute("groupNumber", stud.getGroupnumber());


            var subjects = stud.getSubjects();

            subjects.forEach((key, value) -> {
                Element subject = document.createElement("subject");
                student.appendChild(subject);
                subject.setAttribute("title", key);
                subject.setAttribute("mark", String.valueOf(value));
            });
            var avg = document.createElement("average");
            avg.setTextContent(String.valueOf(stud.getAvg()));
            student.appendChild(avg);
        }
        return document;
    }

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            System.out.println("Введите название файла с которого нужно читать: ");
            String fileName = new Scanner(System.in).nextLine();

            Document document = builder.parse("src/main/resources/" + fileName + ".xml");
            document.getDocumentElement().normalize();

            NodeList list = document.getElementsByTagName("student");
            ArrayList<MyStudentHan> students = ReaderDom.parseInfoFromXML(list);
            checkAVG(students);

            document = formingXMLData(students, builder);
            System.out.println("Введите название файла для записи: ");
            fileName = new Scanner(System.in).nextLine();
            writeXml(document, "src/main/resources/" + fileName + ".xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
