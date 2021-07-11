package com.abc.complaints.service;

import java.io.IOException;
import java.util.Map;

public interface CommunicationService {
    void communicate(String sendTo, Map<String, String> context, String template) throws IOException;
}