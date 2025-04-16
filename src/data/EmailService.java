package data;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

    public static void sendMessage(String recipientEmail, String affair, String body) {
        // Configuración del correo de envío
        final String username = "techlend27@gmail.com";
        final String password = "xzql nyuk apkd fcrq";

        // Propiedades SMTP para Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crear sesión
        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            }
        );

        try {
            // Crear el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Remitente
            message.setRecipients(Message.RecipientType.TO, // Destinatario
                    InternetAddress.parse(recipientEmail));
            message.setSubject(affair);  // Asunto
            message.setText(body); // Cuerpo

            // Enviar
            Transport.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

