package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.MockedSecurity;

public class MockedSecutiryController {

    static public boolean isOwner(MockedSecurity ms) {
        return  ifSpecial(ms);
    }
    static public boolean isUser(MockedSecurity ms) {
        return  ifSpecial(ms);
    }
    static public boolean isAdmin(MockedSecurity ms) {
        return ifSpecial(ms);
    }

    private static boolean ifSpecial(MockedSecurity ms) {
        return "bypass".equals(ms.token);
    }

}
