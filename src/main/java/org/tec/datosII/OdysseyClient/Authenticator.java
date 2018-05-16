package org.tec.datosII.OdysseyClient;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.time.LocalDate;

public class Authenticator {
    public boolean login(String userStr, String passwordStr) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request").addAttribute("opcode", "1");

        Element user = root.addElement("username").addText(userStr);

        Element password = root.addElement("password").addText(passwordStr);

        String request = document.asXML();

        NioClient client = NioClient.getInstance();

        ResponseHandler loginHandler = client.send(request.getBytes());

        System.out.println(loginHandler.getStrResponse());

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

        NioClient client = NioClient.getInstance();
        ResponseHandler registerHandler = client.send(request.getBytes());

        System.out.println(registerHandler.getStrResponse());
        //if request return true or return false

        return true;
    }
}
