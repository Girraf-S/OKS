package com.example.oks.tokenRing;

import com.fazecast.jSerialComm.SerialPort;

import java.util.HashMap;
import java.util.Map;

public class StationInitilizer {

    private static final Map<String, SerialPort> serialPortMap = new HashMap<>();

    public static void init() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (SerialPort serialPort : serialPorts) {
            serialPort.openPort();
            serialPortMap.put(serialPort.getSystemPortName(), serialPort);
        }
    }
    public static SerialPort getPort(String portName){
        return serialPortMap.get(portName);
    }
}
