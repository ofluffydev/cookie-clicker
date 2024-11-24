module com.kadenfrisk.cookieclicker {
    requires javafx.controls;

    opens com.kadenfrisk.cookieclicker to javafx.fxml;
    exports com.kadenfrisk.cookieclicker;
}