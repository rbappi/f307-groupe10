module ulb.infof307.g10.app.controllers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires javafx.web;
    requires jlatexmath;
    requires org.apache.commons.lang3;
    requires java.desktop;
    requires logback.core;
    requires org.apache.commons.io;
    requires org.apache.pdfbox;
    requires org.controlsfx.controls;
    requires org.jsoup;
    requires javafx.swing;
    requires batik.svggen;


    opens ulb.infof307.g10.app.controllers to javafx.fxml;
    exports ulb.infof307.g10.app.controllers;
    exports ulb.infof307.g10.constante.appConst;
    opens ulb.infof307.g10.constante.appConst to javafx.fxml;
    exports ulb.infof307.g10.app.views.Connection;
    opens ulb.infof307.g10.app.views.Connection to javafx.fxml;
    exports ulb.infof307.g10.app.views.Card;
    opens ulb.infof307.g10.app.views.Card to javafx.fxml;
    exports ulb.infof307.g10.app.views.Deck;
    opens ulb.infof307.g10.app.views.Store to  javafx.fxml;
    exports ulb.infof307.g10.app.views.Store;
    opens ulb.infof307.g10.app.views.Deck to javafx.fxml;
    exports ulb.infof307.g10.app.views.Card.LaTex_HTML.util;
    opens ulb.infof307.g10.app.views.Card.LaTex_HTML.util to javafx.fxml;
    exports ulb.infof307.g10.abstractClass;
    opens ulb.infof307.g10.abstractClass to javafx.fxml;
}