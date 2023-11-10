package com.example.oks.tokenRing;

import Exceptions.PackageException;
import com.example.oks.data.DataPackage;
import com.fazecast.jSerialComm.SerialPort;
import communication.Consumer;
import communication.Produser;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Stantion {
    private byte sourceAddres;
    private byte[] buffer;
    private Semaphore semaphore;
    private Produser produser;
    private Consumer consumer;
    private byte[] data;

    public String getCurrentPackage() {
        if (currentPackage == null) return "";
        else return Arrays.toString(currentPackage);
    }

    private byte[] currentPackage;
    private byte destinationAddres;

    public byte[] getOutputData() {
        return outputData;
    }

    private byte[] outputData = new byte[5];
    private boolean isMonitor;

    public Stantion(byte SA, Semaphore semaphore, Produser produser, Consumer consumer) {
        this.sourceAddres = SA;
        this.semaphore = semaphore;
        this.consumer = consumer;
        this.produser = produser;
    }

    public Runnable stationThread = () -> {

        RingPackage ring = new RingPackage();
        if (isMonitor) {
            produser.sendPackage(ring.generateToken());
        }
        while (true) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            buffer = consumer.acceptPackage();
            currentPackage = buffer;
            if (buffer == null) {
                semaphore.release();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                continue;
            }

            System.out.println(Arrays.toString(buffer));
            if (ring.isFrame(buffer)) {
                byte length = buffer[5];
                System.out.println(length);
                System.out.println("захвачен кадр");
                if (buffer[4] == sourceAddres && buffer[8 + length] == 51) {
                    byte[] token = ring.generateToken();
                    produser.sendPackage(token);
                } else if (buffer[3] == sourceAddres) {

                    outputData = new byte[length];
                    System.arraycopy(buffer, 6, outputData, 0, length);
                    buffer[8 + length] = 51;
                    produser.sendPackage(buffer);
                } else
                    produser.sendPackage(buffer);

            } else if (ring.isToken(buffer)) {
                System.out.println("захвачен токен");
                if (data != null) {
                    produser.sendPackage(ring.toFrame(buffer, destinationAddres, sourceAddres, data));
                    data = null;
                } else {
                    produser.sendPackage(buffer);
                }
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            semaphore.release();
            currentPackage = null;
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

        }
    };

    public String getOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        if (outputData != null) {
            for (byte outputDatum : outputData) {
                stringBuilder.append(((char) outputDatum));
            }
        }
        System.out.println(stringBuilder.toString() + " " + sourceAddres + " ");

        outputData = new byte[5];
        return stringBuilder.toString();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDestinationAddres(byte destinationAddres) {
        this.destinationAddres = destinationAddres;
    }

    public boolean isMonitor() {
        return isMonitor;
    }

    public void setMonitor(boolean monitor) {
        isMonitor = monitor;
    }
}
