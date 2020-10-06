package de.michab.lab.tools.xslt;

import javax.xml.transform.SourceLocator;

import org.jdesktop.util.StringUtil;

public class SourceLocatorImpl implements SourceLocator
{
    public final String publicId;
    public final String systemId;
    public final int lineNumber;
    public final int columnNumber;

    public SourceLocatorImpl( int lineNumber )
    {
        this( lineNumber, 1, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING );
    }
    public SourceLocatorImpl( int line, int column )
    {
        this( line, column, StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING );
    }
    public SourceLocatorImpl( int line, int column, String pid, String sid )
    {
        publicId = pid;
        systemId = sid;
        lineNumber = line;
        columnNumber = column;
    }

    @Override
    public String getPublicId()
    {
        return publicId;
    }

    @Override
    public String getSystemId()
    {
        return systemId;
    }

    @Override
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public int getColumnNumber()
    {
        return columnNumber;
    }
}
