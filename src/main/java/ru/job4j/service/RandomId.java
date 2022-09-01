package ru.job4j.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomId {

    private final static int MAX_INTEGER = 999999;

    public int randomizeId() {
        Random random = new Random();
        return random.nextInt(MAX_INTEGER);
    }
}
