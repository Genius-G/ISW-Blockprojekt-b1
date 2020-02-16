package de.moviemanager.data;

import android.widget.Toast;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.util.DateUtils;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovieTest {


    @Test
    @DisplayName("lend Movie - not lent Movie - no Due Date")
    void testLentMovieWithNotLentMovieAndNoDueDate() throws UnsupportedOperationException {
        Movie m1 = createNewMovie(1, "Star Wars");
        m1.setLent(false);

        // precondition
        assertFalse(m1.isOnLoan());
        assertNull(m1.getDueDate());

        // test
        assertThrows(UnsupportedOperationException.class, () ->
                m1.lendMovie());

        // postcondition
        assertFalse(m1.isOnLoan());
    }

    @Test
    @DisplayName("lend Movie - lent Movie - no Due Date")
    void testLentMovieWithLentMovieAndNoDueDate() throws UnsupportedOperationException{
        Movie m1 = createNewMovie(1, "Star Wars");
        m1.setLent(true);

        // precondition
        assertTrue(m1.isOnLoan());
        assertNull(m1.getDueDate());

        // test
        assertThrows(UnsupportedOperationException.class, () ->
               m1.lendMovie());

        // postcondition
        assertTrue(m1.isOnLoan());
    }

    @Test
    @DisplayName("lend Movie - lent Movie - with Due Date")
    void testLentMovieWithLentMovieAndDueDate() throws IllegalStateException {
        Movie m1 = createNewMovie(1, "Star Wars","15.06.2025");
        m1.setLent(true);

        // precondition
        assertTrue(m1.isOnLoan());
        assertNotNull(m1.getDueDate());

        // test
        assertThrows(IllegalStateException.class, () ->
                m1.lendMovie());

        // postcondition
        assertTrue(m1.isOnLoan());
    }

    @Test
    @DisplayName("lend Movie - not lent Movie - with Due Date")
    void testLentMovieWithNotLentMovieAndDueDate() {
        Movie m1 = createNewMovie(1, "Star Wars","15.06.2025");
        m1.setLent(false);

        // precondition
        assertFalse(m1.isOnLoan());
        assertNotNull(m1.getDueDate());

        // test
        m1.lendMovie();

        // postcondition
        assertTrue(m1.isOnLoan());
    }

    @Test
    @DisplayName("change Due Date - not lent Movie - null Due Date")
    void testChangeDueDateWithNotLentMovieAndNullDueDate() {
        Movie m1 = createNewMovie(1, "Star Wars","15.06.2025");
        m1.setLent(true);

        // precondition
        assertTrue(m1.isOnLoan());
        assertNotNull(m1.getDueDate());

        // test
        m1.changeDueDate(null);

        // postcondition
        assertFalse(m1.isOnLoan());
        assertNull(m1.getDueDate());
    }

    @Test
    @DisplayName("change Due Date - not lent Movie - no Due Date")
    void testChangeDueDateWithNotLentMovieAndNoDueDate() {
        Movie m1 = createNewMovie(1, "Star Wars");
        m1.setLent(false);

        // precondition
        assertFalse(m1.isOnLoan());
        assertNull(m1.getDueDate());

        // test
        m1.changeDueDate(createDateFromText("15.06.2026"));

        // postcondition
        assertTrue(m1.isOnLoan());
        assertEquals(m1.getDueDate(), createDateFromText("15.06.2026"));
    }

    @Test
    @DisplayName("change Due Date - not lent Movie - later Due Date")
    void testChangeDueDateWithNotLentMovieAndLaterDueDate() {
        Movie m1 = createNewMovie(1, "Star Wars","15.06.2025");
        m1.setLent(true);

        // precondition
        assertTrue(m1.isOnLoan());
        assertNotNull(m1.getDueDate());

        // test
        m1.changeDueDate(createDateFromText("15.06.2026"));

        // postcondition
        assertTrue(m1.isOnLoan());
        assertEquals(m1.getDueDate(), createDateFromText("15.06.2026"));
    }

    @Test
    @DisplayName("change Due Date - not lent Movie - prior Due Date")
    void testChangeDueDateWithNotLentMovieAndPriorDueDate() throws IllegalArgumentException {
        Movie m1 = createNewMovie(1, "Star Wars","15.06.2025");
        m1.setLent(true);

        // precondition
        assertTrue(m1.isOnLoan());
        assertNotNull(m1.getDueDate());

        // test
        assertThrows(IllegalArgumentException.class, () ->
        m1.changeDueDate(DateUtils.textToDate(new SimpleDateFormat
                ("dd.MM.yyyy"), "15.06.2024")));

        // postcondition
        assertTrue(m1.isOnLoan());
        assertEquals(m1.getDueDate(), createDateFromText("15.06.2025"));
    }

    private Movie createNewMovie(int id, String name) {
        final Movie movie = new Movie(id);
        movie.setName(name);
        return movie;
    }

    private Movie createNewMovie(int id, String name, String dueDate) {
        final Movie movie = new Movie(id);
        movie.setName(name);
        movie.setDueDate(createDateFromText(dueDate));
        return movie;
    }

    private Date createDateFromText(String string) {
        return DateUtils.textToDate(new SimpleDateFormat
                ("dd.MM.yyyy"), string);
    }
}
