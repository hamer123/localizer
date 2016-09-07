package com.pw.localizer.identyfikator;

import com.pw.localizer.identyfikator.exception.OverlayUUIDExpcetion;

/**
 * Created by Patryk on 2016-09-07.
 */
public class OverlayUUIDUtilitis {
    public static boolean matches(String uuid, String uuidSecond){
        String[] partsOne = uuid.split("-");
        String[] partsTwo = uuid.split("-");

        if(!(partsOne.length == 5 && partsTwo.length == 5)) throw new OverlayUUIDExpcetion("Nie poprawny format uuid");

        for(int i = 0; i<5; i++) {
            if (!compareUUIDPart(partsOne[i], partsTwo[i]))
                return false;
        }
        return true;
    }

    private static boolean compareUUIDPart(String part, String partSecond){
        if(part == "any" || partSecond == "any")
            return true;
        return part.equals(partSecond);
    }
}
