/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.serviceBack.fenix.Utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Send {

    public void alertas(String receptor_mail, String mail_send, String pass, String smsMessage) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");
        // Configuración para enviar correos electrónicos (SMTP)
        final String username = mail_send;//mail del correo creado para alertas
        final String password = pass;//contraseña del correo creado para alertas
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crear sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Crear un objeto de mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receptor_mail));
            message.setSubject("ERROR");
            message.setText(smsMessage.toString());

            // Enviar el correo electrónico
            Transport.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
