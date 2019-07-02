package wvustat.simulation.relfreq;

class PlotCoord
{

    PlotCoord(float f, float f1, float f2, float f3, int i, int j, int k)
    {
        xstretch = 1.0F;
        ystretch = 1.0F;
        xshift = 0.0F;
        yshift = 0.0F;
        inset = k;
        xstretch = (float)(i - 2 * inset - 30) / (f - f2);
        ystretch = (float)(j - 10 - 2 * inset) / (f1 - f3);
        xshift = -f2;
        yshift = -f3;
        ywholeScreen = f1 - f3;
    }

    int xcoord(float f)
    {
        float f1 = (f + xshift) * xstretch + (float)inset + 24F;
        return Math.round(f1);
    }

    int ycoord(float f)
    {
        float f1 = (ywholeScreen - (f + yshift)) * ystretch + (float)inset;
        return Math.round(f1);
    }

    float xstretch;
    float ystretch;
    float xshift;
    float yshift;
    float ywholeScreen;
    int inset;
}
