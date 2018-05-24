package org.tec.datosII.OdysseyClient;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class ResponseHandler {
    private byte[] rsp = null;

    public synchronized boolean handleResponse(byte[] rsp) {
        this.rsp = rsp;
        this.notify();
        return true;
    }

    public synchronized void waitForResponse() {
        while(this.rsp == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Document getXmlResponse() throws Exception{
        return DocumentHelper.parseText(new String(rsp));
    }

    public byte[] getResponse(){
        return rsp;
    }

    public String getStrResponse(){
        return new String(rsp);
    }
}
