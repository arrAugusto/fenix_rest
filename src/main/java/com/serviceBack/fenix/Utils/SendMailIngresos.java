/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.Utils;

import static org.hibernate.bytecode.BytecodeLogging.LOGGER;

/**
 *
 * @author agr12
 */
public class SendMailIngresos {

    private final Send sendMail;

    public SendMailIngresos() {
        this.sendMail = new Send();
    }

    public void sendMail(String mailTO, String mailFROM, String PWD, String messageItemsLoads) {
        sendMail.alertas(mailTO, mailFROM, PWD, messageItemsLoads);
        // Registrar el error en el log
        LOGGER.info("Menssage send in mail"+messageItemsLoads);

    }
}
