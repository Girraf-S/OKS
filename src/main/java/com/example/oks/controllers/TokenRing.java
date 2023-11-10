package com.example.oks.controllers;

import com.example.oks.tokenRing.Stantion;
import com.example.oks.tokenRing.StationInitilizer;
import com.fazecast.jSerialComm.SerialPort;
import communication.Consumer;
import communication.Produser;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class TokenRing {
    public TextArea inputField1;
    public TextArea outputField1;
    public RadioButton monitor1;
    public ToggleGroup monitor;
    public TextArea package1;
    public TextArea inputField2;
    public TextArea outputField2;
    public RadioButton monitor2;
    public TextArea package2;
    public TextArea inputField3;
    public TextArea outputField3;
    public RadioButton monitor3;
    public TextArea package3;
    public Button button3;
    public Button button2;
    public Button button1;
    public TextField DA1;
    public TextField DA2;
    public TextField DA3;
    Semaphore semaphore = new Semaphore(1);

    Stantion stantion1 = new Stantion((byte) 1, semaphore,
            new Produser(StationInitilizer.getPort("COM1")),
            new Consumer(StationInitilizer.getPort("COM15")));
    Stantion stantion2 = new Stantion((byte) 2, semaphore,
            new Produser(StationInitilizer.getPort("COM2")),
            new Consumer(StationInitilizer.getPort("COM16")));
    Stantion stantion3 = new Stantion((byte) 3, semaphore,
            new Produser(StationInitilizer.getPort("COM3")),
            new Consumer(StationInitilizer.getPort("COM14")));
    public void button3Clicked(ActionEvent actionEvent) {
        stantion3.setDestinationAddres(Byte.parseByte(DA3.getText()));
        stantion3.setData(inputField3.getText().getBytes());

    }

    public void button2Clicked(ActionEvent actionEvent) {
        stantion2.setDestinationAddres(Byte.parseByte(DA2.getText()));
        stantion2.setData(inputField2.getText().getBytes());
    }

    public void button1Clicked(ActionEvent actionEvent) {
        stantion1.setDestinationAddres(Byte.parseByte(DA1.getText()));
        stantion1.setData(inputField1.getText().getBytes());
    }

    public void monitor3Selected(ActionEvent actionEvent) {
        stantion1.setMonitor(false);
        stantion2.setMonitor(false);
        stantion3.setMonitor(true);
    }

    public void monitor2Selected(ActionEvent actionEvent) {
        stantion1.setMonitor(false);
        stantion2.setMonitor(true);
        stantion3.setMonitor(false);
    }

    public void monitor1Selected(ActionEvent actionEvent) {
        stantion1.setMonitor(true);
        stantion2.setMonitor(false);
        stantion3.setMonitor(false);
    }

    boolean start = false;
    public void mainStart(MouseEvent mouseEvent) {
        if(!start) {
            Runnable uploadOutput = () -> {
                while (true) {
                    if (stantion1.getOutputData() != null)
                        outputField1.setText(outputField1.getText() + stantion1.getOutput());
                    if (stantion2.getOutputData() != null)
                        outputField2.setText(outputField2.getText() + stantion2.getOutput());
                    if (stantion3.getOutputData() != null)
                        outputField3.setText(outputField3.getText() + stantion3.getOutput());
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Runnable packagePursuit = () -> {
                while (true) {
                    package1.setText(stantion1.getCurrentPackage());
                    package2.setText(stantion2.getCurrentPackage());
                    package3.setText(stantion3.getCurrentPackage());
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Thread stationThread1 = new Thread(stantion1.stationThread);
            Thread stationThread2 = new Thread(stantion2.stationThread);
            Thread stationThread3 = new Thread(stantion3.stationThread);
            Thread outputThread = new Thread(uploadOutput);
            Thread pursuitThread = new Thread(packagePursuit);
            stantion1.setMonitor(true);
            outputThread.start();
            pursuitThread.start();
            stationThread1.start();
            //Thread.sleep(1000);
            stationThread2.start();
            // Thread.sleep(1000);
            stationThread3.start();
            // Thread.sleep(1000);
        }
        start = true;
    }
}
