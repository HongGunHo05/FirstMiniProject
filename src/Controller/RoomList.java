package Controller;

import java.sql.*;

import static Controller.connection.rs;

public class RoomList {
    public static void selectAll() throws SQLException {
        System.out.println("-------------------------------------------------------");
        rs = connection.stmt.executeQuery(" SELECT * FROM ROOMINFORMATION ");
        // 방 정보가 담긴 ROOMINFORMATION을 조회해 결과 값을 rs에 담는다
        System.out.println("방번호 \t\t 방타입 \t\t 방크기");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + "\t | \t" + rs.getString(2) + "\t | \t" + rs.getString(3));
            // 각 속성을 위치와 타입에 맞게 출력
        }
        System.out.println("-------------------------------------------------------");
    }

}
