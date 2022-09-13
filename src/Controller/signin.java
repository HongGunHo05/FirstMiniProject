package Controller;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Controller.connection.*;

public class signin { // 회원가입 클래스
    public static int getLst = 0; // 고객번호의 마지막 번호를 가져와 저장하기 위한 변수
    // connect
    public static boolean isSignID = false; // 아이디를 사용할 수 있는지 체크하기 위한 변수
    public static boolean isSignPW = false; // 비밀번호를 사용할 수 있는지 체크하기 위한 변수
    public static boolean isWhile = false;  // 비밀번호, 아이디가 둘다 한번에 유효한지 체크 하는 변수

    //////////////////////////////////////// 비밀번호 확인 메서드
    public static String checkPassword(String pwd, String id) {
        // 입력한 비밀번호가 조건 틀에 유효한지 확인 하는 메소드
        String msg = ""; // 문제가 있을 시 각 문제마다 오류를 담아 보내기 위해 만든 변수

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if (!passMatcher1.find()) {
            msg = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.";
            return msg;
        }

        // 반복된 문자 확인
        Pattern passPattern2 = Pattern.compile("(\\w)\\1\\1\\1");
        Matcher passMatcher2 = passPattern2.matcher(pwd);

        if (passMatcher2.find()) {
            msg = "비밀번호에 동일한 문자를 과도하게 연속해서 사용할 수 없습니다.";
            return msg;
        }

        // 아이디 포함 확인
        if (pwd.contains(id)) {
            msg = "비밀번호에 ID를 포함할 수 없습니다.";
            return msg;
        }

        // 특수문자 확인
        Pattern passPattern3 = Pattern.compile("\\W");
        Pattern passPattern4 = Pattern.compile("[!@#$%^*+=-]");

        for (int i = 0; i < pwd.length(); i++) {
            String s = String.valueOf(pwd.charAt(i));
            Matcher passMatcher3 = passPattern3.matcher(s);

            if (passMatcher3.find()) {
                Matcher passMatcher4 = passPattern4.matcher(s);
                if (!passMatcher4.find()) {
                    msg = "비밀번호에 특수문자는 !@#$^*+=-만 사용 가능합니다.";
                    return msg;
                }
            }
        }

        //연속된 문자 확인
        int ascSeqCharCnt = 0; // 오름차순 연속 문자 카운트
        int descSeqCharCnt = 0; // 내림차순 연속 문자 카운트

        char char_0;
        char char_1;
        char char_2;

        int diff_0_1;
        int diff_1_2;

        for (int i = 0; i < pwd.length() - 2; i++) {
            char_0 = pwd.charAt(i);
            char_1 = pwd.charAt(i + 1);
            char_2 = pwd.charAt(i + 2);

            diff_0_1 = char_0 - char_1;
            diff_1_2 = char_1 - char_2;

            if (diff_0_1 == 1 && diff_1_2 == 1) {
                ascSeqCharCnt += 1;
            }

            if (diff_0_1 == -1 && diff_1_2 == -1) {
                descSeqCharCnt -= 1;
            }
        }

        if (ascSeqCharCnt > 1 || descSeqCharCnt > 1) {
            msg = "비밀번호에 연속된 문자열을 사용할 수 없습니다.";
            return msg;
        }
        isSignPW = true;

        return msg;
    } // 입력한 비밀번호가 조건 틀에 유효한지 확인 하는 메소드

    //////////////////////////////////////// 아이디 중복 확인 메서드
    public static String checkID(String id, ResultSet rs) throws SQLException {
        String msgId = "";
        while (rs.next()) {
            if (rs.getString(8).equals(id)) {
                msgId = "중복된 ID가 존재합니다. ";
                return msgId;
            }
        }
        isSignID = true;
        return msgId;
    }

    public static void setup() throws IOException, SQLException {
        isSignID = false;
        isSignPW= false;
        isWhile = false;
        // 회원가입 함수
        connection.rs = connection.stmt.executeQuery("select * from customer order by C_NO");
        // 고객 정보가 담긴 테이블인 coustomer를 C_NO 순으로 정렬해 조회 한 결괴를 rs에 담는다

        while (connection.rs.next()) { // 조회 결과에 커서를 행마다 내리면서
            signin.getLst = connection.rs.getInt(1); // 고객번호의 마지막 번호를 가져와 전역변수에 저장 한다
        }


        while (!isSignID && !isSignPW && !isWhile) { // 아이디유효성, 비밀번호유효성, 한번에 두개다 유효하지 않다면 반복한다
            System.out.println("**** 회원가입 ****");
            System.out.print("이름을 입력하세요 : ");
            String name = br.readLine(); // 이름을 입력 받는다
            System.out.print("사용할 ID를 입력해 주세요 : ");
            String id = br.readLine(); // id를 입력 받는다

            String checkId = checkID(id, connection.rs); // 조회한 결과와 입력 받은 아이디로 아이디유효성을 체크하는 메소드 호출


            if (isSignID) { // 아이디 유효성이 맞다면 -> 비밀번호로 넘어간다
                String pwd = ""; // 비밀번호 입력 받기 위해 선언
                while (!isSignPW) { // 비밀번호유효성이 맞지 않다면 반복

                    System.out.print("사용할 PW를 입력해 주세요(영문, 특수문자, 숫자 포함 8자 이상) : ");
                    pwd = br.readLine(); // 비밀번호 입력 받는다

                    String checkPw = checkPassword(pwd, id);
                    // 입력받은 아이디와 비밀번호를 기반으로 비밀번호 유효성 확인하는 메소드 호출 한 후
                    // // 비밀번호 유효성에 오류가 있을시 구문을 담을 변수에 반환값 저장

                    if (!isSignPW) { // 비밀번호 유효성이 맞지 않다면
                        System.out.println(checkPw); // 반환값 출력
                    }
                }

                System.out.print("생년월일을 입력하세요 : ");
                String birth = br.readLine();

                System.out.print("전화번호를 입력하세요 : ");
                String phone = br.readLine();

                System.out.print("주소를 입력하세요 : ");
                String addr = br.readLine();

                System.out.print("성별을 선택하세요(남성 : M / 여성 : F) : ");
                String sex = br.readLine();

                System.out.print("이메일을 입력하세요 : ");
                String email = br.readLine();

                connection.pstmt = connection.conn.prepareStatement("insert into customer values (?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
                // prepareStatement 명령문을 사용해 쿼리문을 이용해 customer 테이블에 데이터 삽입한다
                // 각 파라미터 인덱스 위치의 타입에 맞게 데이터 넣어준다
                connection.pstmt.setInt(1, (signin.getLst + 1));
                connection.pstmt.setNString(2, name);
                connection.pstmt.setNString(3, birth);
                connection.pstmt.setNString(4, phone);
                connection.pstmt.setNString(5, addr);
                connection.pstmt.setNString(6, sex);
                connection.pstmt.setNString(7, email);
                connection.pstmt.setNString(8, id);
                connection.pstmt.setNString(9, pwd);
                connection.pstmt.executeUpdate();

                System.out.println("가입이 완료되었습니다.");

                if (isSignPW && isSignID) { // 아이디유효성, 비밀번호 유효성 둘다 맞다면
                    isWhile = true;
                }


            } else System.out.println(checkId); //조회한 결과와 입력 받은 아이디로 아이디유효성을 체크하는 메소드 호출한 결과 값을 출력한다
        }

    } // 회원가입 함수
}