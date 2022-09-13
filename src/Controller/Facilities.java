package Controller;

import java.sql.SQLException;

import static Controller.connection.rs;

public class Facilities { // 부대 시설 출력 클래스
    public static void Facility() throws SQLException {
        rs = connection.stmt.executeQuery(" SELECT * FROM FACILITY ");
        // 부대시설 관련 정보를 담은 테이블 FACILITY를 조회해 결과값을 rs에 담는다

        System.out.println("-------------------------------------------------------");
        System.out.println("부대시설 번호 \t 장소이름 \t 시작시간 \t 마감시간 \t 층수 \t 전화번호");

        while (rs.next()) { // 결과 값을 각 속성인덱스 위치에 맞제 출력 시킨다
            System.out.println(rs.getString(1) + "\t " + rs.getString(2)
                    + "\t\t " + rs.getString(3) + " \t\t " + rs.getString(4)
                    + " \t\t" + rs.getString(5) + "\t\t" + rs.getString(6));
        }

        System.out.println("-------------------------------------------------------");
    }
}
