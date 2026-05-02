
module com.example.chatsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatsystem to javafx.fxml;
    exports com.example.chatsystem;
}