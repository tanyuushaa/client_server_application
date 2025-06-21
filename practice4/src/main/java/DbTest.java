import com.github.tanyuushaa.db.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class DbTest {
    public static void main(String[] args) {
        try(Connection connection = DbUtils.connect()) {
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
