package com.thumbstage.hydrogen;

import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.utils.PinyinUtils;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testIsValidEmail() {
        String email = "c@163.com";
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(email);
        boolean status =  matcher.find();
        assertTrue(status);
    }

    @Test
    public void testPinyin() {
        String first = PinyinUtils.getFirstSpell("你好");
        assertEquals(first, "mike");
    }
}