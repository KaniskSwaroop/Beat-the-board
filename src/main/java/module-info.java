module main.javafxtrial {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;


    opens Main to javafx.fxml;
    exports Main;
}