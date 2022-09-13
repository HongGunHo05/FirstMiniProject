package Controller;

import java.io.IOException;
import java.sql.*;

import static Controller.connection.br;
import static Controller.connection.rs;

public class loginin { // 로그인을 위한 클래스

    public static boolean isLogin = false; // 로그인이 되었는지 확인 하는 변수
    public static String StId = ""; // 고객 아이디
    public static int c_no; // 고객 번호

    public static void checkLog(String id, String pwd, ResultSet rs) throws SQLException {
        // 입력한 아이디와 비밀번호가 db에 정확하게 있는지 확인 하기 위한 메소드

        while (rs.next()) { // 결과값에 커서가 행마다 내려가면서
            if (rs.getString(8).equals(id) && rs.getString(9).equals(pwd)) {
                // 아이디가 행에 존재하는 것과 일치하면서 비밀번호도 일치하면
                isLogin = true; // 로그인이 되었다고 확인 하는 변수에 true 값을 준다
                break;
            }
        }
    } // 로그인 확인 메소드

    public static void logining() throws IOException, SQLException {
        // 첫 메뉴에서 로그인 메뉴 선택시 이 메소드가 호출된다

        boolean bl = true; // while 문 탈출 변수

        while (bl) {
            System.out.println("1. 로그인 2. 회원가입");
            String choiceNum = br.readLine();
            switch (choiceNum) { // 원하는 메뉴 선택
                case "1":
                    rs = connection.stmt.executeQuery("select * from customer");
                    // 고객 정보가 담긴 테이블을 조회해 rs에 결과값 담는다
                    System.out.print("ID를 입력해 주세요 : ");
                    String id = br.readLine();
                    StId = id;
                    // id를 입력 받고 전역변수 StId에 담는다
                    // 개인 정보 수정시 입력받아 논 아이디를 이용해 비밀번호를 변경하기 위해 전역변수로 저장한다

                    System.out.print("PW를 입력해 주세요 : ");
                    String pwd = br.readLine();
                    // 비밀번호 입력 받는다

                    checkLog(id, pwd, rs);
                    // 결과값과 입력받은 아이디와 비밀번호를 기반으로 로그인 확인한다

                    if (isLogin) { // 로그인이 확인 되었으면
                        System.out.println(id + "님 환영합니다!!");

                        rs = connection.stmt.executeQuery("select c_no from customer where c_id = '" + id + "'");
                        // 입력 받은 고객아이디를 이용해 고객테이블에서 고객번호를 조회하여 rs에 담는다

                        while (rs.next()) {  // rs에 커서를 행마다 내리면서
                            c_no = rs.getInt(1);
                            // 결과값을 전역변수인 c_no 에 담는다
                            // 개인정보 수정, 예약확인, 예약 삭제 등 여러 기능에서 이용 된다
                        }

                        member.CheckLogin = true; // 로그인 되었다면 메인 메뉴에서 로그인 화면이 유지되게 전역변수 변경해 준다
                        bl = false; // while 탈출

                    } else { // 로그인 되지 않았아면
                        System.out.println("등록된 ID가 없습니다.");
                        System.out.print("1. 로그인 다시 진행 2. 회원가입 3. 홈 ===> ");
                        int choiceMenu = Integer.parseInt(br.readLine());
                        // 원하는 메뉴 선택
                        switch (choiceMenu) {
                            case 1: // 로그인 다시 실행
                                continue;
                            case 2: // 회원가입 메소드 실행
                                signin.setup();
                                break;
                            case 3:
                                break;
                        }
                        bl = false; // while 탈출
                    }
                    isLogin = false; // 로그인이 되이 않았기에 false
                    break;
                case "2": // 회원가입 메소드 실행
                    signin.setup();
                    break;
                default:
                    System.out.println("잘못된 번호를 누르셨습니다. 다시 입력하세요");
                    break;
            }

        }
    } // 로그인 함수
}
