package com.thumbstage.hydrogen;

import com.avos.avoscloud.AVUtils;
import com.thumbstage.hydrogen.model.LineType;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testParseDate() {
        String dislogue = LineType.LT_DIALOGUE.name();
        System.out.println(dislogue);
        System.out.println(LineType.LT_DIALOGUE.name());
    }
}