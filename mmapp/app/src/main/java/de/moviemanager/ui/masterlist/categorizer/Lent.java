package de.moviemanager.ui.masterlist.categorizer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.moviemanager.data.Movie;
import de.moviemanager.ui.masterlist.elements.ContentElement;
import de.moviemanager.ui.masterlist.elements.DividerElement;
import de.moviemanager.ui.masterlist.elements.HeaderElement;
import de.util.DateUtils;
import de.util.StringUtils;

import static de.util.DateUtils.differenceInDays;
import static de.util.DateUtils.normDateTimeToMidnight;
import static java.lang.Math.abs;

public class Lent extends Categorizer<String, Movie> {
    private static final List<String> CATEGORIES = Arrays.asList(
            "Today",
            "Tomorrow",
            "This Week",
            "This Month",
            "This Year",
            "In A Long Time",
            "Overdue",
            "Not Lent"
    );
    private static final int TODAY_INDEX = 0;
    private static final int TOMORROW_INDEX = 1;
    private static final int THIS_WEEK_INDEX = 2;
    private static final int THIS_MONTH_INDEX = 3;
    private static final int THIS_YEAR_INDEX = 4;
    private static final int IN_A_LONG_TIME_AGO_INDEX = 5;
    private static final int OVERDUE_INDEX = 6;
    private static final int NOT_LENT_INDEX = 7;

    @Override
    protected String getCategoryNameFor(final Movie movie) {
        final Date date = movie.getDueDate();
        int resultIndex;
        if (date == null) {
            resultIndex = NOT_LENT_INDEX;
        } else if (date.before(DateUtils.nowAtMidnight())) {
            resultIndex = OVERDUE_INDEX;
        } else {
            final Date now = DateUtils.nowAtMidnight();
            long diff = abs(differenceInDays(now, normDateTimeToMidnight(date)));
            resultIndex = getCategoryIndex(diff);
        }

        return CATEGORIES.get(resultIndex);
    }

    private static int getCategoryIndex(long diff) {
        int result;
        if (diff == 0) {
            result = TODAY_INDEX;
        } else if (diff == 1) {
            result = TOMORROW_INDEX;
        } else if (diff <= 7) {
            result = THIS_WEEK_INDEX;
        } else if (diff <= 30) {
            result = THIS_MONTH_INDEX;
        } else if (diff <= 365) {
            result = THIS_YEAR_INDEX;
        } else {
            result = IN_A_LONG_TIME_AGO_INDEX;
        }

        return result;
    }

    @Override
    protected HeaderElement<Movie> createHeader(final String category) {
        return new HeaderElement<>(category);
    }

    @Override
    protected ContentElement<Movie> createContent(final Movie movie) {
        final Date dueDate = movie.getDueDate();
        final String subTitle;
        if(dueDate == null) {
            subTitle = "Not lent";
        } else {
            subTitle = DateUtils.dateToText(dueDate);
        }
        return new ContentElement<>(movie.getTitle(), subTitle);
    }

    @Override
    protected DividerElement createDivider() {
        return new DividerElement();
    }

    @Override
    protected int compareCategories(final String cat1, final String cat2) {
        return Integer.compare(CATEGORIES.indexOf(cat1), CATEGORIES.indexOf(cat2));
    }

    @Override
    protected int compareContent(final ContentElement<Movie> content1, final ContentElement<Movie> content2) {
        final Movie movie1 = content1.retrieveContentModel();
        final Movie movie2 = content2.retrieveContentModel();

        final Date dueDate1 = movie1.getDueDate();
        final Date dueDate2 = movie2.getDueDate();

        int result;
        // because two movies can only be compared if they share the same category we only need
        // to check for (null, null) or (not null, not null)
        if(dueDate1 == null && dueDate2 == null) {
            result = compareMoviesAlphabetically(movie1, movie1);
        } else {
            result = -1 * dueDate1.compareTo(dueDate2);
            if(result == 0) {
                result = compareMoviesAlphabetically(movie1, movie2);
            }
        }

        return result;
    }

    private static int compareMoviesAlphabetically(final Movie m1, final Movie m2) {
        return StringUtils.alphabeticalComparison(m1.getTitle(), m2.getTitle());
    }
}

