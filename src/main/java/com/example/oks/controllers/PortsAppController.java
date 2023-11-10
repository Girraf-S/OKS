package com.example.oks.controllers;

import Exceptions.PackageException;
import com.example.oks.PortsInitializer;
import com.example.oks.collision.Collision;
import com.example.oks.collision.Signal;
import com.example.oks.data.Buffer;
import com.example.oks.data.DataPackage;
import communication.Consumer;
import communication.Produser;
import com.example.oks.staffing.Staffer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.*;

public class PortsAppController {
    @FXML
    TextArea inputField;
    @FXML
    TextArea outputField;
    @FXML
    TextField portsPair;
    @FXML
    TextField countBytes;
    @FXML
    TextArea staffingArea;
    @FXML
    TextArea inCollPack;
    @FXML
    TextArea outCollPack;
    @FXML
    ToggleButton collisionButton;
    boolean lab4 = true;
    Produser produser = new Produser(PortsInitializer.getProduserPort());
    Consumer consumer = new Consumer(PortsInitializer.getConsumerPort());

    @FXML
    protected void CollisionButtonClicked(){
        lab4 = !lab4;
        if(lab4)collisionButton.setText("ON");
        else collisionButton.setText("OFF");
    }
    @FXML
    protected void mouseClicked() {
        if(lab4) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable editable = () -> {
                inputField.setEditable(Collision.chanelBusy());
            };
            executor.scheduleAtFixedRate(editable, 0, 1, TimeUnit.SECONDS);
        }
    }


    @FXML
    protected void enterClicked(KeyEvent event) throws InterruptedException {

        if (event.getCode() == KeyCode.ENTER) {
            String[] temp = inputField.getText().split("\n");
            byte[] buffer = (temp[temp.length - 1] + "\n").getBytes();
            Buffer.add(buffer);
            int packCounter = 0;
            while (!Buffer.isEmpty()) {
                DataPackage potentionalCollisionPack = produser.sendData(Collision.
                        sendCollision(++packCounter, lab4).getPackage());

                if (Collision.getSignal().equals(Signal.ERROR)) {
                    System.out.println("input" + potentionalCollisionPack.toString());
                    inCollPack.setText(inCollPack.getText() + potentionalCollisionPack.toString() + "\n");
                }
                String data = "";
                DataPackage dataPackage;
                try {
                    dataPackage = Collision.acceptCollision(consumer, lab4);//consumer.acceptData();
                    if (Collision.getSignal().equals(Signal.ERROR)) {
                        System.out.println("output" + outCollPack);
                        outCollPack.setText(outCollPack.getText() + dataPackage.toString() + "\n");
                    }

                    staffingArea.setText(staffingArea.getText() + dataPackage.toString() + "\n");
                    data = Staffer.deStaffing(dataPackage);
                    outputField.setText(outputField.getText() + data);
                } catch (PackageException e) {
                    data = e.getMessage();
                    outputField.setText(outputField.getText() + data);
                }

            }
        }
        countBytes.setText(consumer.getCounterBytes().toString());
    }


    @FXML
    protected void evenSelected() {
        PortsInitializer.setParity(true);
    }

    @FXML
    protected void oddSelected() {
        PortsInitializer.setParity(false);
    }

    @FXML
    protected void pairOfPortsClick() {
        portsPair.setText(
                PortsInitializer.getProduserPort().getSystemPortName()
                        + " -> " +
                        PortsInitializer.getConsumerPort().getSystemPortName()
        );
    }

    @FXML
    protected void transferredBytesClicked() {
        countBytes.setText(consumer.getCounterBytes().toString());
    }


}
