module io.lok1s.nahod {
    requires javafx.fxml;
    requires eu.hansolo.applefx;
    requires eu.hansolo.jdktools;
    requires java.desktop;
    requires javafx.controls;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.iostreams;
    requires org.apache.commons.io;
    opens io.lok1s.nahod to javafx.fxml;
    exports io.lok1s.nahod;
}