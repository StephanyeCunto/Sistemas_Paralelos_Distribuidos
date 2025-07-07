module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires static lombok;

    opens com to javafx.fxml;
    exports com;

   // exports com to java.rmi;

}