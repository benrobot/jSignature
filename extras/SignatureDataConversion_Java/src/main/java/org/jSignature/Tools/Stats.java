//
// Translated by CS2J (http://www.cs2j.com): 11/30/2015 10:55:10 PM
//

package org.jSignature.Tools;


public class Stats   
{
    private int[][][] data = new int[][][]();
    private int[] _content_dimensions = new int[]();
    private void _calc_content_dimensions() throws Exception {
        int x = new int();
        int y = new int();
        int minx = System.Int32.MaxValue;
        int miny = System.Int32.MaxValue;
        int maxx = System.Int32.MinValue;
        int maxy = System.Int32.MinValue;
        for (Object __dummyForeachVar1 : this.data)
        {
            int[][] stroke = (int[][])__dummyForeachVar1;
            int lastx = 0;
            int lasty = 0;
            for (Object __dummyForeachVar0 : stroke)
            {
                int[] coordinate = (int[])__dummyForeachVar0;
                x = lastx + coordinate[0];
                y = lasty + coordinate[1];
                if (x < minx)
                    minx = x;
                 
                if (x > maxx)
                    maxx = x;
                 
                if (y < miny)
                    miny = y;
                 
                if (y > maxy)
                    maxy = y;
                 
                lastx = x;
                lasty = y;
            }
        }
        this._content_dimensions = new int[]{ minx, miny, maxx, maxy };
    }

    public Stats(int[][][] data) throws Exception {
        this.data = data;
        _calc_content_dimensions();
    }

    /**
    * Returns total image's size, including whitespace around content
    * 
    *  @return
    */
    public int[] getSize() throws Exception {
        return new int[]{ this._content_dimensions[2], this._content_dimensions[3] };
    }

    /**
    * Returns the size of the content only, excluding the whitespace around content
    * This is useful for cropping.
    * 
    *  @return
    */
    public int[] getContentSize() throws Exception {
        return new int[]{ this._content_dimensions[2] - this._content_dimensions[0] + 1, this._content_dimensions[3] - this._content_dimensions[1] + 1 };
    }

    /**
    * Returns min upper left coordinate and max lower right coordinate of the content
    * 
    *  @return int[] of form [minx, miny, maxx, maxy], where "min" is upper left point and "max" is lower right point.
    */
    public int[] getContentLimits() throws Exception {
        return this._content_dimensions;
    }

}


