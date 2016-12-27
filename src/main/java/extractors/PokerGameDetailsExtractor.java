package extractors;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerVenue;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Inject))
public class PokerGameDetailsExtractor {

    private final LimitAndTablesExtractor limitAndTablesExtractor;

    private static final Pattern PATTERN_1 = Pattern.compile("(?<game>NLH|PLO)(?<info>(\\d+(-|/)\\d+\\(\\d+\\))+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_2 = Pattern.compile("(?<!,)(?<info>\\d+x\\d+/\\d+)(?<game>ROE|PLO|NLH)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_3 = Pattern.compile("(?<game>NLH|PLO)(?<info>(\\d+x\\d+/\\d+(,)?)+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_4 = Pattern.compile("(?<info>(\\d+,\\d+/\\d+))(.*[game|games|running].*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_5 = Pattern.compile("(?<!NLH|PLO|\\))(?<info>(\\d+(-|/)\\d+\\(\\d+\\))+)", Pattern.CASE_INSENSITIVE);

    public List<PokerGameDetail> extract(String username, String statusText, LocalDateTime updatedAt, String twitterUrl) {

        //very dodgy replacing
        String strippedStatusText = statusText
                .replaceAll("\\d{1,2}:\\d{2}", "")
                .replaceAll("4/5/6", "")
                .replaceAll("((?<=\\d)(\\n|\\s)+(?=\\d+x|X)|(?<=\\d)\\s+(?=(£?\\d+/£?\\d+)))", ",")
                .replaceAll("[^a-zA-Z0-9\\(\\)\\-/,]", "").replaceAll("(?i)omaha", "PLO");

        List<PokerGameDetail> details = new ArrayList<>();

        Optional<Matcher> optionalMatcher = Arrays.asList(PATTERN_1, PATTERN_2,
                PATTERN_3, PATTERN_4, PATTERN_5)
                .stream()
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
                PokerVenue pokerVenue = PokerVenue.fromTwitterName(username);
                String info = matcher.group("info");
                List<Pair<String, Integer>> limitAndTablesList = limitAndTablesExtractor.extract(info);
                final String finalGame = game;
                limitAndTablesList
                        .forEach(limitAndTables -> {
                            PokerGameDetail detail =
                                    new PokerGameDetail(
                                            new PokerGame(pokerVenue, finalGame.toUpperCase(), limitAndTables.getKey()),
                                            limitAndTables.getValue(),
                                            updatedAt,
                                            twitterUrl);

                            details.add(detail);
                        });
            }
        } else {
            log.warn(format("The statusText does not match any of the expected patterns"));
        }
        return details;

    }
}
