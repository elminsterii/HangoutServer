package com.fff.hos.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class VerifyEmailSender {

    private static final Logger LOGGER = Logger.getLogger(VerifyEmailSender.class.getName());

    public String sendVerifyMail(String strTargetEmail) {
        String strVerifyCode = genVerifyCode();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(System.getProperty("email_sender_address"), System.getProperty("email_sender_title")));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(strTargetEmail, ""));
            msg.setSubject(System.getProperty("email_verify_title"));
            msg.setText(System.getProperty("email_verify_content") + strVerifyCode);
            Transport.send(msg);
        } catch (UnsupportedEncodingException | MessagingException e) {
            LOGGER.warning(e.getMessage());
        }
        return strVerifyCode;
    }

    private String genVerifyCode() {
        StringBuilder strVerifyCode = new StringBuilder();

        final int CODE_SIZE = 4;
        final int MAX_NUMBER = 9;
        final int MIN_NUMBER = 0;

        Random rand = new Random();

        for(int i=0; i<CODE_SIZE; i++)
            strVerifyCode.append(rand.nextInt(MAX_NUMBER - MIN_NUMBER + 1));

        return strVerifyCode.toString();
    }
}
