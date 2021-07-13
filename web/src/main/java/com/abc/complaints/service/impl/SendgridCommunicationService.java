package com.abc.complaints.service.impl;

import com.abc.complaints.config.SendgridConfig;
import com.abc.complaints.service.CommunicationService;
import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * This service is fo managing emails with sendGrid
 */
@Service
public class SendgridCommunicationService implements CommunicationService {
    private final SendgridConfig sendgridConfig;

    public SendgridCommunicationService(SendgridConfig sendGridConfig) {
        this.sendgridConfig = sendGridConfig;
    }

    /**
     * Send an email to user using sendgrid config, and the parameters provided in the context.
     */
    @Override
    public void communicate(String sendTo, Map<String, String> context, String template) throws IOException {
        Email from = new Email(sendgridConfig.getSender());
        Email to = new Email(sendTo);

        Personalization personalization = createPersonalizationForMail(context, to);
        Mail mail = prepareMail(template, personalization, from);

        sendMail(mail);
    }

    /**
     * Add context fields into email personalization
     */
    private Personalization createPersonalizationForMail(Map<String, String> context, Email sendTo) {
        Personalization personalization = new Personalization();
        for (Map.Entry<String, String> entry : context.entrySet()) {
            personalization.addDynamicTemplateData(entry.getKey(), entry.getValue());
        }
        personalization.addTo(sendTo);
        return personalization;
    }

    /**
     * Prepare for sending the email, using a specific template, personalization, and sender
     */
    private Mail prepareMail(String template, Personalization personalization, Email sender) {
        Mail mail = new Mail();
        mail.setTemplateId(template);
        mail.addPersonalization(personalization);
        mail.setFrom(sender);

        return mail;
    }

    private void sendMail(Mail mail) throws IOException {
        SendGrid sendGrid = new SendGrid(sendgridConfig.getApiKey());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sendGrid.api(request);
    }
}