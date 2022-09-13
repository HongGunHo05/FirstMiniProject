package Controller;

import java.sql.SQLException;
import java.util.Scanner;

import model.CustomerVO;

import static Controller.connection.rs;
import static Controller.signin.checkPassword;

public class mypage {
    static Scanner sc = new Scanner(System.in);

    public static void mypagemenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("\n 마이페이지");
        System.out.println("\t 1. 개인정보");
        System.out.println("\t 2. 개인정보수정");
        System.out.println("\t 3. 회원탈퇴");
        System.out.println("\t 4. 메뉴로 돌아가기");
        System.out.println("-------------------------------------------------------");
    } // 메뉴

    public static void menu() throws SQLException {
        // 마이페이지 메뉴를 선택시 이 메소드 호출
        CustomerVO vo = new CustomerVO();
        // CustomerVO 클래스에 vo 객체 생성한다

        boolean a = true; // while문 탈출 변수

        while (a) {
            mypagemenu(); // 메뉴 호출

            switch (sc.nextInt()) { // 원하는 번호 입력 받는다
                case 1:
                    // 내 정보 보기
                    selectUSER(vo.getClassName()); // vo 객체를 이용해 설정한 클래스 이름 "Customer"를 가져와 함수에 넣어 호출한다
                    break;

                case 2:
                    // 개인정보 수정
                    selectUSER(vo.getClassName()); // vo 객체를 이용해 설정한 클래스 이름 "Customer"를 가져와 함수에 넣어 호출한다
                    update(vo.getClassName()); // 클래스 이름을 가져와 수정하는 함수에 넣어 호출한다
                    selectUSER(vo.getClassName()); // 다시 vo 객체를 이용해 설정한 클래스 이름 "Customer"를 가져와 함수에 넣어 호출한다
                    break;

                case 3:
                    // 회원 탈퇴
                    delete(vo.getClassName()); // 클래스 이름을 넣어 삭제하는 함수를 호출한다
                    a = false; //반복문 탈출
                    break;

                case 4:
                    // 메뉴로 돌아가기
                    System.out.println("메뉴로 돌아갑니다.");
                    a = false; //반복문 탈출
                    break;

                default:
                    System.out.println("1-4번만 눌러주세요~");
                    break;
            }
        }
    }

    private static void selectUSER(String className) throws SQLException { // 내정보 보기
        rs = connection.stmt.executeQuery("SELECT * FROM customer WHERE C_NO = " + Controller.loginin.c_no);
        // 고객 정보가 담긴 customer 테이블에서 전역변수로 저장한 고객 변수를 기준으로 조회해 결과 값을 rs에 저장한다

        // 컬럼생성
        System.out.println("-------------------------------------------------------");
        System.out.println("회원이름\t 생년월일\t 전화번호\t\t 주소\t 성별\t 이메일\t\t\t ID\t\t PASSWORD");
        while (rs.next()) {
            // 조회한 결과값에 커서를 행마다 내리면서
            // 각 속성 인텍스에 맞게 각 변수에 저장한 후
            String c_name = rs.getString(2);
            String c_birth = rs.getString(3);
            String c_phone = rs.getString(4);
            String c_address = rs.getString(5);
            String c_sex = rs.getString(6);
            String c_email = rs.getString(7);
            String c_id = rs.getString(8);
            String c_password = rs.getString(9);

            // 저장한 변수를 이용해 출력한다
            System.out.print(c_name + " \t " + c_birth + "\t" + c_phone + "\t" + c_address + "\t  " + c_sex + "\t\t" + c_email + "\t " + c_id + "\t ");

            for (int i = 0; i < c_password.length(); i++) { // 비밀번호 숨김처리
                System.out.print("*");
            }

        }
        System.out.println();
    } // 내정보 보기

    private static void update(String className) throws SQLException { // 수정

        System.out.println("수정할 휴대폰 번호 입력: ");
        String C_PHONE = sc.next();
        System.out.println("수정할 주소 입력: ");
        String C_ADDRESS = sc.next();
        System.out.println("수정할 이메일 입력: ");
        String C_EMAIL = sc.next();

        String C_PASSWORD = ""; // 비밀번호 입력 받기 위한 변수
        boolean isLoginon = true; // while문 탈출 변수

        while (isLoginon) {
            System.out.println("수정할 비밀번호 입력: ");
            C_PASSWORD = sc.next();

            String Checkpswd = checkPassword(C_PASSWORD, loginin.StId);
            // 회원가입을 할때 만든 비밀번호 유효성 체크 메소드 호출하고 반환된 결과 값 저장

            if (Checkpswd.length() > 0) { // 반환 값의 길이가 0보다 길다면
                System.out.println(Checkpswd); // 반환 된 값 출력
            } else isLoginon = false; // 길이가 0이라면 유효성 타당함으로 반복문 탈출

        }


        //''잘 찾아서 넣기
        connection.stmt.executeQuery("update customer set C_PHONE = '" + C_PHONE + "', C_ADDRESS = '" + C_ADDRESS + "', C_EMAIL = '" + C_EMAIL + "', C_PASSWORD = '" + C_PASSWORD + "' where C_NO= '" + Controller.loginin.c_no + "'");
        // 수정문 실행
        System.out.println("개인정보가 수정 되었습니다");
    } // 개인정보 수정

    // 회원 탈퇴
    private static void delete(String className) throws SQLException {

        System.out.print("패스워드 입력: ");
        String C_PASSWORD = sc.next();
        String checkpwd = "";

        rs = connection.stmt.executeQuery("SELECT * FROM customer WHERE C_NO = " + Controller.loginin.c_no);
        while (rs.next()) {
            checkpwd = rs.getString(9);
        }

        if (C_PASSWORD.equals(checkpwd)) {
            connection.stmt.executeQuery("delete from customer where C_NO = '"
                    + loginin.c_no + "' and C_PASSWORD = '" + C_PASSWORD + "'");
            // 입력 받은 비밀번호를 조건으로 삭제문 실행
            System.out.println("회원탈퇴되었습니다.");
            member.CheckLogin = false; // 로그아웃 되었고 로그아웃 메뉴를 보여주기 위해 전역변수 설정
        }else{
            System.out.println("틀렸습니다. 홈으로 돌아 갑니다.");
        }

    } // 회원 탈퇴
}
