package extractors;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
public class PokerGameDetailsExtractor {

    private final LimitAndTablesExtractor limitAndTablesExtractor;

    private static final List<String> DS_NLH_GAMES = Arrays.asList("NLHDS", "NLHDEEPSTACK", "NLHDEEP", "DSNLH", "DEEPSTACKNLH", "DEEPNLH");

    private static final Pattern PATTERN_1 = Pattern.compile("(?<game>NLH|PLO)(?<info>(\\d+([-/])\\d+\\(\\d+\\))+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_2 = Pattern.compile("(?<!(,|[^\\d](NLH|PLO)))(?<info>\\d+x\\d+/\\d+)(?<game>ROE|PLO|" + Joiner.on("|").join(DS_NLH_GAMES) + "|NLH)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_3 = Pattern.compile("(?<game>NLH|PLO)(?<info>(\\d+x\\d+/\\d+(,)?)+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_4 = Pattern.compile("(?<info>(\\d+x\\d+/\\d+(,)?)+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_5 = Pattern.compile("(?<game>NLH|PLO)(?<info>(\\d+/\\d+x\\d+(,)?)+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_6 = Pattern.compile("(?<!NLH|PLO|\\))(?<info>(\\d+([-/])\\d+\\(\\d+\\))+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_7 = Pattern.compile("(?<info>(\\d+,\\d+/\\d+))(.*[game|games|running].*)", Pattern.CASE_INSENSITIVE);

    public List<PokerGameDetail> extract(PokerVenue pokerVenue, String statusText, LocalDateTime updatedAt, String twitterUrl) {

        String lineSep = StringEscapeUtils.escapeJava(System.lineSeparator());

        //very dodgy replacing
        String strippedStatusText = statusText
                .replaceAll("\\d{1,2}:\\d{2}", "")
                .replaceAll("(?i)(?<=\\n)\\d+\\s*(?![xX])[a-z]", "")
                .replaceAll("([a-z]|\\n)(?<![xX])\\s*\\d+\\n", "")
                .replaceAll("4/5/6", "")
                .replaceAll("(((?<=\\d)(" + lineSep + "|\\s)+(?=\\d+\\s*([xX])))|((?<=\\d)" +
                        "\\s+(?=(£?\\d+/£?\\d+))))", ",")
                .replaceAll("(?<=(\\d\\n)|(\\d\\r\\n))\\d+.+(\\w+)", "")
                .replaceAll("[^!a-zA-Z0-9()\\-/,]", "")
                //plo specific replace
                .replaceAll("((?i)mixed|(?i)mix)", "")
                .replaceAll("(?i)omaha", "PLO")
                //remove commas between games
                .replaceAll(",(?=\\d+X)", "");

        List<PokerGameDetail> details = new ArrayList<>();

        Optional<Matcher> optionalMatcher = Stream.of(PATTERN_1, PATTERN_2,
                PATTERN_3, PATTERN_4, PATTERN_5, PATTERN_6, PATTERN_7)
                .filter(pattern -> pattern.asPredicate().test(strippedStatusText))
                .map(pattern -> pattern.matcher(strippedStatusText))
                .findFirst();

        if (optionalMatcher.isPresent()) {
            Matcher matcher = optionalMatcher.get();
            matcher.reset();
            while (matcher.find()) {
                String game;
                try {
                    game = matcher.group("game");
                } catch (IllegalArgumentException e) {
                    //default to NLH
                    game = "NLH";
                }
                String info = matcher.group("info");
                List<Pair<String, Integer>> limitAndTablesList = limitAndTablesExtractor.extract(info);
                final String finalGame;
                if (containsIgnoreCase(DS_NLH_GAMES, game)) {
                    finalGame = "NLH Deepstack";
                } else {
                    finalGame = game.toUpperCase();
                }
                limitAndTablesList
                        .forEach(limitAndTables -> {
                            PokerGameDetail detail =
                                    new PokerGameDetail(
                                            new PokerGame(pokerVenue, finalGame, limitAndTables.getKey()),
                                            limitAndTables.getValue(),
                                            updatedAt,
                                            twitterUrl);

                            details.add(detail);
                        });
            }
        } else {
            log.warn("The statusText does not match any of the expected patterns");
        }
        return details;

    }

    private boolean containsIgnoreCase(List<String> list, String game) {
        return list.stream()
                .anyMatch(elem -> elem.equalsIgnoreCase(game));
    }
}
