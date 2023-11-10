package com.example.oks;

import com.example.oks.tokenRing.StationInitilizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StationInitilizer.init();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tokenRing.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 810, 620);
        stage.setTitle("Token Ring interface");
        stage.setScene(scene);
        stage.show();
//        if(!PortsInitializer.init()) {
//            System.out.println("fail open ports");
//        }else {
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("portsApp.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 550, 640);
//            stage.setTitle("COM ports interface");
//            stage.setScene(scene);
//            stage.show();
//
//        }
    }

    public static void main(String[] args) {
        launch();
    }
}