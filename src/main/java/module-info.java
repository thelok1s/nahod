module io.lok1s.nahod {
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires eu.hansolo.applefx;
    requires eu.hansolo.jdktools;
    requires java.desktop;
    requires javafx.controls;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.iostreams;
    requires org.apache.commons.io;

    opens io.lok1s.nahod to javafx.fxml;
    exports io.lok1s.nahod;
}