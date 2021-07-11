package com.abc.complaints.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SessionTest {

    @Test
    void testPutAttributeInSession() {
        Session session = new Session();
        session.setGlobalId("dcbfh1hwf8lkvn5w8rgju3bwvkn");

        session.putAttribute("Attribute1", "value1");

        Assertions.assertEquals(session.getAttribute("Attribute1"), "value1");
    }

    @Test
    void removeAttribute() {
        Session session = new Session();
        session.setGlobalId("dcbfh1hwf8lkvn5w8rgju3bwvkn");

        session.putAttribute("Attribute1", "value1");

        Assertions.assertEquals(session.getAttribute("Attribute1"), "value1");

        session.removeAttribute("Attribute1");

        Assertions.assertNull(session.getAttribute("Attribute1"));
    }
}