/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.serviceBack.fenix.Util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Send {

    public static void alertas(String receptor_mail) {
        // Configuración para enviar correos electrónicos (SMTP)
        final String username = "mail@.com";//mail del correo creado para alertas
        final String password = "345yhgfddf";//contraseña del correo creado para alertas
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
            message.setSubject("Pruebas correo");
            message.setText("Hola desde java");

            // Enviar el correo electrónico
            Transport.send(message);
            System.out.println("Correo enviado con éxito");

            // Configuración para recibir correos electrónicos (POP3)
            props = new Properties();
            props.put("mail.pop3.host", "pop.gmail.com");
            props.put("mail.pop3.port", "995");
            props.put("mail.pop3.ssl.enable", "true");

            // Crear una sesión de correo para POP3
            Session pop3Session = Session.getInstance(props);

            // Crear un almacén de mensajes POP3
            Store store = pop3Session.getStore("pop3s");
            store.connect("pop.gmail.com", username, password);

            // Abrir la carpeta INBOX
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Mostrar los mensajes recibidos
            Message[] messages = inbox.getMessages();
            System.out.println("Número de mensajes recibidos: " + messages.length);

            // Cerrar la conexión
            inbox.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
