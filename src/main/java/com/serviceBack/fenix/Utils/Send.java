package com.serviceBack.fenix.Utils;

import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class Send {

    public void alertas(String receptor_mail, String mail_send, String pass, String smsMessage, String subj) {
        // Configuración para enviar correos electrónicos (SMTP)
        final String username = mail_send; // Correo del remitente
        final String password = pass; // Contraseña del remitente
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crear la sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receptor_mail));
            message.setSubject(subj);

            // Plantilla HTML para el cuerpo del mensaje sin botón
            String htmlMessage = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    ".container { max-width: 600px; margin: auto; background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }" +
                    ".header { background: rgb(232, 218, 239); color: #4c4c4c; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; border-radius: 10px 10px 0 0; }" +
                    ".header img { width: 50px; height: 50px; vertical-align: middle; margin-right: 10px; }" +
                    ".header span { vertical-align: middle; }" +
                    ".content { padding: 20px; }" +
                    ".content h2 { color: #333; }" +
                    ".content p { color: #555; line-height: 1.5; }" +
                    ".footer { background: #f4f4f4; color: #777; padding: 10px; text-align: center; border-radius: 0 0 10px 10px; font-size: 12px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<img src='https://cdn-icons-png.flaticon.com/512/5310/5310773.png' alt='Kimbo Logo'>" +
                    "<span>Kimbo - Gestión de Inventario</span>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<h2>" + subj + "</h2>" +
                    "<p>" + smsMessage + "</p>" +
                    "<p>Por favor, no dudes en contactarnos si necesitas más información.</p>" +
                    "</div>" +
                    "<div class='footer'>" +
                    "<p>Este es un correo generado automáticamente por Kimbo. Por favor, no responder a este correo.</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Configurar el contenido del mensaje como HTML
            message.setContent(htmlMessage, "text/html; charset=utf-8");

            // Enviar el correo electrónico
            Transport.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}