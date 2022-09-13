package Controller;

import java.sql.SQLException;

import static Controller.connection.rs;

public class CardInfo { // 카드 할인 정보 클래스
    public static void ask() throws SQLException {
        System.out.println("--------------------------------------------");
        rs = connection.stmt.executeQuery(" SELECT * FROM DISCOUNT ");
        // 카드 종류와 할인율이 담긴 테이블 DISCOUNT를 조회하여 결과 값을 rs에 담는다

        System.out.println("카드 번호 \t 카드 종류  \t 할인율");
        while (rs.next()) { // 결과 값을 각 속성인덱스 위치에 맞제 출력 시킨다
            System.out.println(rs.getString(1) + " \t\t " + rs.getString(2) + " \t\t " + rs.getString(3));
        }
        System.out.println("-------------------------------------------------------");
    }
}
