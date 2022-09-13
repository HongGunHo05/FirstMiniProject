package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import static Controller.connection.br;
import static Controller.connection.rs;

public class Payment {
    public static int ch = 0; // 결재 하기 위해 카드 번호 저장하기 위한 변수

    public static void Charge() throws SQLException, IOException { // 카드 결제
        rs = connection.stmt.executeQuery(" SELECT * FROM DISCOUNT ");
        // 카드 종류와 할인률이 저장된 테이블 DISCOUNT를 조회해 결과값 rs에 저장

        int[] arr = new int[9]; // 카드 번호들을 저장 하기 위한 배열
        int i = 0;

        System.out.println("카드 종류");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            // 카드 번호, 종류, 할인률을 각 속성인덱스에 맞게 출력 시킨다
            arr[i] = Integer.parseInt(rs.getString(1)); // 배열에 카드 번호 저장
            i++;
        }

        Boolean bl = true; // 반복문 탈출을 위한 변수

        while (bl) {
            System.out.print("이 중 하나를 선택하세요 : ");
            ch = Integer.parseInt(br.readLine()); // 원하는 카드 번호

            for (i = 0; i < arr.length; i++) { // 입력받은 카드 번호가 카드 번호 배열에 받는것이 있는지 체크
                if (ch == arr[i]) { // 있다면
                    bl = false; // 반복문 탈출
                }
            }
            if (bl) { // 없다면 실행
                System.out.println("잘못 된 번호입니다. 다시 누르세요.");
            }
        } // 원하는 카드 번호 선택

        // ch 카드 종류, roomprice 방 가격 , choicenum 방번호, roomtype 방 타입

        rs = connection.stmt.executeQuery(" SELECT * FROM DISCOUNT where DIS_NO = " + ch);
        // 입력 받은 카드 번호를 기준으로 DISCOUNT 테이블을 조회해 결과 값을 rs에 저장

        String Cname = ""; // 카드 이름
        int Crate = 0; // 카드 할인

        while (rs.next()) {
            Cname = rs.getString(2); // 카드 이름을 변수에 저장
            Crate = Integer.parseInt(rs.getString(3).replace("%", ""));
            // 카드 할인률을 변수에 저장
        }

        resMember.totalPrice = (resMember.totalPrice / 100 * (100 - Crate));
        // 총가격을 의미하는 전역변수 totalPrice에 총 가격에서 할인 된 가격을 넣어준다

        System.out.println("고객님이 선택한 카드 종류는 : " + Cname);
        System.out.println("고객님의 결제 금액은 : " + resMember.totalPrice);

        bl = true; // 반복문 탈출을 위한 변수
        while (bl) {
            System.out.println("진행 하시겠습니까? 아니면 홈으로 돌아 갑니다");
            System.out.print("Y / N 으로 입력하세요 : ");

            String yn = br.readLine(), gara = "";

            if (yn.equalsIgnoreCase("N")) { // 홈으로 돌아가는 선택지
                bl = false;
            } else if (yn.equalsIgnoreCase("Y")) { // 결재 진행
                System.out.print("카드 번호를 입력하세요 : ");
                gara = br.readLine();
                System.out.print("년/월을 입력하세요 : ");
                gara = br.readLine();
                System.out.print("cvc를 입력하세요 : ");
                gara = br.readLine();
                System.out.print("비밀번호 앞 두자리를 입력하세요 : ");
                gara = br.readLine();

                System.out.println("결제 되었습니다.");
                tableIn(); // 지금까지 입력한 것을 테이블에 삽입 하기 위한 메소드 호출
                bl = false;
            } else {
                System.out.println("잘못 누르셨습니다. 다시 누르세요.");
            }
        }
    } // 카드 결제

    public static void deposit() throws SQLException, InterruptedException { // 무통장 입금
        System.out.println("고객님의 총 금액은 " + resMember.totalPrice + " 입니다.");
        System.out.println();
        System.out.println("입금 계좌번호 : (국민) 943202-00-000000 \t 김덕배");
        System.out.println("입금 대기 중 입니다.");

        Thread.sleep(5000);
        System.out.println("입금되었습니다.");
        tableIn(); // 지금까지 입력한 것을 테이블에 삽입 하기 위한 메소드 호출

    } // 무통장 입금

    public static void tableIn() throws SQLException {

        // 예약번호 설정
        // 예약번호 = 날짜 + 방번호
        String[] sarr = resMember.idate.split("/");
        // 예약하기 위해 입력 받은 날짜에서 / 를 빼고 각각 배열에 저장
        sarr[0] = sarr[0].substring(2, 4);
        // 2022년도면 22만 저장 하기 위해
        resMember.resNum = sarr[0] + sarr[1] + sarr[2] + Integer.toString(resMember.choicenum);
        // 전역변수 resNum에 ( 년 + 월 + 일 + 입력받아 논 방번호 ) 저장한다


        // 예약 테이블에 저장
        connection.pstmt = connection.conn.prepareStatement(" insert into reservation values (?,?,?,?,?,?) ");
        // 저장 하기 위해 쿼리문 사용

        connection.pstmt.setString(1, resMember.resNum);
        connection.pstmt.setString(2, Integer.toString(resMember.choicenum));
        connection.pstmt.setString(3, resMember.idate);
        connection.pstmt.setString(4, resMember.roomOp);
        connection.pstmt.setInt(5, loginin.c_no);
        connection.pstmt.setInt(6, resMember.totalPrice);
        connection.pstmt.executeUpdate();
        // 각 파라미터인덱스 위치, 타입에 맞게 삽입 하고
        // 업데이트 시켜준다

        rs = connection.stmt.executeQuery(" SELECT PW_NO FROM PAYWITH  WHERE DIS_NO = " + ch);
        // 저장해둔 카드 번호를 기반으로 카드 결제 번호 조회 한 결과를 rs에 저장
        while (rs.next()) {
            resMember.pwno = rs.getString(1); // 결과를 전역변수로 저장
        }

        // 결제 테이블에 저장
        connection.pstmt = connection.conn.prepareStatement(" insert into pay values (?,?,?,?) ");
        // 저장 하기 위해 쿼리문 사용

        connection.pstmt.setString(1, "P" + resMember.resNum);
        connection.pstmt.setString(2, resMember.pwno);

        int lNum = (int) (Math.random() * 8 + 1); // 직원 번호를 랜덤값으로 준다
        connection.pstmt.setString(3, String.valueOf(lNum));
        connection.pstmt.setString(4, resMember.resNum);
        connection.pstmt.executeUpdate();
        // 각 파라미터인덱스 위치, 타입에 맞게 삽입 하고
        // 업데이트 시켜준다

    } // 테이블 삽입
}
