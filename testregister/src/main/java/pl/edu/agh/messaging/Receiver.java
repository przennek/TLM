package pl.edu.agh.messaging;

import java.io.UnsupportedEncodingException;

public class Receiver {

    public void receiveMessage(byte[] message) {
        try {
            System.out.println("Received <" + new String(message, "UTF-8") + ">");
        } catch (UnsupportedEncodingException e) {
            // whatever, if your system doesn't support utf-8 it's your fault, happy debugging
        }
    }
}