module io.lok1s.nahod {
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires eu.hansolo.applefx;
    requires java.desktop;
    requires javafx.controls;

    opens io.lok1s.nahod to javafx.fxml;
    exports io.lok1s.nahod;
}