package com.itzroma.astrocornerapi.email;

public interface EmailSender {
    void send(String receiver, String email);
}
