package org.tec.datosII.OdysseyClient;


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

        System.out.println("Response:" + new String(this.rsp));
    }
}
