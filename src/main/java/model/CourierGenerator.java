package model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static Courier random(){
        return new Courier(RandomStringUtils.randomAlphabetic(5),"123123", "Name");
    }
}
