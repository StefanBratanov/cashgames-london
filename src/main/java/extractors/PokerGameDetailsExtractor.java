package extractors;

import com.google.inject.Inject;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.PokerGame;
import model.PokerGameDetail;
import model.PokerGame;
import model.PokerVenue;

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

    public List<PokerGameDetail> extract(String username, String statusText, LocalDateTime updatedAt) {

        //very dodgy replacing
        String strippedStatusText = statusText.replaceAll("(?<=\\d)(\\n|\\s)+(?=\\d+x|X)", ",")
                .replaceAll("[^a-zA-Z0-9\\(\\)\\-/,]", "").replaceAll("(?i)omaha", "PLO");

        List<PokerGameDetail> details = new ArrayList<>();

        Optional<Matcher> optionalMatcher = Arrays.asList(PATTERN_1, PATTERN_2, PATTERN_3)
                .stream()
                .filter(pattern -> pattern.asPredicate().test(strippedStatusText))
                .map(pattern -> pattern.matcher(strippedStatusText))
                .findFirst();

        if (optionalMatcher.isPresent()) {
            Matcher matcher = optionalMatcher.get();
            matcher.reset();
            while (matcher.find()) {
                String game = matcher.group("game").toUpperCase();
                PokerVenue pokerVenue = PokerVenue.fromTwitterName(username);
                String info = matcher.group("info");
                List<Pair<String, Integer>> limitAndTablesList = limitAndTablesExtractor.extract(info);
                limitAndTablesList
                        .forEach(limitAndTables -> {
                            PokerGameDetail detail = PokerGameDetail.builder()
                                    .pokerGame(PokerGame.builder()
                                            .venue(pokerVenue).game(game).limit(limitAndTables.getKey())
                                            .build())
                                    .numberOfTables(limitAndTables.getValue())
                                    .updatedAt(updatedAt)
                                    .build();

                            details.add(detail);
                        });
            }
        } else {
            log.warn(format("The statusText does not match any of the expected patterns"));
        }
        return details;

    }
}
