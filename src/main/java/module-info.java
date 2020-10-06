/**
 * $Id$
 */
module framework.laboratory {
    requires java.desktop;
	requires java.logging;
	requires java.prefs;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;

    requires framework.smack;

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
    exports de.michab.lab
        to framework.smack;
}
