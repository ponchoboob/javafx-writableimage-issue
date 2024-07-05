module com.example.fximage {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fximage to javafx.fxml;
    exports com.example.fximage;
}