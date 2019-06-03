/**
 * $Id: module-info.java 407 2018-12-19 15:20:09Z michab66 $
 */
module framework.laboratory {
	requires java.xml.ws;
	requires java.logging;
	requires java.prefs;
	requires javafx.base;
	requires javafx.controls;
    requires javafx.media;
    requires javafx.swing;

    requires framework.smack;
    requires javafx.fxml;
    requires java.desktop;

    opens de.michab.lab.tools.xslt.resources
        to framework.smack;
    opens de.michab.lab.tools.xslt
        to framework.smack, javafx.fxml;
    exports de.michab.lab.tools.xslt;

    // Required for resource loading.
    opens de.michab.lab.nativ.resources
        to framework.smack;
    exports de.michab.lab.nativ
        to framework.smack;
}
