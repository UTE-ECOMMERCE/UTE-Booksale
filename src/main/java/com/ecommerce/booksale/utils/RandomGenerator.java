package com.ecommerce.booksale.utils;

import java.util.Random;

public class RandomGenerator {

    public static int generateRandomNumber(int upperLimit) {
        Random random = new Random();
        int randomNumber = random.nextInt(upperLimit);
        return randomNumber;
    }
}
