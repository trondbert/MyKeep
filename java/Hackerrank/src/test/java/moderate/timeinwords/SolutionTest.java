package moderate.timeinwords;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {

    @Test
    public void testTimeInWords1() { assertEquals("one minute past one", timeInWords("1","01"));
    }

    @Test
    public void testTimeInWords6() { assertEquals("one minute to twelve", timeInWords("11","59"));
    }

    @Test
    public void testTimeInWords2() { assertEquals("two minutes past one", timeInWords("1","02"));
    }

    @Test
    public void testTimeInWords3() { assertEquals("twenty minutes past one", timeInWords("1","20"));
    }

    @Test
    public void testTimeInWords4() { assertEquals("twenty two minutes past one", timeInWords("1","22"));
    }

    @Test
    public void testTimeInWords5() { assertEquals("twenty six minutes to two", timeInWords("1","34"));
    }

    @Test
    public void testTimeInWords7() { assertEquals("twelve o' clock", timeInWords("12","00"));
    }

    @Test
    public void testTimeInWords8() { assertEquals("three o' clock", timeInWords("3","00"));
    }

    @Test
    public void testTimeInWords9() { assertEquals("half past six", timeInWords("6","30"));
    }

    @Test
    public void testTimeInWords10() { assertEquals("quarter to ten", timeInWords("9","45"));
    }

    private String timeInWords(String h, String m) {
        return new Solution().timeInWords(h, m);
    }

}
