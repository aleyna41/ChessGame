package com.example.backend;

import com.example.backend.entity.SessionToken;
import com.example.backend.repository.SessionTokenRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.backend.entity.User;
import org.springframework.data.util.Pair;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

import java.util.*;

@Service
public class SecurityService {
    private final SessionTokenRepository sessionTokenRepository;
    public SecurityService(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }

    // Passwort-Methoden

    private Map<Long, Pair<String, Long>> authCodes = new HashMap<>();

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }
    public String[] hashAndSaltPassword(String password, String salt) {
        String saltedPassword = salt + password;
        String hashedPassword = hashPassword(saltedPassword);
        return new String[]{hashedPassword, salt};
    }
    public boolean verifyPassword(String inputPassword, String storedPasswordHash, String storedSalt) {
        String saltedInputPassword = storedSalt + inputPassword;
        String hashedInputPassword = hashPassword(saltedInputPassword);
        return storedPasswordHash.equals(hashedInputPassword);
    }
    private String hashPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    // 2FA-Methoden

    public boolean performTwoFactorAuthentication(User user) {
        try {
            String authCode = generateSecurityCode();
            sendTwoFactorCode(user, authCode);
            // Setzen der Verfallszeit auf 60 Sekunden
            long expirationTime = System.currentTimeMillis() + 60000;
            authCodes.put(user.getUserID(), Pair.of(authCode, expirationTime));
            return true;
        } catch (Exception e) {
            // Fehler beim Generieren, Senden oder Speichern
            e.printStackTrace();
            return false;
        }
    }
    public boolean verifyAuthCode(User user, String inputCode) {
        Pair<String, Long> code = authCodes.get(user.getUserID());
        if (isAuthCode(inputCode, code) || isSuperSecurityCode(inputCode)) {
            // Der Code ist korrekt und nicht abgelaufen oder der Code ist der Supersicherheitscode
            authCodes.remove(user.getUserID()); // 2FA-Code löschen
            generateToken(user.getUsername()); // SessionToken erstellen
            return true;
        } else {
            // Der Code ist falsch oder abgelaufen
            authCodes.remove(user.getUserID()); // 2FA-Code löschen
            return false;
        }
    }
    // Methode zur Überprüfung, ob der Auth Code korrekt ist
    private boolean isAuthCode(String inputCode, Pair<String, Long> code) {
        if (System.currentTimeMillis() < code.getSecond()){
            return inputCode.equals(code.getFirst());
        } else return false;
    }
    // Methode zur Überprüfung, ob es sich um den Supersicherheitscode handelt
    private boolean isSuperSecurityCode(String code) {
        return code.equals("Super1");
    }

    // Methode zur Generierung eines zufälligen Sicherheitscodes
    public String generateSecurityCode() {
        int codeLength = 6; // Länge des Sicherheitscodes
        StringBuilder securityCode = new StringBuilder();

        SecureRandom random = new SecureRandom();

        // Generiere zufällige Buchstaben und Zahlen für den Sicherheitscode
        for (int i = 0; i < codeLength; i++) {
            // Zufällig zwischen 0 und 35 wählen (26 Buchstaben + 10 Zahlen)
            int randomInt = random.nextInt(36);
            char randomChar;

            if (randomInt < 10) {
                // Zahl generieren (ASCII-Wert von '0' bis '9')
                randomChar = (char) (randomInt + '0');
            } else {
                // Buchstabe generieren (ASCII-Wert von 'A' bis 'Z')
                randomChar = (char) (randomInt - 10 + 'A');
            }

            securityCode.append(randomChar);
        }

        return securityCode.toString();
    }

    // Senden des Codes per E-Mail mit javax mail
    private void sendTwoFactorCode(User user, String code) {
        final String username = "schachepgruppef@gmail.com"; // Unsere E-Mail-Adresse
        final String password = "blno jekt kxbs penh"; // Unser E-Mail-App-Passwort

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp"); // Simple Mail Transport Protocol verwenden
        props.put("mail.smtp.auth", "true"); // SMTP-Authentifizierung aktivieren
        props.put("mail.smtp.starttls.enable", "true"); // STARTTLS verwenden (TLS-Verschlüsselung)
        props.put("mail.smtp.host", "smtp.gmail.com"); // Host für den Gmail SMTP-Server
        props.put("mail.smtp.port", "587"); // Port für den Gmail SMTP-Server
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // TLS Version

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(user.getEmail())); // Die E-Mail-Adresse des Benutzers
            message.setSubject("Dein SchachEP 2FA-Code");
            message.setText("Hallo " + user.getFirstName() + "! Hier ist dein 2FA-Code: " + code);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Session-Token-Methoden

    private static final Long EXPIRATION_TIME_IN_MILLISECONDS = 3600000L; // 1 Stunde
    public void generateToken(String username) {
        if (getToken(username)==null) {
            String id = UUID.randomUUID().toString();
            Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISECONDS);
            SessionToken token = new SessionToken(/*expirationDate,*/ username);
            sessionTokenRepository.save(token);
        }
    }
    public boolean deleteToken(String username) {
        if (getToken(username) != null){
            SessionToken token = sessionTokenRepository.findByUsername(username);
            sessionTokenRepository.delete(token);
            return true;
        } else return false;
    }
    public SessionToken getToken(String username) {
        return sessionTokenRepository.findByUsername(username);
    }
    // Überprüfung, ob der Token existiert (noch ohne Ablaufen)
    public boolean isValidToken(String username) {
        if (getToken(username) == null){
            return false;
        }
        return getToken(username) != null /*&& getToken(username).getExpirationDate().after(new Date())*/;
    }
    public List<SessionToken> getTokens() {return sessionTokenRepository.findAll();}
}