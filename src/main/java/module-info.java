module com.example.oks {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires java.datatransfer;

    opens com.example.oks to javafx.fxml;
    exports com.example.oks;
    exports Exceptions;
    opens Exceptions to javafx.fxml;
    exports com.example.oks.data;
    opens com.example.oks.data to javafx.fxml;
    exports com.example.oks.controllers;
    opens com.example.oks.controllers to javafx.fxml;
}