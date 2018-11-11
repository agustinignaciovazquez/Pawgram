package ar.edu.itba.pawgram.persistence.querybuilder;

import ar.edu.itba.pawgram.interfaces.persistence.querybuilder.KeywordQueryBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;
import java.util.Set;

public abstract class EntityKeywordQueryBuilder implements KeywordQueryBuilder {

    public String buildQuery(final Set<String> keywords, final Map<String, String> keyWordsRegExp, String[] fields) {
        final StringBuilder whereQueryBuilder = new StringBuilder();
        boolean queryPutAnd = false;

        for (final String keyword : keywords) {
            if (queryPutAnd)
                whereQueryBuilder.append(" AND ");
            final String lowercaseKeyword = keyword.toLowerCase();
            final String candidateKeyWord = RandomStringUtils.randomAlphanumeric(10);

            for (int i = 0; i < fields.length; i++) {
                final String firstKeyWord = "first" + candidateKeyWord;
                final String otherKeyWord = "other" + candidateKeyWord;
                final String firstKeyWordRegExp = lowercaseKeyword + "%";
                final String otherKeyWordRegExp = "% " + lowercaseKeyword + "%";

                if (i == 0) {
                    whereQueryBuilder.append("(");
                    keyWordsRegExp.put(firstKeyWord, firstKeyWordRegExp);
                    keyWordsRegExp.put(otherKeyWord, otherKeyWordRegExp);
                } else
                    whereQueryBuilder.append(" OR ");

                whereQueryBuilder.append("(");
                whereQueryBuilder.append(fields[i]);
                whereQueryBuilder.append(" LIKE ");
                whereQueryBuilder.append(":").append(firstKeyWord);

                whereQueryBuilder.append(" OR ");

                whereQueryBuilder.append(fields[i]);
                whereQueryBuilder.append(" LIKE ");
                whereQueryBuilder.append(":").append(otherKeyWord);
                whereQueryBuilder.append(")");

                if (i == fields.length - 1)
                    whereQueryBuilder.append(")");

                queryPutAnd = true;
            }
        }

        return whereQueryBuilder.toString();
    }
}
