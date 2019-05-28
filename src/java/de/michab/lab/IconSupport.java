/* $Id: IconSupport.java 781 2015-01-05 17:45:38Z Michael $
 *
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2014 Michael G. Binz
 */
package de.michab.lab;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;




/**
 * Icon support class.
 *
 * @author vogelh
 * @version $Revision: 781 $
 *
 */
public class IconSupport {
    public static enum HorizontalAlign { LEFT, CENTER, RIGHT }
    public static enum VerticalAlign { TOP, CENTER, BOTTOM }

    private IconSupport() {
        throw new AssertionError();
    }

    private static final Logger LOG = Logger.getLogger(IconSupport.class.getName());

    public static final ImageIcon SAVE_AS = loadIcon("save_as.png");

    public static final ImageIcon EMPTY_ICON_16 = createEmptyIcon(16, 16);

    public static final ImageIcon START_16 = loadIcon( "start_16.png" );
    public static final ImageIcon START_24 = loadIcon( "start_24.png" );

    public static final ImageIcon END_16 = loadIcon( "end_16.png" );
    public static final ImageIcon END_24 = loadIcon( "end_24.png" );

    public static final ImageIcon JOIN_16 = loadIcon( "join_16.png" );
    public static final ImageIcon JOIN_24 = loadIcon( "join_24.png" );

    public static final ImageIcon FORK_16 = loadIcon( "fork_16.png" );
    public static final ImageIcon FORK_24 = loadIcon( "fork_24.png" );


    public static final ImageIcon IMAGE_ERROR = loadIcon( "error.png" );
    public static final ImageIcon IMAGE_WARNING = loadIcon( "warning.png" );
    public static final ImageIcon IMAGE_WARNING_24 = loadIcon( "warning_24.png" );

    public static final ImageIcon IMAGE_INFO = loadIcon( "information2.png" );
    public static final ImageIcon MESSAGE = loadIcon( "message.png" );

    public static final ImageIcon ICON_SEARCH = loadIcon("search.png");
    public static final ImageIcon ICON_EXPAND = loadIcon("expand.png");
    public static final ImageIcon ICON_COLLAPSE = loadIcon("collapse.png");
    public static final ImageIcon ICON_REFRESH = loadIcon("refresh.png");

    public static final ImageIcon ICON_BLANK =  loadIcon("blank.gif");

    public static final ImageIcon ICON_ADD_NEW = loadIcon("add2.png");
    public static final ImageIcon ICON_DELETE = loadIcon("delete2.png");

    public static final ImageIcon ICON_SERVICE_SMALL = loadIcon( "gear16.png" );
    public static final ImageIcon ICON_SERVICE_BIG = loadIcon( "gear24.png" );

    public static final ImageIcon ARROW_BLACK = loadIcon("arrow_black.png");

    public static final ImageIcon ICON_VALIDATION = loadIcon( "check2.png" );

    public static final ImageIcon ICON_SYSTEM_MESSAGE = loadIcon("gear_error.png");
    public static final ImageIcon ICON_SYSTEM_MESSAGE_PARAMS =
            loadIcon("Array.png");
    public static final ImageIcon ICON_SYSTEM_MESSAGE_CODE =
        loadIcon("key1.png");
    public static final ImageIcon ICON_SYSTEM_MESSAGE_TEXT =
        loadIcon("information2.png");
    public static final ImageIcon ICON_SYSTEM_MESSAGE_PARAM =
            loadIcon("graph_node.png");
    public static final ImageIcon ICON_OPERATION = ICON_SYSTEM_MESSAGE_PARAM;

    /**
     * Loads the icon with the given name.
     *
     * @param pImageName icon name relative to this class.
     * @return
     */
    private static ImageIcon loadIcon(String pImageName)
    {
        return loadIcon(IconSupport.class, pImageName);
    }

    /**
     * Loads the icon with the given name that is relative to the given class.
     *
     * @param pClass
     *            class to be used as base for the icon name.
     * @param pImageName
     *            the name of the icon to load.
     * @return
     */
    public static ImageIcon loadIcon(Class<?> pClass, String pImageName)
    {
        URL url = pClass.getResource(pImageName);
        if (url == null) {
            LOG.warning("Icon '" + pImageName + "' for class '" + pClass.getName()
                    + "' not found.");
            // Use empty icon in order to avoid NPE.
            return EMPTY_ICON_16;
        }
        return loadIcon(url);
    }

    /**
     * Loads the icon with the given URL.
     *
     * @param pIconUrl
     * @return
     */
    public static ImageIcon loadIcon(URL pIconUrl)
    {
        return new ImageIcon(pIconUrl);
    }

    /**
     * Creates new empty transparent icon with the given dimensions.
     *
     * @param pWidth
     * @param pHeight
     * @return
     */
    public static ImageIcon createEmptyIcon(int pWidth, int pHeight)
    {
        BufferedImage newImage = new BufferedImage(
                pWidth,
                pHeight,
                BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(newImage);
    }

    /**
     * Creates new icon with the given dimensions. The new icon contains the
     * given icon (unscaled, but possibly cropped). The given icon is alligned
     * according to the pHorizontalAlign and pVerticalAlign.
     *
     * @param pIcon
     * @param pNewWidth
     * @param pNewHeight
     * @param pHorizontalAlign
     * @param pVerticalAlign
     * @return
     */
    public static ImageIcon createResizedIcon(
            ImageIcon pIcon,
            int pNewWidth,
            int pNewHeight,
            HorizontalAlign pHorizontalAlign,
            VerticalAlign pVerticalAlign)
    {
        ImageIcon resizedEmptyIcon = createEmptyIcon(pNewWidth, pNewHeight);
        return createOverlayedIcon(
                resizedEmptyIcon,
                pIcon,
                pHorizontalAlign,
                pVerticalAlign);
    }

    /**
     * Creates new icon that is combination of the two given icons with
     * overlaying the pOverlayIcon over the pBaseIcon. The pOverlayIcon is
     * positioned according to the pHorizontalAlign and pVerticalAlign.
     *
     * @param pBaseIcon
     * @param pOverlayIcon
     * @param pHorizontalAlign
     * @param pVerticalAlign
     * @return
     */
    public static ImageIcon createOverlayedIcon(
            ImageIcon pBaseIcon,
            ImageIcon pOverlayIcon,
            HorizontalAlign pHorizontalAlign,
            VerticalAlign pVerticalAlign)
    {
        BufferedImage newImage = new BufferedImage(
                pBaseIcon.getIconWidth(),
                pBaseIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(pBaseIcon.getImage(), 0, 0, null);

        int x = getOverlayOffsetX(
                pBaseIcon.getIconWidth(),
                pOverlayIcon.getIconWidth(),
                pHorizontalAlign);
        int y = getOverlayOffsetY(
                pBaseIcon.getIconHeight(),
                pOverlayIcon.getIconHeight(),
                pVerticalAlign);
        g.drawImage(pOverlayIcon.getImage(), x, y, null);

        return new ImageIcon(newImage);
    }

    /**
     * Creates new icon that is produced with scaling the given icon with the
     * given factor.
     *
     * @param pIcon
     * @param pScaleFactor
     * @return
     */
    public static ImageIcon createScaledIcon(ImageIcon pIcon, double pScaleFactor)
    {
        int newWidth = (int) (pIcon.getIconWidth() * pScaleFactor);
        int newHeight = (int) (pIcon.getIconHeight() * pScaleFactor);

        BufferedImage newImage = new BufferedImage(
                newWidth,
                newHeight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        AffineTransform transform =
                AffineTransform.getScaleInstance(pScaleFactor, pScaleFactor);
        g.drawImage(pIcon.getImage(), transform, null);
        return new ImageIcon(newImage);
    }

    private static int getOverlayOffsetX(
            int pBaseWidth,
            int pOverlayWidth,
            HorizontalAlign pHorizontalAlign)
    {
        switch (pHorizontalAlign) {
        case LEFT:
            return 0;
        case CENTER:
            return (pBaseWidth - pOverlayWidth) / 2;
        case RIGHT:
            return pBaseWidth - pOverlayWidth;
        default:
            throw new IllegalArgumentException("pHorizontalAlign: "
                    + pHorizontalAlign);
        }
    }

    private static int getOverlayOffsetY(
            int pBaseHeight,
            int pOverlayHeight,
            VerticalAlign pVerticalAlign)
    {
        switch (pVerticalAlign) {
        case BOTTOM:
            return pBaseHeight - pOverlayHeight;
        case CENTER:
            return (pBaseHeight - pOverlayHeight) / 2;
        case TOP:
            return 0;
        default:
            throw new IllegalArgumentException("pVerticalAlign: "
                    + pVerticalAlign);
        }
    }
}
