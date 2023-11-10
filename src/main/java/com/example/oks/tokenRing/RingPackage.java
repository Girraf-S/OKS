package com.example.oks.tokenRing;

import com.example.oks.staffing.FCSClass;

public class RingPackage {
    private byte[] pack;
    private byte currentPriority;


    public boolean isToken(byte[] newPackage) {
        return newPackage.length == 3;
    }

    public boolean isFrame(byte[] newPackage) {
        return newPackage.length >10;
    }

    public byte[] toFrame(byte[] token, byte DA, byte SA, byte[] info) {
        byte length = (byte) info.length;
        System.out.println(length);
        pack = new byte[10 + length];
        pack[0] = token[0];
        pack[1] = (byte) (token[1] ^ 8);
        pack[3] = DA;
        pack[4] = SA;
        pack[5] = length;
        System.arraycopy(info, 0, pack, 6, length);
        pack[6 + length] = FCSClass.getControlSum(info);
        pack[7 + length] = token[2];
        pack[8 + length] = 0;//00110011
        return pack;
    }

    public byte[] generateToken() {
        return new byte[]{'$', 0, '$'};
    }

    public void setCurrentPriority(byte currentPriority) {
        this.currentPriority = currentPriority;
    }
}
