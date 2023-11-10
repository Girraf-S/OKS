package com.example.oks.staffing;

import Exceptions.PackageException;
import com.example.oks.data.DataPackage;

import java.time.LocalTime;

public class Error {
    public static byte[] randomError(byte[] data) {
        //if(true) return data;
        if ((Math.random() * 10) % 2 == 1) return data;
        int randomByte, randomBit;
        randomByte = ((int) (Math.random() * 10)) % 5;
        randomBit = (int) Math.pow(2, ((int) (Math.random() * 10)) % 8);
        data[randomByte] ^= (byte) randomBit;
        //System.out.println("Error: " + randomByte + " " + randomBit);
        return data;
    }

    public static DataPackage collision(DataPackage dataPackage) {
        //int random = (int)((Math.random() * 10) % 4);
        LocalTime time = LocalTime.now();
        //(time.getSecond() % 8) < 5;
        int random = (int)(time.getSecond() % 4);
        System.out.println(random);
        if (random == 0){
            System.out.println("collision hapend!");
            byte[] bytes = dataPackage.getPackage();
            bytes[5]^=1;
            bytes[6]|=1;
            bytes[7]&=1;
            try {
                return new DataPackage(bytes);
            } catch (PackageException e) {
                throw new RuntimeException(e);
            }
        }else return dataPackage;
    }
}
