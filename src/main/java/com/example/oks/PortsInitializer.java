package com.example.oks;

import Exceptions.PackageException;
import com.fazecast.jSerialComm.SerialPort;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PortsInitializer {
    private static SerialPort produserPort;
    private static SerialPort consumerPort;

    public static boolean init() {
        int computer = new Date().getMinutes() % 2;
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        Map<String, SerialPort> serialPortMap = new HashMap<>();
        for (SerialPort serialPort : serialPorts) {
            serialPortMap.put(serialPort.getSystemPortName(), serialPort);

        }
//        serialPortMap.put(serialPorts[4].getSystemPortName(), serialPorts[4]);
//        serialPortMap.put(serialPorts[5].getSystemPortName(), serialPorts[5]);
//        serialPortMap.put(serialPorts[6].getSystemPortName(), serialPorts[6]);
//        serialPortMap.put(serialPorts[7].getSystemPortName(), serialPorts[7]);
        if (computer == 1) {
            produserPort = serialPortMap.get("COM1");
            consumerPort = serialPortMap.get("COM14");
        } else {
            produserPort = serialPortMap.get("COM2");
            consumerPort = serialPortMap.get("COM15");
        }
        if (!(produserPort.openPort() && consumerPort.openPort())) {
            return false;
        }
        produserPort.setParity(SerialPort.NO_PARITY);
        produserPort.setBaudRate(9600);
        produserPort.setNumDataBits(8);
        produserPort.setNumStopBits(1);

        consumerPort.setParity(SerialPort.NO_PARITY);
        consumerPort.setBaudRate(9600);
        consumerPort.setNumDataBits(8);
        consumerPort.setNumStopBits(1);
        return true;
    }

    public PortsInitializer() {
    }

    public static SerialPort getProduserPort() {
        return produserPort;
    }

    public static SerialPort getConsumerPort() {
        return consumerPort;
    }

    public static void setParity(boolean parity) { //true - четный, false - нечетный
        if (parity) {
            produserPort.setParity(SerialPort.EVEN_PARITY);
            consumerPort.setParity(SerialPort.EVEN_PARITY);
        } else {
            produserPort.setParity(SerialPort.ODD_PARITY);
            consumerPort.setParity(SerialPort.ODD_PARITY);
        }
    }
}
