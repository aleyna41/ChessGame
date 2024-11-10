package com.example.backend.entity;
import com.example.backend.entity.User;
//import com.example.backend.service.FriendStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


    @Entity
    @Getter
    @Setter
    @Table(name = "friends")
    public class Friend {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long friendshipId;

        @Column
        private String sender; // die bzh zwischen den zwei nutzern ob sie freunde sind oder nicht

        @Column
        private String receiver;

        @Enumerated(EnumType.STRING)
        private FriendStatus status;

        private boolean isPrivate;


        public Friend() {
        } //StandardKonstruktor für JPA-Implementierung(einfach lassen)


        public Friend(String sender, String receiver) {
            this.sender = sender;
            this.receiver = receiver;
        }


        public void sendFriendRequestNotif(User receiver) { // hier anstatt User user, User receiver damit FriendService funktioniert
            final String username = "schachepgruppef@gmail.com";
            final String password = "blno jekt kxbs penh";

            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {
                javax.mail.Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(receiver.getEmail())); // Die E-Mail-Adresse des Benutzers
                message.setSubject("Freundschaftsanfrage!");
                message.setText("Hallo, " + receiver.getUsername() + "! Du hast eben eine Freundschaftsanfrage von einem User namens " + sender + " erhalten. :)");
                Transport.send(message);

                System.out.println("Bestätigungsmail wurde an " + receiver.getEmail() + " gesendet.");
            } catch (MessagingException e) {
                e.printStackTrace();

            }
        }
    }


