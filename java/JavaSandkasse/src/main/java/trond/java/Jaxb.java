package trond.java;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.*;

import com.sun.xml.internal.bind.marshaller.DataWriter;
import com.sun.xml.internal.bind.marshaller.DumbEscapeHandler;

import trond.java.jaxb.mapping.Customer;
import trond.java.jaxb.mapping.ObjectFactory;
import trond.java.jaxb.mapping.Order;
import trond.java.jaxb.mapping.OrderLine;

/**
 * @author trond.
 */
public class Jaxb {

    public static void main(String[] args) {
        try {
            JAXBContext ctx = JAXBContext.newInstance("trond.java.jaxb.mapping");
            //JAXBContext ctx = JAXBContext.newInstance(Order.class);

            Marshaller marshaller = ctx.createMarshaller();

            Order order = new Order();

            Customer customer = new Customer();
            customer.setFirstName("Trond");
            customer.setLastName("Valen");
            OrderLine orderLine = new OrderLine();
            orderLine.setProductNumber("234-abd");
            orderLine.setUnitCount(2);

            order.setCustomer(customer);
            order.getOrderLines().add(orderLine);
            order.setData("<foo>123</foo>");

            JAXBElement<Order> orderJAXBElement = new ObjectFactory().createOrder(order);
            StringWriter writer = new StringWriter();
            marshaller.marshal(orderJAXBElement, writer);
            String s = writer.toString();
            System.out.println(s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceFirst("data .*?>", "data>"));

            StringWriter stringWriter = new StringWriter();
            DataWriter dataWriter = new DataWriter(stringWriter, "UTF-8", DumbEscapeHandler.theInstance);
            marshaller.marshal(orderJAXBElement, dataWriter);
            System.out.println("Dumb: " + stringWriter.toString());

            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            JAXBElement<Order> unmarshalled = (JAXBElement<Order>) unmarshaller.unmarshal(new StringReader(
                    "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><order><customer>"
                    + "<firstName>Trond</firstName><lastName>Valen</lastName>" + "</customer><orderLines>"
                    + "<productNumber>234-abd</productNumber><unitCount>2</unitCount>" + "</orderLines>"
                    + "<data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:double\">" + "2.3" + "</data></order>"));

            Order orderUnmarshalled = unmarshalled.getValue();
            System.out.println(orderUnmarshalled.getOrderLines().get(0).getProductNumber());
            System.out.println(orderUnmarshalled.getData());
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
