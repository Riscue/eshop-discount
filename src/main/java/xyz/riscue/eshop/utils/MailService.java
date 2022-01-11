package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import xyz.riscue.eshop.model.config.MailConfig;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class);

    public static void send(MailConfig mailConfig, String subject, String content) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtps");
            props.setProperty("mail.host", StringUtil.ifPresentOrDefault(mailConfig.getHost(), "localhost"));
            props.setProperty("mail.smtps.auth", "true");
            props.setProperty("mail.smtps.port", StringUtil.ifPresentOrDefault(mailConfig.getPort(), "25"));
            props.setProperty("mail.smtps.ssl.trust", StringUtil.ifPresentOrDefault(mailConfig.getHost(), "localhost"));
            props.setProperty("mail.smtps.ssl.enable", "true");
            props.setProperty("mail.smtps.ssl.protocols", "TLSv1.1 TLSv1.2");

            Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(StringUtil.ifPresentOrDefault(mailConfig.getFrom(), mailConfig.getUsername())));

            if (mailConfig.getTo() != null) {
                for (String adr : mailConfig.getTo()) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(adr));
                }
            }
            if (mailConfig.getCc() != null) {
                for (String adr : mailConfig.getCc()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(adr));
                }
            }
            message.setSubject(subject);
            message.setText(content);

            Transport transport = session.getTransport();
            transport.connect(mailConfig.getUsername(), mailConfig.getPassword());
            transport.sendMessage(message, message.getAllRecipients());

            logger.info(String.format("Mail sent to: %s", Arrays.stream(message.getAllRecipients()).map(Address::toString).collect(Collectors.joining(", "))));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
