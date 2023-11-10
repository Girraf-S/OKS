package com.example.oks.collision;

import Exceptions.PackageException;
import com.example.oks.PortsInitializer;
import com.example.oks.data.DataPackage;
import communication.Consumer;
import com.example.oks.staffing.Error;
import com.example.oks.staffing.Staffer;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class Collision {
    private static Signal signal = Signal.CONTINUE;

    public static Signal isHapend(DataPackage send, DataPackage accept) {
        Signal signal;
        if (!send.equals(accept)) signal = Signal.JAM;
        else signal = Signal.CONTINUE;
        return signal;
    }

    public static boolean chanelBusy() {
        LocalTime time = LocalTime.now();
        return (time.getSecond() % 8) < 5;
    }

    private static int randomDelay(int c) {
        return (int) (Math.random() * ((int) Math.pow(2, Math.min(c, 10)) + 1));
    }

    public static DataPackage sendCollision(int packCounter, boolean lab4) {
        int attemtCounter = 0, colWin = 100;
        signal = Signal.CONTINUE;
        DataPackage inputPackage = null;
        try {
            inputPackage = new DataPackage(Staffer.staffing(
                    (byte) Integer.parseInt(
                            PortsInitializer.getProduserPort()
                                    .getSystemPortName().substring(3))));
        } catch (PackageException e) {
            throw new RuntimeException(e);
        }
        if(!lab4)return inputPackage;
        DataPackage collisionPackage = null;
        do {
            collisionPackage = Error.collision(inputPackage);
            System.out.println("sendCollision" + collisionPackage);
            if (!inputPackage.equals(collisionPackage)) {
                signal = Signal.JAM;
                attemtCounter++;
                if (packCounter == 1) {
                    if (attemtCounter < 6) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(randomDelay(attemtCounter));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else signal = Signal.ERROR;
                } else signal = Signal.ERROR;
            }else signal = Signal.CONTINUE;
        } while (signal == Signal.JAM);
        return collisionPackage;
    }

    public static DataPackage acceptCollision(Consumer consumer, boolean lab4) {
        signal = Signal.CONTINUE;
        DataPackage acceptPackage;
        try {
            acceptPackage = consumer.acceptData();
        } catch (PackageException e) {
            throw new RuntimeException(e);
        }
        if(!lab4)return acceptPackage;
        DataPackage collisionPackage = Error.collision(acceptPackage);
        System.out.println("acceptCollision" + collisionPackage);
        if (collisionPackage.equals(acceptPackage)) {
            signal = Signal.CONTINUE;
        } else signal = Signal.ERROR;
        return collisionPackage;
    }

    public static Signal getSignal() {
        return signal;
    }
}
