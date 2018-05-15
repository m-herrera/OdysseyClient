package org.tec.datosII.OdysseyClient;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.time.LocalDate;

public class Authenticator {
    public boolean login(String userStr, String passwordStr) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "1");

        Element user = root.addElement("username").addText(userStr);

        Element password = root.addElement("password").addText(passwordStr);

        String request = document.asXML();

        System.out.println(request);

        NioClient client = NioClient.getInstance();
        client.send(request.getBytes());

        //SocketConnection.request(request);
        //if request return true or return false

        return true;
    }

    public boolean register(String fnameStr, String lnameStr, String userStr, String passwordStr, LocalDate bdayDate, String[] genresStr){
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "2");

        Element fname = root.addElement("first_name").addText(fnameStr);

        Element lname = root.addElement("last_name").addText(lnameStr);

        Element user = root.addElement("username").addText(userStr);

        Element bday = root.addElement("birthday").addText(bdayDate.toString());

        Element password = root.addElement("password").addText(passwordStr);

        String request = document.asXML();

        System.out.println(request);

        NioClient client = NioClient.getInstance();
        client.send(request.getBytes());

        //if request return true or return false

        return true;
    }
}
