package Business;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class MailUtils {
    private static MailUtils instance = new MailUtils();
    private static String mail;
    private static String password;
    private static Properties props;
    private static Session session;

    private MailUtils() {
        mail = ""; //insert email
        password = ""; //insert password
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, password);
            }
        });
    }

    public static MailUtils getInstance() {
        return instance;
    }

    public int send(String receiver, String mailObject, String mailMessage) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setFrom(new InternetAddress(mail));
            message.setSubject(mailObject);
            message.setText(mailMessage);

            Transport.send(message);
            return 0;

        } catch (MessagingException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int send(String receiver, String mailObject, String mailMessage, File file) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setFrom(new InternetAddress(mail));
            message.setSubject(mailObject);

            Multipart multipart = new MimeMultipart();

            BodyPart bodyPartText = new MimeBodyPart();
            bodyPartText.setText(mailMessage);
            multipart.addBodyPart(bodyPartText);

            BodyPart bodyPartFile = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            bodyPartFile.setDataHandler( new DataHandler(source));
            bodyPartFile.setFileName(file.getName());
            multipart.addBodyPart(bodyPartFile);

            message.setContent(multipart);

            Transport.send(message);
            boolean done = file.delete();
            if(!done){
                return 2;
            }
            return 0;
        } catch (MessagingException e) {
            e.printStackTrace();
            return 1;
        }
    }

}
