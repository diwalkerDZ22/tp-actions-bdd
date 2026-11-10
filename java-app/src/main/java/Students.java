import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Students {

    public record Student(int id, String name, double grade) {}

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp", "postgres", "postgres");
    }

    public static int addStudent(Connection conn, String name, double grade) throws SQLException {
        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, grade);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public static Optional<Student> getStudent(Connection conn, int id) throws SQLException {
        String sql = "SELECT id, name, grade FROM students WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Student(rs.getInt("id"), rs.getString("name"), rs.getDouble("grade")));
            }
        }
    }

    public static double averageGrade(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT AVG(grade) FROM students");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getDouble(1);
        }
    }
}
