package wvustat.simulation.fivestep;

import wvustat.plot.Drawable;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The purpose of this class is to provide pagination to printing documents.
 */
public class PageLayout implements Printable {
    public static class PageElement {
        int x;
        int y;
        int width;
        int height;
    }

    public static class TextContent extends PageElement {
        Font font;

        String[] lineText;


        public TextContent(String[] lineText, Font font) {
            this.lineText = lineText;
            this.font = font;

        }

    }

    public static class ImageContent extends PageElement {
        Drawable drawable;

        public ImageContent(Drawable drawable, int width, int height) {
            this.drawable = drawable;
            this.width = width;
            this.height = height;
        }
    }

    private List elements = new ArrayList();
    private int pageCount = 0;
    private List pageBreaks = new ArrayList();

    public void addLine(String linetext[], Font font) {
        elements.add(new TextContent(linetext, font));

    }

    public void addLine(String lineText, Font font){
        String[] array=new String[]{lineText};

        elements.add(new TextContent(array, font));
    }

    public void addLine(Drawable drawable, int width, int height) {
        elements.add(new ImageContent(drawable, width, height));
    }

    /**
     * Add empty space as a line to separate contents
     *
     * @param height
     */
    public void addLine(int height) {
        PageElement elem = new PageElement();
        elem.height = height;
        elements.add(elem);
    }

    public void prePrint(Graphics g, PageFormat pageFormat) {
        if (pageCount > 0)
            return;
        pageCount = 0;
        int x = (int) pageFormat.getImageableX();
        int y = (int) pageFormat.getImageableY();
        Iterator iterator = elements.iterator();
        while (iterator.hasNext()) {
            PageElement elem = (PageElement) iterator.next();
            elem.x = x;
            elem.y = y;
            if (elem instanceof TextContent) {
                TextContent lc = (TextContent) elem;

                FontMetrics metrics = g.getFontMetrics(lc.font);

                lc.width = (int)pageFormat.getImageableWidth();
                lc.height = metrics.getHeight();
            }
            //y += elem.height;
            if (y+elem.height - pageFormat.getImageableY() >= pageFormat.getImageableHeight()) {
                pageCount++;
                pageBreaks.add(new Integer(elements.indexOf(elem)-1));
                if (!(elem instanceof TextContent) &&
                        !(elem instanceof ImageContent) &&
                                iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }

                y = (int)pageFormat.getImageableY();
                elem.y=y;
            }
            else
                y+=elem.height;

        }

        pageBreaks.add(new Integer(elements.size()-1));
    }

    public int print(Graphics graphics, PageFormat pageFormat, int i) throws PrinterException {
        prePrint(graphics, pageFormat);
        if (i <= pageCount) {
            int start = 0;
            if (i > 0)
                start = ((Integer) pageBreaks.get(i - 1)).intValue() + 1;
            int end = ((Integer) pageBreaks.get(i)).intValue();
            for (int j = start; j <= end; j++) {
                PageElement elem = (PageElement) elements.get(j);
                if (elem instanceof TextContent) {
                    TextContent lc = (TextContent) elem;
                    if (lc.lineText != null) {
                        graphics.setFont(lc.font);
                        FontMetrics metrics = graphics.getFontMetrics(lc.font);
                        int columnWidth = (int) (pageFormat.getImageableWidth() / (lc.lineText.length));
                        int tmpx=lc.x;
                        for(int k=0;k<lc.lineText.length;k++){
                            graphics.drawString(lc.lineText[k], tmpx,  lc.y + metrics.getAscent());
                            tmpx+=columnWidth;
                        }
                    }

                } else if (elem instanceof ImageContent) {
                    ImageContent ic = (ImageContent) elem;
                    graphics.translate(ic.x, ic.y);
                    ic.drawable.draw(graphics, ic.width, ic.height);
                    graphics.translate(-ic.x, -ic.y);
                }
            }
            return PAGE_EXISTS;
        } else
            return NO_SUCH_PAGE;
    }


}
