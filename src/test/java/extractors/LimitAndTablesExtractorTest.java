package extractors;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LimitAndTablesExtractorTest {

    private LimitAndTablesExtractor underTest;

    @Before
    public void init() {
        underTest = new LimitAndTablesExtractor();
    }

    @Test
    public void testsExtractingUsingPattern1() {
        String text = "1-1(3)1-2(4)";
        List<Pair<String, Integer>> actual = underTest.extract(text);

        Pair<String, Integer> first = actual.get(0);
        Pair<String, Integer> second = actual.get(1);
        assertThat(first.getKey(), is("1/1"));
        assertThat(first.getValue(), is(3));
        assertThat(second.getKey(), is("1/2"));
        assertThat(second.getValue(), is(4));
    }

    @Test
    public void testsExtractingUsingPattern2() {
        String text = "1x1/2";
        List<Pair<String, Integer>> actual = underTest.extract(text);

        Pair<String, Integer> first = actual.get(0);

        assertThat(first.getKey(), is("1/2"));
        assertThat(first.getValue(), is(1));

    }

    @Test
    public void testsExtractingUsingPattern3() {
        String text = "4,1/2";
        List<Pair<String, Integer>> actual = underTest.extract(text);

        Pair<String, Integer> first = actual.get(0);

        assertThat(first.getKey(), is("1/2"));
        assertThat(first.getValue(), is(4));
    }

    @Test
    public void testNotMatchingTextReturnsEmptyList() {
        String text = "crap";

        assertThat(underTest.extract(text).size(), is(0));
    }
}