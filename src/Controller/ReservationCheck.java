package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import static Controller.connection.br;
import static Controller.connection.*;

public class ReservationCheck {
    static Scanner sc = new Scanner(System.in);

    // 예약번호 입력시 예약 내역(테이블) 출력
    public static void ReservationT() throws SQLException, IOException { // 내정보 보기
        System.out.println("예약확인 page");
        connection.rs = connection.stmt.executeQuery("Select * from reservation where C_NO = " + Controller.loginin.c_no);
        // 로그인 시 입력 받은 고객번호를 기준으로 예약테이블을 조회 후 결과값을 rs에 담는다 

        int rsCnt = 0;
        boolean a = true;

        while (a) {
            // 예약번호
            System.out.println("-------------------------------------------------------");
            System.out.println("예약번호\t\t방번호\t사용날짜\t예약가격");

            while (connection.rs.next()) {
                // 각 속성 인덱스와 타입에 맞게 변수에 저장
                String RE_NO = connection.rs.getString(1);
                String RI_NO = connection.rs.getString(2);
                String RE_DATE = connection.rs.getString(3);
                String RE_PRICE = connection.rs.getString(6);
                rsCnt++;

                // 저장한 변수들을 출력
                System.out.println(RE_NO + "\t" + RI_NO + "\t" + RE_DATE + "\t" + RE_PRICE);

            } // 정보출력 end
            System.out.println("1.예약취소");
            System.out.println("2.이전페이지 이동");

            switch (sc.nextInt()) {
                case 1:
                    if (rsCnt != 0) {
                        delete();
                        System.out.println("취소되었습니다.");
                    } else System.out.println("예약된 내역이 없습니다.");
                    a = false;
                    break;
                case 2:
                    a = false;
                    System.out.println("이전페이지 이동");
                    break;
                default:
                    System.out.println("1-2번만 눌러주세용~");
                    break;
            }
        } // 정보 출력 

    } // 예약 확인

    // 예약취소
    private static void delete() throws SQLException, IOException {
        int count = 0;
        int choose = 0;
        String reservationNumber = "";
        rs = stmt.executeQuery(" select re.re_no from pay p ,reservation re where p.re_no = re.re_no and re.c_no = " + Controller.loginin.c_no);
        // 로그인 시 입력 받은 고객 번호를 기준으로 예약번호 조회 후 rs에 결과값을 담는다

        while (choose != 1 && choose != 2) {
            System.out.print("전체 삭제는 1, 선택 삭제는 2를 누르세요 : ");
            choose = Integer.parseInt(br.readLine());

            switch (choose) { // 입력 받은 번호로 메뉴 선택
                case 1:
                    while (rs.next()) {
                        reservationNumber = rs.getString(1); // 조회 한 예약 번호를 변수에 넣어준다
                        connection.pstmt = connection.conn.prepareStatement("delete pay where RE_NO = '" + reservationNumber + "'");
                        connection.pstmt.executeUpdate();
                        connection.pstmt = connection.conn.prepareStatement("delete reservation where RE_NO = '" + reservationNumber + "'");
                        connection.pstmt.executeUpdate();
                        // 각각 삭제 하기 위해 쿼리문 사용
                        // 업데이트 시켜준다

                    }
                    System.out.println("전체 취소 되었습니다.");
                    break;

                case 2:
                    while (rs.next()) {
                        count++;
                    } // 배열 크기 정해주기 위해 count를 센다

                    rs = stmt.executeQuery(" select re.re_no from pay p ,reservation re where p.re_no = re.re_no and re.c_no = " + Controller.loginin.c_no);
                    // 로그인 시 입력 받은 고객 번호를 기준으로 예약번호 조회 후 rs에 결과값을 담는다

                    String[] sarrs = new String[count]; // 예약번호를 담기 위한 배열 선언
                    int i = 0;

                    while (rs.next()) { // 배열에 예약 번호를 담는다
                        sarrs[i] = rs.getString(1);
                        i++;
                    }

                    boolean bl, isbl;
                    isbl = true;
                    while (isbl) {  // 입력한 예약번호가 배열안의 배열번호와 일치 하는지 확인 하는 작업
                        bl = true;
                        while (bl) {
                            System.out.print("삭제를 원하는 예약 번호를 입력하세요 : ");
                            reservationNumber = br.readLine(); // 사용자 고르는 예약번호

                            for (int j = 0; j < count; j++) {
                                if (sarrs[j].equals(reservationNumber)) {
                                    bl = false;
                                }
                            }
                            if (bl) System.out.println("잘못 눌렀습니다. 다시 입력하세요");
                        }

                        connection.pstmt = connection.conn.prepareStatement("delete pay where RE_NO = '" + reservationNumber + "'");
                        connection.pstmt.executeUpdate();
                        connection.pstmt = connection.conn.prepareStatement("delete reservation where RE_NO = '" + reservationNumber + "'");
                        connection.pstmt.executeUpdate();
                        // 입력 받은 예약변호를 각각 삭제 하기 위해 쿼리문 사용
                        // 업데이트 시켜준다

                        System.out.println("예약이 취소되었습니다..");

                        boolean yon = true;
                        while (yon) {
                            System.out.println("다른 예약도 취소 하시겠습니까?");
                            System.out.println("Y / N 으로 입력하세요");
                            String yesorno = br.readLine();
                            if (yesorno.equalsIgnoreCase("Y")) {
                                yon = false;
                            } else if (yesorno.equalsIgnoreCase("N")) {
                                yon = false;
                                isbl = false;
                            } else {
                                System.out.println("잘못 눌렀습니다. 다시 누르세요.");
                            }
                        }

                    }
                    break;
                default:
                    System.out.println("잘못 눌렀습니다. 다시 누르세요.");
                    break;
            }

        } // 예약 취소
    } // 예약 삭제
}