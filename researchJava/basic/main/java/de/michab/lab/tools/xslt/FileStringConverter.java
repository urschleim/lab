package de.michab.lab.tools.xslt;

import java.io.File;

import org.smack.util.StringUtil;

import javafx.util.StringConverter;

public class FileStringConverter extends StringConverter<File>
{
    @Override
    public String toString( File object )
    {
        if ( object == null )
            return null;

        return object.getPath();
    }

    @Override
    public File fromString( String string )
    {
        if ( StringUtil.isEmpty( string ) )
            return null;

        return new File( string );
    }
}
