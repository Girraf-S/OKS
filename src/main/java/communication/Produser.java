package communication;

import Exceptions.PackageException;
import com.example.oks.data.DataPackage;
import com.fazecast.jSerialComm.SerialPort;

public class Produser {
    private SerialPort port;
    public Produser(SerialPort port){
        this.port = port;
    }
    public DataPackage sendData(byte[] bytes){
        port.writeBytes(bytes, bytes.length);
        try {
            return new DataPackage(bytes);
        } catch (PackageException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendPackage(byte[] bytes){
        port.writeBytes(bytes, bytes.length);
    }

    public SerialPort getPort() {
        return port;
    }
}
