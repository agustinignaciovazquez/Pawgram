package ar.edu.itba.pawgram.persistence.querybuilder;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class PostKeywordQueryBuilder extends EntityKeywordQueryBuilder{
    private final static String FIRST_SEARCH_FIELD = "lower(p.title)";
    private final static String SECOND_SEARCH_FIELD = "lower(p.description)";

    private final String[] fields = { FIRST_SEARCH_FIELD, SECOND_SEARCH_FIELD };

    public String buildQuery(final Set<String> keywords, final Map<String, String> keyWordsRegExp) {
        return buildQuery(keywords, keyWordsRegExp, fields);
    }
}
