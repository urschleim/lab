/*
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */

package de.michab.lab;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import org.smack.util.TimeProbe;



/**
 *
 * @author Michael Binz
 */
@SuppressWarnings("serial")
class RoundTitleTester extends JPanel
{
    private final String _title;

    /**
     *
     * @param font
     * @param title
     */
    public RoundTitleTester( Font font, String title )
    {
        _title = title;

        setFont( font );
    }
    /**
     *
     * @param title
     */
    public RoundTitleTester( String title )
    {
        _title = title;
    }


    private double steigung( Point p1, Point p2 )
    {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;

        return dy / dx;
    }

    /**
     *
     * @param gfx
     * @param shape
     * @param angle
     */
    private void paintHelperStuff(
            Graphics2D gfx, Ellipse2D.Float shape, float angle )
    {
        // Draw the ellipse.
        gfx.draw( shape );

        FontMetrics fm = getFontMetrics( getFont() );

        gfx.translate( 0, shape.getHeight() - fm.getHeight() );

        gfx.drawLine( 0, fm.getLeading(), getWidth(), fm.getLeading() );
        gfx.drawLine( 0, fm.getHeight(), getWidth(), fm.getHeight() );
        gfx.drawLine( 0, fm.getAscent(), getWidth(), fm.getAscent() );
        gfx.drawString( "Copyright © 2012 Michael G. Binz", 0, fm.getAscent() );
    }

    /**
     *
     * @param pc
     * @param alpha
     * @param m
     * @param c
     * @return
     */
    private Point2D getRotateCenter( Point pc, double alpha, double m, double c )
    {
        double t = pc.getY() - (m*pc.getX());

        double b = c * Math.cos( alpha );

        double rx = pc.x - b;
        double ry = (m*rx) + t;

        return new Point2D.Double( rx, ry );
    }


    /**
     *
     * @param gfx
     * @param shape
     * @param angle
     */
    private void doTheTrick( Graphics2D gfx, Ellipse2D.Float shape, float angle )
    {
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = getFont();
        FontMetrics fm = getFontMetrics( font );

        int asc = fm.getAscent();

        Ellipse2D.Float ellipse = new Ellipse2D.Float(
                asc,
                asc,
                getWidth() - 2 * asc,
                getHeight() - 2 * asc );

        // The top middle point in our coordinate system.
        Point currentPoint = new Point(
                (int)ellipse.getCenterX(),
                asc );

        EllipticPath ep = new EllipticPath( ellipse );

        GlyphVector gv = getFont().createGlyphVector(gfx.getFontRenderContext(), _title);
        int length = gv.getNumGlyphs();

        for ( int i = 0; i < length; i++ )
        {
            float advance = gv.getGlyphMetrics( i ).getAdvance();

            System.err.println( i + ": " + currentPoint + ", " + advance + "," + gv.getGlyphPosition( i ) );

            Point next = ep.next(
                    currentPoint,
                    advance );

            double steigung = steigung( currentPoint, next );
            double theta = Math.atan( steigung );

            Point2D p = getRotateCenter(
                    currentPoint,
                    theta,
                    steigung,
                    gv.getGlyphPosition( i ).getX() );

            AffineTransform at = AffineTransform.getTranslateInstance(
                    p.getX(), p.getY() );
            at.rotate( theta );

            gfx.fill(
                at.createTransformedShape(
                    gv.getGlyphOutline( i ) ) );

            currentPoint = next;
        }
    }

    /**
     * Switch the coordinate system from the outer world.
     *
     * @param g2
     */
    private void paintImpl( Graphics2D g2 )
    {
        Ellipse2D.Float shape = new Ellipse2D.Float(
                0,
                0,
                getWidth(),
                getHeight() );

        float angle = .0f;

        Graphics2D tmpGfx = (Graphics2D)g2.create();
        try
        {
            tmpGfx.setColor( Color.LIGHT_GRAY );
            paintHelperStuff( tmpGfx, shape, angle );
        }
        finally
        {
            tmpGfx.dispose();
        }

        tmpGfx = (Graphics2D)g2.create();
        try
        {
            doTheTrick( tmpGfx, shape, angle );
        }
        finally
        {
            tmpGfx.dispose();
        }
    }


//    private void markPosition( Graphics2D gfx, int x, int y )
//    {
//        int radius = 5;
//        gfx.drawOval( x-radius, y-radius, 2*radius, 2*radius );
//    }
    /**
     *
     */
    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        Graphics lg = g.create();

        TimeProbe tp = new TimeProbe( "paint" ).start();
        try
        {
            paintImpl( (Graphics2D)lg );
        }
        finally
        {
            tp.stop();
            lg.dispose();

            System.err.println( tp );
        }
    }
}
