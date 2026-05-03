
module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.java_websocket;
    requires org.json;

    opens com.example.chatsystem to javafx.fxml;
    exports com.example.chatsystem;
}