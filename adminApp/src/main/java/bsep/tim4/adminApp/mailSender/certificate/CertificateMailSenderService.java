package bsep.tim4.adminApp.mailSender.certificate;

import bsep.tim4.adminApp.mailSender.certificate.CertificateLink;
import bsep.tim4.adminApp.mailSender.certificate.CertificateLinkRepository;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.CertificateData;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class CertificateMailSenderService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private CertificateLinkRepository certificateLinkRepository;

    @Autowired
    Environment env;

    @Async
    @Transactional
    public void sendCertificateLink(CertificateData certData) throws MessagingException {
        String token = UUID.randomUUID().toString();
        createCertificateLink(token, certData);

        String recipientAddress = certData.getEmail();
        String subject = "Certificate download address";
        String certificateUrl
                = "http://localhost:8080" + "/api/certificate/download?token=" + token;

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
                "Follow the link to download your certificate!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#797c7f;font-family:'Fakt Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px 0\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "<p style=\"outline:none;margin:0;padding:0\">Follow the link to complete the download: " + certificateUrl +"</p>\n" +
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
                "Hope this message finds you well!\n" +
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
    public void sendCertificateRevoked(CertificateData certData) throws MessagingException {
        String token = UUID.randomUUID().toString();

        String recipientAddress = certData.getEmail();
        String subject = "Certificate has been revoked";

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
                "Your certificate has been revoked!\n" +
                "\n" +
                "</td>\n" +
                "\n" +
                "</tr>\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "<td style=\"outline:none;width:100%;color:#797c7f;font-family:'Fakt Pro','Segoe UI','SanFrancisco Display',Arial,sans-serif;font-size:14px;font-style:normal;font-weight:normal;line-height:24px;word-spacing:0;margin:0;padding:50px 80px 0\" align=\"left\" valign=\"top\">\n" +
                "\n" +
                "<p style=\"outline:none;margin:0;padding:0\">Follow the link to complete the download: " + certificateUrl +"</p>\n" +
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
                "Hope this message finds you well!\n" +
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

    private CertificateLink createCertificateLink(String token, CertificateData certData) {
        CertificateLink certificateLink = new CertificateLink(token, certData);
        return certificateLinkRepository.save(certificateLink);
    }

}
