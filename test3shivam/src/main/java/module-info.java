module org.example.test3shivam {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.test3shivam to javafx.fxml;
    exports org.example.test3shivam;
}