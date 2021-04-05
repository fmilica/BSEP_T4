package bsep.tim4.adminApp.mailSender.csr;

import bsep.tim4.adminApp.pki.model.CSR;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class CsrSenderService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VerificationLinkRepository verificationLinkRepository;

    /*@Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;*/

    @Autowired
    Environment env;

    @Async
    @Transactional
    public void sendVerificationLink(CSR certificateRequest) throws MessagingException {
        String token = UUID.randomUUID().toString();
        createVerificationLink(token, certificateRequest);

        String recipientAddress = certificateRequest.getEmail();
        String subject = "Certificate Signing Request Verification";
        String verificationUrl
                = "http://localhost:8080" + "/api/csr/verification?token=" + token;

        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, "utf-8");

        email.setTo(recipientAddress);
        email.setSubject(subject);

        String htmlMsg = "<tbody><tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#17181a;font-family:'FreightSans Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:26px;font-style:normal;font-weight:normal;line-height:30px;word-spacing:0;margin:0;padding:60px 120px 0\" align=\"center\" valign=\"top\">\n" +
                "\n" +
                "Please verify your certificate signing request!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#797c7f;font-family:'Fakt Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px 0\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "<p style=\"outline:none;margin:0;padding:0\">Follow the link to complete the verification: " + verificationUrl +"</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr></tr>\n" +
                "\n" +
                "<tr><td style=\"outline:none;width:100%;color:#17181a;font-family:'FreightSans Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:18px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "Please ignore this message if you have not sent certificate signing request!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "                            \n" +
                "\n" +
                "                            \n" +
                "\n" +
                "\n" +
                "\n" +
                "                        </tr></tbody>";
        email.setText(htmlMsg, true);
        mailSender.send(mimeMessage);
    }

    @Async
    @Transactional
    public void sendCsrDeclined(CSR certificateRequest) throws MessagingException {
        String token = UUID.randomUUID().toString();

        String recipientAddress = certificateRequest.getEmail();
        String subject = "Certificate Signing Request Declined";

        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, "utf-8");

        email.setTo(recipientAddress);
        email.setSubject(subject);

        String htmlMsg = "<tbody><tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#17181a;font-family:'FreightSans Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:26px;font-style:normal;font-weight:normal;line-height:30px;word-spacing:0;margin:0;padding:60px 120px 0\" align=\"center\" valign=\"top\">\n" +
                "\n" +
                "Your certificated signing request has been denied!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#797c7f;font-family:'Fakt Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px 0\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr></tr>\n" +
                "\n" +
                "<tr><td style=\"outline:none;width:100%;color:#17181a;font-family:'FreightSans Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:18px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "Please ignore this message if you have not sent certificate signing request!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "                            \n" +
                "\n" +
                "                            \n" +
                "\n" +
                "\n" +
                "\n" +
                "                        </tr></tbody>";
        email.setText(htmlMsg, true);
        mailSender.send(mimeMessage);
    }

    private VerificationLink createVerificationLink(String token, CSR csrRequest) {
        VerificationLink verificationLink = new VerificationLink(token, csrRequest);
        return verificationLinkRepository.save(verificationLink);
    }

}
