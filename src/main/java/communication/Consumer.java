package communication;

import Exceptions.PackageException;
import com.example.oks.data.DataPackage;
import com.example.oks.staffing.FCSClass;
import com.fazecast.jSerialComm.SerialPort;

public class Consumer {
    private SerialPort port;
    private static int counterBytes = 0;

    public Consumer(SerialPort port) {
        this.port = port;
    }


    public DataPackage acceptData() throws PackageException {
        byte[] buffer = new byte[2048];
        int bytesRead = port.readBytes(buffer, buffer.length);
        try {
            DataPackage dataPackage = new DataPackage(buffer);
            counterBytes += bytesRead;
            return dataPackage;
        } catch (PackageException e) {
            throw new PackageException(e.getMessage());
        }
    }

    public byte[] acceptPackage() {
        byte[] buffer = new byte[2048];
        int bytesRead = port.readBytes(buffer, buffer.length);
        byte[] pack = null;
        if (bytesRead > 0) {
            pack = new byte[bytesRead];
            System.arraycopy(buffer, 0, pack, 0, bytesRead);
        }
        return pack;
    }

    public static int findFailBit(byte[] pack, int index, byte fcs) {
        byte data[] = {pack[3], pack[4], pack[5], pack[6], pack[7]};
        int indexOfByte = index / 8, indexOfBit = index % 8;
        data[indexOfByte] ^= (byte) Math.pow(2, indexOfBit);
        if (FCSClass.getControlSum(data) == fcs) return index;
        else return -1;
    }

    public SerialPort getPort() {
        return port;
    }

    public static Integer getCounterBytes() {
        return counterBytes;
    }
}
