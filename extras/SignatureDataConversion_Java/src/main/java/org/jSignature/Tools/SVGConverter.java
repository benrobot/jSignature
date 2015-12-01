//
// Translated by CS2J (http://www.cs2j.com): 11/30/2015 10:55:10 PM
//

package org.jSignature.Tools;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.jSignature.Tools.Vector;

public class SVGConverter   
{
    protected static String segmentToCurve(int[][] stroke, int positionInStroke, float lineCurveThreshold) throws Exception {
        // long lines (ones with many pixels between them) do not look good when they are part of a large curvy stroke.
        // You know, the jaggedy crocodile spine instead of a pretty, smooth curve. Yuck!
        // We want to approximate pretty curves in-place of those ugly lines.
        // To approximate a very nice curve we need to know the direction of line before and after.
        // Hence, on long lines we actually wait for another point beyond it to come back from
        // mousemoved before we draw this curve.
        // So for "prior curve" to be calc'ed we need 4 points
        // 	A, B, C, D (we are on D now, A is 3 points in the past.)
        // and 3 lines:
        //  pre-line (from points A to B),
        //  this line (from points B to C), (we call it "this" because if it was not yet, it's the only one we can draw for sure.)
        //  post-line (from points C to D) (even through D point is 'current' we don't know how we can draw it yet)
        //
        // Well, actually, we don't need to *know* the point A, just the vector A->B
        // Again, we can only derive curve between points positionInStroke-1 and positionInStroke
        // Thus, since we can only draw a line if we know one point ahead of it, we need to shift our focus one point ahead.
        positionInStroke += 1;
        // Let's hope the code that calls us knows we do that and does not call us with positionInStroke = index of last point.
        Vector CDvector = new Vector(stroke[positionInStroke][0], stroke[positionInStroke][1]);
        // Again, we have a chance here to draw only PREVIOUS line segment - BC
        // So, let's start with BC curve.
        // if there is only 2 points in stroke array (C, D), we don't have "history" long enough to have point B, let alone point A.
        // so positionInStroke should start with 2, ie
        // we are here when there are at least 3 points in stroke array.
        Vector BCvector = new Vector(stroke[positionInStroke - 1][0], stroke[positionInStroke - 1][1]);
        Vector ABvector;
        int rounding = 2;
        String curvetemplate = "c {0} {1} {2} {3} {4} {5}";
        String linetemplate = "l {0} {1}";
        if (BCvector.getLength() > lineCurveThreshold)
        {
            // Yey! Pretty curves, here we come!
            if (positionInStroke > 2)
            {
                ABvector = new Vector(stroke[positionInStroke - 2][0], stroke[positionInStroke - 2][1]);
            }
            else
            {
                ABvector = new Vector(0,0);
            } 
            float minlenfraction = 0.05f;
            float maxlen = (float)(BCvector.getLength() * 0.35);
            float ABCangle = BCvector.angleTo(ABvector.getReversed());
            float BCDangle = CDvector.angleTo(BCvector.getReversed());
            Vector BtoCP1vector = new Vector(ABvector.x + BCvector.x,ABvector.y + BCvector.y).getResizedTo((float)(Math.max(minlenfraction, ABCangle) * maxlen));
            Vector CtoCP2vector = new Vector(BCvector.x + CDvector.x,BCvector.y + CDvector.y).getReversed().getResizedTo((float)(Math.max(minlenfraction, BCDangle) * maxlen));
            Vector BtoCP2vector = new Vector(BCvector.x + CtoCP2vector.x,BCvector.y + CtoCP2vector.y);
            BigDecimal BtoCP1vector_x_BC = new BigDecimal(BtoCP1vector.x);
            BigDecimal BtoCP1vector_y_BC = new BigDecimal(BtoCP1vector.y);
            BigDecimal BtoCP2vector_x_BC = new BigDecimal(BtoCP2vector.x);
            BigDecimal BtoCP2vector_y_BC = new BigDecimal(BtoCP2vector.y);
            BigDecimal BCvector_x_BC = new BigDecimal(BCvector.x);
            BigDecimal BCvector_y_BC = new BigDecimal(BCvector.y);
            return MessageFormat.format(curvetemplate, BtoCP1vector_x_BC.setScale(rounding), BtoCP1vector_y_BC.setScale(rounding), BtoCP2vector_x_BC.setScale(rounding), BtoCP2vector_y_BC.setScale(rounding), BCvector_x_BC.setScale(rounding), BCvector_y_BC.setScale(rounding));
        }
        else
        {
            BigDecimal BCvector_x_BC = new BigDecimal(BCvector.x);
            BigDecimal BCvector_y_BC = new BigDecimal(BCvector.y);
            return MessageFormat.format(linetemplate, BCvector_x_BC.setScale(rounding), BCvector_y_BC.setScale(rounding));
        } 
    }

    // returing curve for BC segment
    // all coords are vectors against Bpoint
    protected static String lastSegmentToCurve(int[][] stroke, float lineCurveThreshold) throws Exception {
        // Here we tidy up things left unfinished
        // What's left unfinished there is the curve between the last points
        // in the stroke
        // We can also be called when there is only one point in the stroke (meaning, the
        // stroke was just a dot), in which case there is nothing for us to do.
        // So for "this curve" to be calc'ed we need 3 points
        // 	A, B, C
        // and 2 lines:
        //  pre-line (from points A to B),
        //  this line (from points B to C)
        // Well, actually, we don't need to *know* the point A, just the vector A->B
        // so, we really need points B, C and AB vector.
        int positionInStroke = stroke.length - 1;
        // there must be at least 2 points in the stroke.for us to work. Hope calling code checks for that.
        Vector BCvector = new Vector(stroke[positionInStroke][0], stroke[positionInStroke][1]);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ rounding = 2;
        String curvetemplate = "c {0} {1} {2} {3} {4} {5}";
        String linetemplate = "l {0} {1}";
        if (positionInStroke > 1 && BCvector.getLength() > lineCurveThreshold)
        {
            // we have at least 3 elems in stroke
            Vector ABvector = new Vector(stroke[positionInStroke - 1][0], stroke[positionInStroke - 1][1]);
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ ABCangle = BCvector.angleTo(ABvector.getReversed());
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ minlenfraction = 0.05;
            /* [UNSUPPORTED] 'var' as type is unsupported "var" */ maxlen = BCvector.getLength() * 0.35;
            Vector BtoCP1vector = new Vector(ABvector.x + BCvector.x,ABvector.y + BCvector.y).getResizedTo((float)(Math.Max(minlenfraction, ABCangle) * maxlen));
            return String.Format(curvetemplate, Math.Round(BtoCP1vector.x, rounding), Math.Round(BtoCP1vector.y, rounding), Math.Round(BCvector.x, rounding), Math.Round(BCvector.y, rounding), Math.Round(BCvector.x, rounding), Math.Round(BCvector.y, rounding));
        }
        else
        {
            return String.Format(linetemplate, Math.Round(BCvector.x, rounding), Math.Round(BCvector.y, rounding));
        } 
    }

    // CP2 is same as Cpoint
    // CP2 is same as Cpoint
    // Since there is no AB leg, there is no curve to draw. This is just line
    public static String getPathsSVGFragment(int[][][] data, int shiftx, int shifty) throws Exception {
        // I was contemplating going the <style> tag + class attr way, but GraphicsMagic and .Net SVG renderer do not support that.
        // hence, reiterating the style with every line
        // {0} {1} {2}:
        // 0 = Move X
        // 1 = Move Y
        // 2 = thereafter movement coordinates, join of curve and line fragments.
        String pathtemplate = "\r\n" + 
        "<path style=\'fill:none;stroke:#000000;stroke-width:2;stroke-linecap:round;stroke-linejoin:round\' d=\'M {0} {1} {2}\'/>".Replace('\'', '"');
        float lineCurveThreshold = 0.5f;
        List<String> paths = new List<String>();
        List<String> pathfragments = new List<String>();
        for (Object __dummyForeachVar0 : data)
        {
            int[][] stroke = (int[][])__dummyForeachVar0;
            pathfragments = new List<String>();
            int len = stroke.Length - 1;
            // we are leaving last point for separate processing
            int i = 1;
            for (;i < len;i++)
            {
                pathfragments.Add(segmentToCurve(stroke, i, lineCurveThreshold));
            }
            if (len > 0)
            {
                /* effectively more than 1, since we "-1" above */
                pathfragments.Add(lastSegmentToCurve(stroke, lineCurveThreshold));
            }
             
            // moveto x, starting point
            // moveto y, starting point
            paths.Add(String.Format(pathtemplate, stroke[0][0] + shiftx, stroke[0][1] + shifty, String.Join(" ", pathfragments.ToArray())));
        }
        return String.Join("", paths.ToArray());
    }

    // pathfragments
    /**
    * Produces a string with properly-formatted SVG document, containing all the signature strokes
    * as simple lines.
    * 
    *  @param data 
    *  @return
    */
    public static String toSVG(int[][][] data) throws Exception {
        Stats stats = new jSignature.Tools.Stats(data);
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ contentsize = stats.getContentSize();
        /* [UNSUPPORTED] 'var' as type is unsupported "var" */ limits = stats.getContentLimits();
        String outersvgtemplate = "<?xml version=\'1.0\' encoding=\'UTF-8\' standalone=\'no\'?>\r\n" + 
        "<!DOCTYPE svg PUBLIC \'-//W3C//DTD SVG 1.1//EN\' \'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\'>\r\n" + 
        "<svg xmlns=\'http://www.w3.org/2000/svg\' version=\'1.1\' width=\'{0}\' height=\'{1}\'>{2}\r\n" + 
        "</svg>".Replace('\'', '"');
        return String.Format(outersvgtemplate, contentsize[0], contentsize[1], GetPathsSVGFragment(data, limits[0] * -1 + 1, limits[1] * -1 + 1));
    }

}


// width
// height