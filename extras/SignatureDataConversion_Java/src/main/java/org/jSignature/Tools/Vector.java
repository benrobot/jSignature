//
// Translated by CS2J (http://www.cs2j.com): 11/30/2015 10:55:10 PM
//

package org.jSignature.Tools;

public class Vector   
{
    public float x;
    public float y;
    public Vector(int x, int y) throws Exception {
        this.x = x;
        this.y = y;
    }

    public Vector(float x, float y) throws Exception {
        this.x = x;
        this.y = y;
    }

    /**
    * Returns NEW Vector object with reversed (multiplied by -1) coords.
    * 
    *  @return
    */
    public Vector getReversed() throws Exception {
        return new Vector(this.x * -1,this.y * -1);
    }

    private float _length;
    /**
    * Applies Pithagoras theorem to find the length of the vector.
    * 
    *  @return
    */
    public float getLength() throws Exception {
        this._length = (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        return this._length;
    }

    /**
    * Returns either +1 or -1 indicating the polarity of the input number
    * 
    *  @param value some number
    *  @return
    */
    private int polarity(float value) throws Exception {
        return Math.round(value / Math.abs(value));
    }

    /**
    * Returns NEW Vector object that has same directionality as this one, but with length of the vector scaled to stated size.
    * 
    *  @param length 
    *  @return
    */
    public Vector getResizedTo(float length) throws Exception {
        // proportionally changes x,y such that the hypotenuse (vector length) is = new length
        if (this.x == 0 && this.y == 0)
        {
            return new Vector(0,0);
        }
        else if (this.x == 0)
        {
            return new Vector(0, length * polarity(this.y));
        }
        else if (this.y == 0)
        {
            return new Vector(length * polarity(this.x), 0);
        }
        else
        {
            float proportion = Math.abs(this.y / this.x);
            double _x = Math.sqrt(Math.pow(length, 2) / (1 + Math.pow(proportion, 2)));
            double _y = proportion * _x;
            return new Vector((float)(_x * polarity(this.x)),(float)(_y * polarity(this.y)));
        }   
    }

    /**
    		 * Calculates the angle between 'this' vector and another.
    		 * @public
    		 * @function
    		 * @returns {Number} The angle between the two vectors as measured in PI. 
    		 */
    public float angleTo(Vector vectorB) throws Exception {
        float divisor = this.getLength() * vectorB.getLength();
        if (divisor == 0)
        {
            return 0;
        }
        else
        {
            return (float)(Math.acos(Math.min(Math.max((this.x * vectorB.x + this.y * vectorB.y) / divisor, -1.0), 1.0)) / Math.PI);
        } 
    }

}


// JavaScript floating point math is screwed up.
// because of it, the core of the formula can, on occasion, have values
// over 1.0 and below -1.0.