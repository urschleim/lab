/*
 *
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.smack.util.StringUtil;
import org.smack.util.TimeProbe;
import org.smack.util.xml.XmlUtil;
import org.xml.sax.SAXParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Controller.
 *
 * @version $Rev: 2163 $
 * @author Michael Binz
 */
public class XsltLabUiController
{
    @FXML private Button _buttonRefresh;
    @FXML private StackPane _xsltParent;
    @FXML private StackPane _xmlParent;
    @FXML private StackPane _resultParent;
    @FXML private Label _statusMessage;

    @FXML protected void handleButtonRefresh( ActionEvent event )
    {
        _textAreaResult.textProperty.set( StringUtil.EMPTY_STRING );

        try
        {
            TimeProbe tp = new TimeProbe().start();

            String line =
                    XmlUtil.transform(
                            new ByteArrayInputStream( _textAreaXsl.getText().getBytes() ),
                            new ByteArrayInputStream( _textAreaXml.getText().getBytes() ) );

            statusMessage( "Transformation took %s", tp.toString() );

            _textAreaResult.textProperty.set(
                    line );
        }
        catch ( Exception e )
        {
            statusMessage( e.getMessage() );

            _textAreaResult.textProperty.set(
                    getStackTrace( e ) );

            SourceLocator locator = findLocation( e );

            if ( locator != null && locator.getLineNumber() >= 0 )
                _textAreaXsl.selectLine( locator.getLineNumber() );
        }
    }

    private StylesheetView _textAreaXsl =
            new StylesheetView();
    private XmlView _textAreaXml =
            new XmlView();
    private FxLineNumberTextArea _textAreaResult =
            new FxLineNumberTextArea();

    @FXML private void initialize()
    {
        _xsltParent.getChildren().add(
                _textAreaXsl );
        _xmlParent.getChildren().add(
                _textAreaXml );
        _resultParent.getChildren().add(
                _textAreaResult );

        // TODO persist the file name instead of the text content.
//        PropertyLink.persist(
//                _textAreaXsl.fileProperty(),
//                new FileStringConverter(),
//                "_textAreaXsl");
//        PropertyLink.persist(
//                _textAreaXml.fileProperty(),
//                new FileStringConverter(),
//                "_textAreaXml" );
    }

    private void statusMessage( String format, Object ... args )
    {
        String message = String.format(
                format,
                args );
        _statusMessage.setText(
                message );
    }

    private SourceLocator findLocation( TransformerException e )
    {
        SourceLocator result = e.getLocator();

        if ( result != null )
            return result;

        Throwable cause = e.getCause();
        if ( cause == null )
            return result;

        if ( cause instanceof TransformerException )
            return findLocation( (TransformerException)cause );
        else if ( cause instanceof SAXParseException )
            return findLocation( (SAXParseException)cause );

        return findLocation( cause );
    }

    private SourceLocator findLocation( SAXParseException e )
    {
        if ( e.getLineNumber() > 0 )
            return new SourceLocatorImpl( e.getLineNumber() );

        Throwable cause = e.getCause();
        if ( cause == null )
            return null;

        if ( cause instanceof TransformerException )
            return findLocation( (TransformerException)cause );
        else if ( cause instanceof SAXParseException )
            return findLocation( (SAXParseException)cause );

        return findLocation( cause );
    }

    private SourceLocator findLocationImpl( Throwable e )
    {
        if ( e instanceof TransformerException )
            return findLocation( (TransformerException)e );
        else if ( e instanceof SAXParseException )
            return findLocation( (SAXParseException)e );

        Throwable cause = e.getCause();
        if ( cause == null )
            return null;

        return findLocation( cause );
    }

    private SourceLocator findLocation( Throwable e )
    {
        SourceLocator result = findLocationImpl( e );

        if ( result != null )
            return result;

        String msg = e.getMessage();
        if ( StringUtil.isEmpty( msg ) )
            return null;

        final String PFX = "line ";
        // As a last resort, try to parse the line number from
        // the error message.  We accept error messages that start with
        // "line d+: ..." as valid.
        if ( ! msg.startsWith( PFX ) )
            return null;

        // Get the string between the prefix and the first occurrence
        // of ':'.
        msg = msg.substring(
                PFX.length(),
                msg.indexOf( ":" ) );

        return new SourceLocatorImpl( Integer.parseInt( msg ) );
    }

    private static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
