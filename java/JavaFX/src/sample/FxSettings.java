package sample;

import java.text.DecimalFormat;

/**
 * @author trond.
 */
public class FxSettings {

    static DecimalFormat floatFormat;

    static double xMax;
    static double xMin;
    static double yMax;
    static double yMin;

    static int SCENE_WIDTH = 1200;
    static int SCENE_HEIGHT = 900;

    static double SCENE_X_MIN = 0;
    static double SCENE_X_MAX = SCENE_WIDTH;
    //Flippet Y-akse, vi regner utifra vanlig koordinatsystem, der y øker oppover
    static double SCENE_Y_MIN = SCENE_HEIGHT;
    static double SCENE_Y_MAX = 0;
}
