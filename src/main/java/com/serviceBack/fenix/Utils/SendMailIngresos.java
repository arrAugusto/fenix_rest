package com.serviceBack.fenix.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMailIngresos {

    private final Send sendMail;

    @Autowired
    public SendMailIngresos(Send sendMail) {
        this.sendMail = sendMail;
    }

    public void sendMail(String mailTO, String mailFROM, String PWD, String messageItemsLoads, String subj) {
        sendMail.alertas(mailTO, mailFROM, PWD, messageItemsLoads, subj, "");
        // Registrar el mensaje en la salida estándar
        System.out.println("Message sent in mail: " + messageItemsLoads);
    }
}
