package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@데이터베이스이름_medium?TNS_ADMIN=지갑주소",
                    "아이디", "비밀번호");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return conn;
        }

    }

}
