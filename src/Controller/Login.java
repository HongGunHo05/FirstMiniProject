package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import util.ConnectionHelper;
import util.closeHelper;

import static Controller.connection.br;

class member {
    public static boolean CheckLogin = false; //로그인 여부
}

class connection { // 연결을 위한 클래스
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    // 입력 받기 위해 선언한 객체
    public static Statement stmt = null;
    // 쿼리 작업을 실행 하기 위해 선언
    public static ResultSet rs = null;
    // 쿼리문 작업을 한 후 결과를 받아오기 위해 선언
    public static Connection conn = null;
    // 연결을 위해 선언
    public static PreparedStatement pstmt = null;
    // 쿼리 작업을 실행 하기 위해 선언

    public static void connect() { // 연결을 위한 메소드
        try {
            conn = ConnectionHelper.getConnection();
            // util 패키지의 ConnectionHelper 에서 getConnection 메소드를 실행하여 데이터베이스 연결
            stmt = conn.createStatement();
            // 연결 후 Statement 객체 생성
        } catch (Exception e) { // 오류 발생시 처리
            e.printStackTrace();
        }
    }

    public static void close() {  // 열어 두었던 것들을 닫는다
        try {
            closeHelper.close(rs);
            closeHelper.close(stmt);
            closeHelper.close(conn);
            closeHelper.close(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} // 연결을 위한 클래스

public class Login { // 로그인 되어 있는 화면, 로그 아웃 되어 있는 화면 보여주기 위한 클래스
    public static void login() throws IOException, SQLException {
        while (true) { // 프로그램 종료시 끝
            if (member.CheckLogin) //로그인이 되어 있으면
                loginon();
            else loginout(); // 로그인 안 되어 있으면
        }
    }

    public static void loginon() throws IOException, SQLException {
        connection.connect(); // db 연결

        while (member.CheckLogin == true) { // 로그인 되어 있는지 확인 변수가 true일 경우 반복
            System.out.println("0. 로그아웃"); // 로그인 되어 있을 때 보여지는 메뉴
            System.out.println("1. 마이페이지");
            System.out.println("2. 객실 정보");
            System.out.println("3. 예약하기");
            System.out.println("4. 예약확인");
            System.out.println("5. 부대시설");
            System.out.println("6. 카드할인 정보");
            System.out.println("7. 프로그램 종료");

            System.out.print("번호를 골라주세요 : ");

            int choice = Integer.parseInt(br.readLine());
            // 원하는 번호를 고르기 위한 변수

            switch (choice) {
                case 0: // 로그 아웃
                    member.CheckLogin = false; // 로그 아웃 시 로그인 체크 변수 false로 변환
                    break;
                case 1: // 마이페이지
                    mypage.menu();
                    break;
                case 2: // 객실 정보
                    RoomList.selectAll();
                    break;
                case 3: // 예약 하기
                    Reservation.Reserve();
                    break;
                case 4: // 예약 확인
                    ReservationCheck.ReservationT();
                    break;
                case 5: // 부대 시설
                    Facilities.Facility();
                    break;
                case 6: // 카드 할인 정보
                    CardInfo.ask();
                    break;
                case 7: // 프로그램 종료
                    System.out.println("프로그램 종료");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못 누르셨습니다. 다시 누르세요.");
                    break;
            }

        }
        connection.close();
    } // 로그인 외어 있는 화면

    public static void loginout() throws IOException, SQLException {
        connection.connect(); // db연결

        while (member.CheckLogin == false) { // 로그인 되어 있는지 확인 변수가 false일 경우 반복
            System.out.println("1. 로그인"); // 로그인 되어 있을 때 보여지는 메뉴, 처음 보여지는 화면도 이 화면을 보여준다
            System.out.println("2. 객실 정보");
            System.out.println("3. 부대시설");
            System.out.println("4. 카드할인 정보");
            System.out.println("5. 프로그램 종료");

            System.out.print("번호를 골라주세요 : ");

            int choice = Integer.parseInt(br.readLine());
            // 원하는 메뉴 선택

            switch (choice) {
                case 1: // 로그인
                    loginin.logining();
                    break;
                case 2: // 객실 정보
                    RoomList.selectAll();
                    break;
                case 3: // 부대 시설
                    Facilities.Facility();
                    break;
                case 4: // 카드 할인 정보
                    CardInfo.ask();
                    break;
                case 5: // 프로그램 종료
                    System.out.println("프로그램 종료");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못 누르셨습니다. 다시 누르세요.");
                    break;
            }


        }
        connection.close(); // 열려 있는 것 다 닫는다
    } // 로그 아웃 되어 있는 화면

}