import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentsTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        conn = Students.connect();
        try (Statement st = conn.createStatement()) {
            st.execute("TRUNCATE students RESTART IDENTITY");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void ajoutEtLecture() throws SQLException {
        int id = Students.addStudent(conn, "Amel", 15);
        Students.Student s = Students.getStudent(conn, id).orElseThrow();
        assertEquals("Amel", s.name());
        assertEquals(15.0, s.grade(), 0.001);
    }

    @Test
    void etudiantInexistant() throws SQLException {
        assertEquals(Optional.empty(), Students.getStudent(conn, 999));
    }

    @Test
    void moyenne() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);
        assertEquals(12.0, Students.averageGrade(conn), 0.001);
    }
}
