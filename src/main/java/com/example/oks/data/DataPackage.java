package com.example.oks.data;

import Exceptions.PackageException;

import java.util.Arrays;

public class DataPackage {
    public static final int DATA_LENGTH = 5;
    public static final byte FLAG = (byte) ('Z' + DATA_LENGTH);
    public static final byte STAFFING_BYTE = (byte) '$';
    private byte flag;
    private byte destinitionAddress;
    private byte sourceAddress;
    private byte[] data = new byte[5];
    private byte FCS;

    public DataPackage(byte[] data, byte portNumber) {
        flag = FLAG;
        destinitionAddress = 0;
        FCS = 0;
        sourceAddress = portNumber;
    }

    public DataPackage(byte[] pack) throws PackageException {
//        if (pack.length < 9) {
//            throw new PackageException("Package is corrupted");
//        }
        flag = pack[0];
        destinitionAddress = pack[1];
        sourceAddress = pack[2];
        for (int i = 3, j = 0; i < 8; i++, j++)
            data[j] = pack[i];
        FCS = pack[8];
    }

    public DataPackage() {
    }

    public byte[] getPackage() {
        byte[] pack = new byte[9];
        pack[0] = DataPackage.FLAG;
        pack[1] = destinitionAddress;
        pack[2] = sourceAddress;
        for (int i = 3, j = 0; i < 8; i++, j++)
            pack[i] = data[j];
        pack[8] = FCS;
        return pack;
    }

    public byte getFlag() {
        return flag;
    }

    public byte getDestinitionAddress() {
        return destinitionAddress;
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public byte[] getData() {
        return data;
    }

    public byte getFCS() {
        return FCS;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String Sflag = String.valueOf((char) this.flag);
        String SdestinitionAddress = String.format("%d", this.destinitionAddress);
        String SsourceAddress = String.format("%d", this.sourceAddress);
        String Sdata = String.valueOf((char) (this.data[0])) +
                String.valueOf((char) (this.data[1])) +
                String.valueOf((char) (this.data[2])) +
                String.valueOf((char) (this.data[3])) +
                String.valueOf((char) (this.data[4]));
        String SFCS = String.format("%d", (this.FCS));
        return Sflag + " " + SdestinitionAddress + " " + SsourceAddress + " " + Sdata + " " + SFCS;
    }

    public String getCode() {
        return
                Integer.toBinaryString(this.flag) + " " +
                        Integer.toBinaryString(this.destinitionAddress) + " " +
                        Integer.toBinaryString(this.sourceAddress) + " " +
                        Integer.toBinaryString(this.data[0]) +
                        Integer.toBinaryString(this.data[1]) +
                        Integer.toBinaryString(this.data[2]) +
                        Integer.toBinaryString(this.data[3]) +
                        Integer.toBinaryString(this.data[4]) + " " +
                        Integer.toBinaryString(this.FCS);
    }

    public boolean equals(DataPackage dataPackage) {
        return this.flag == dataPackage.flag
                && this.sourceAddress == dataPackage.sourceAddress
                && this.destinitionAddress == dataPackage.destinitionAddress
                && Arrays.equals(this.data, dataPackage.data)
                && this.FCS == dataPackage.FCS;
    }

}
