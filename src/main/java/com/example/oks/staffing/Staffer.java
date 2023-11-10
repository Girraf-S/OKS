package com.example.oks.staffing;

import Exceptions.PackageException;
import com.example.oks.data.Buffer;
import com.example.oks.data.DataPackage;
import communication.Consumer;

public class Staffer {

    public static byte[] staffing(byte SA) {
        byte[] tempBuffer = {
                DataPackage.FLAG, 0, SA, DataPackage.STAFFING_BYTE,
                DataPackage.STAFFING_BYTE, DataPackage.STAFFING_BYTE,
                DataPackage.STAFFING_BYTE, DataPackage.STAFFING_BYTE, 0};
        byte flag = DataPackage.FLAG;
        boolean staffingHappened = false;
        for (int i = 3; i < 3 + DataPackage.DATA_LENGTH; i++) {
            byte temp;
            if (!Buffer.isEmpty()) {
                temp = Buffer.poll();
            } else {
                break;
            }
            if (temp == flag) {
                staffingHappened = true;
                tempBuffer[i] = DataPackage.STAFFING_BYTE;
                if (++i < DataPackage.DATA_LENGTH + 3)
                    tempBuffer[i] = temp;
                else Buffer.addFirst(flag);
            } else tempBuffer[i] = temp;
        }

        tempBuffer = FCSClass.getFCS(tempBuffer, staffingHappened);
        return tempBuffer;
    }
    public static String deStaffing(DataPackage dataPackage) throws PackageException {
        byte[] tempBuffer = dataPackage.getPackage();
        if (tempBuffer[0] != DataPackage.FLAG) throw new PackageException("Can't read the package. Flag is corrupted.");
        if (tempBuffer.length != 9) throw new PackageException("Can't read the package. Byte order is out of order.");
        if (dataPackage.getFCS() != FCSClass.getFCS(tempBuffer, true)[8]) {
            int index = 0;
            for (int i = 0; i < 40; i++) {
                if ((index = Consumer.findFailBit(tempBuffer, i, dataPackage.getFCS())) != -1) break;
            }

            int indexOfByte = index / 8, indexOfBit = index % 8;

            tempBuffer[3 + indexOfByte] ^= (byte) Math.pow(2, indexOfBit);
        }
        byte data[] = new byte[5];
        int j = 0;
        for (int i = 3; i < 8; i++, j++) {
            if (tempBuffer[i] == DataPackage.STAFFING_BYTE) {
                while (tempBuffer[i] == DataPackage.STAFFING_BYTE) i++;
                if(i<8)data[j] = tempBuffer[i];
            } else data[j] = tempBuffer[i];
        }
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i <= j && i < DataPackage.DATA_LENGTH; i++) {
            result.append(String.valueOf((char) data[i]));

        }
        System.out.println(result);
        return result.toString();
    }
}
