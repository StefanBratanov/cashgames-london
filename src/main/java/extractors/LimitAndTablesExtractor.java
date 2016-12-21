package extractors;

import com.google.inject.Singleton;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Singleton
@Slf4j
public class LimitAndTablesExtractor {

    private static final Pattern PATTERN_1 = Pattern.compile("(?<sb>\\d+)(-|/)(?<bb>\\d+)\\((?<tables>\\d+)\\)");
    private static final Pattern PATTERN_2 = Pattern.compile("(?<tables>\\d+)X(?<sb>\\d+)/(?<bb>\\d+)", Pattern.CASE_INSENSITIVE);


    public List<Pair<String, Integer>> extract(String text) {

        List<Pair<String, Integer>> limitAndTables = new ArrayList<>();

        Optional<Matcher> optionalMatcher = Arrays.asList(PATTERN_1, PATTERN_2)
                .stream()
                .map(pattern -> pattern.matcher(text))
                .filter(Matcher::find)
                .findFirst();

        if (optionalMatcher.isPresent()) {
            Matcher matcher = optionalMatcher.get();
            matcher.reset();
            while (matcher.find()) {
                String limit = matcher.group("sb") + "/" + matcher.group("bb");
                Pair<String, Integer> pair = new Pair<>(limit, Integer.valueOf(matcher.group("tables")));
                limitAndTables.add(pair);
            }
        } else {
            log.warn(format("The text [%s] does not match any of the expected patterns", text));
        }

        return limitAndTables;
    }
}
