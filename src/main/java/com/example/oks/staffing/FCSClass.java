package com.example.oks.staffing;

public class FCSClass {
    private byte fcs;

    public FCSClass(byte[] pack) {
        byte[] data = {pack[3], pack[4], pack[5], pack[6], pack[7]};
        byte fcs = getControlSum(data);
    }

    public static byte[] getFCS(byte[] pack, boolean staff) {
        byte[] data = {pack[3], pack[4], pack[5], pack[6], pack[7]};
        byte temp = getControlSum(data);
        if (!staff) data = Error.randomError(data);
        pack[3] = data[0];
        pack[4] = data[1];
        pack[5] = data[2];
        pack[6] = data[3];
        pack[7] = data[4];
        pack[8] = temp;

        return pack;
    }

    public static byte getControlSum(byte[] data) {
        char findCRC[] = (toBinaryString(data[0]) +
                toBinaryString(data[1]) +
                toBinaryString(data[2]) +
                toBinaryString(data[3]) +
                toBinaryString(data[4]) +
                "00000000").toCharArray();

        for (int i = 0; i < 40; i++) {
            while (findCRC[i] == '0' && (i < 39)) i++;
            findCRC = getRemaind(findCRC, i);
        }

        return (byte) ((findCRC[findCRC.length - 1] - '0') +
                ((findCRC[findCRC.length - 2] - '0') * 2) +
                ((findCRC[findCRC.length - 3] - '0') * 4) +
                ((findCRC[findCRC.length - 4] - '0') * 8) +
                ((findCRC[findCRC.length - 5] - '0') * 16) +
                ((findCRC[findCRC.length - 6] - '0') * 32) +
                ((findCRC[findCRC.length - 7] - '0') * 64) +
                ((findCRC[findCRC.length - 8] - '0') * 128));
    }

    private static char[] getRemaind(char[] data, int index) {
        char[] polynom = "100110001".toCharArray();
        for (int i = 0; i < 9; i++) {
            if (data[index + i] == polynom[i]) data[index + i] = '0';
            else data[index + i] = '1';
        }
        return data;
    }

    private static String toBinaryString(byte b) {
        StringBuilder binary = new StringBuilder(Integer.toBinaryString(b));
        while (binary.length() < 8) binary.insert(0, "0");
        return binary.toString();
    }
}
