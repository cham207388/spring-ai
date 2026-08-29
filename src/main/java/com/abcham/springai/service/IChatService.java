package com.abcham.springai.service;


public interface IChatService {

    String answer(String message);

    String answer(String username, String message);

}
