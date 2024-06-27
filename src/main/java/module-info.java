module org.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires layout;
    requires kernel;
    requires javafx.media;
    requires javafx.graphics;



    opens org.example.demo2 to javafx.fxml;
    exports org.example.demo2;
}
