package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static Controller.connection.*;
import static Controller.resMember.*;

class resMember {
    public static int roomprice = 0, roomOpPrice = 0; // 방가격, 옵션 가격
    public static int totalPrice = 0; // 총 가격
    public static int choicenum = 0; // 방번호

    public static String resNum = "", roomtype = "", idate = "",
            roomOp = null, pwno = ""; //예약 번호, 룸타입, 날짜, 룸 옵션, 카드 결재 번호

}

public class Reservation {
    public static void Reserve() throws IOException, SQLException {

        System.out.print("원하는 날짜를 입력 하세요 : ");
        idate = br.readLine();

        connection.rs = connection.stmt.executeQuery(" select * from roominformation where ri_no not in( select ri_no from reservation where re_date = '" + idate + "') order by ri_no");
        // 없는 방 번호를 쿼리문을 통해 조회 하고 결과값을 rs에 담는다

        int[] arr = new int[6]; // 없는 방 번호 넣기 위한 배열
        int i = 0;
        while (rs.next()) {
            arr[i] = Integer.parseInt(rs.getString(1)); // 없는 방 번호 배열에 삽입
            System.out.println(arr[i] + "\t" + rs.getString(2)); // 없는 방 번호 유형 출력
            i++;
        }

        boolean bl = true; // 반복문 탈출 위한 변수
        while (bl) {
            System.out.print("원하는 방 번호를 누르세요 : ");
            choicenum = Integer.parseInt(br.readLine()); // 방번호 입력 하고 전역변수로 저장
            for (int j = 0; j < arr.length; j++) { // 입력한 방 번호가 배열에 있는지 확인
                if (choicenum == arr[j]) { // 있다면 반복문 탈출
                    bl = false;
                }
            }
            if (bl) {
                System.out.println("잘못된 번호를 눌렀습니다.");
            }
        } // 방 번호 선택

        System.out.print("룸 서비스를 원하시면 1 아니면 2를 누르세요 : ");
        switch (Integer.parseInt(br.readLine())) { // 룸서비스 사용유무 선택
            case 1:
                String[] roparr = new String[7]; // 룸서비스 번호 넣기 위한 변수
                i = 0;
                System.out.println("**룸 서비스 목록*******************************************");
                connection.rs = connection.stmt.executeQuery(" SELECT * FROM ROOMOPTION ");
                // 룸서비스 정보가 담긴 ROOMOPTION 테이블을 조회해 rs에 담는다
                while (rs.next()) { // 결과값에 커서를 한 행 씩 내리면서
                    System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3));
                    // 각 속성에 맞게 출력한다
                    roparr[i] = rs.getString(1); // 룸 옵션 번호를 배열에 넣는다
                    i++;
                }

                bl = true;

                while (bl) {
                    System.out.print("원하는 룸서비스 번호를 입력하세요 : ");
                    roomOp = br.readLine();

                    if (Arrays.asList(roparr).contains(roomOp)) { // 배열에 선택한 번호가 있다면
                        bl = false; // 반복문 탈출
                    } else {
                        System.out.println("잘못 눌렀습니다. 다시 선택하세요.");
                    }
                }

                connection.rs = connection.stmt.executeQuery(" SELECT RO_PRICE FROM ROOMOPTION WHERE RO_NO = '" + roomOp + "'");
                // 선택한 룸 서비스 번호를 기준으로 룸서비스 가격 조회 한 결과를 rs에 담는다

                while (rs.next()) {
                    roomOpPrice = rs.getInt(1); // 조회한 결과 값에서 원하는 값을 전역변수로 저장한다
                }
                break;
            default:
                break;
        } // 룸서비스

        connection.rs = connection.stmt.executeQuery("select RI_TYPE, RI_PRICE from RoomInformation where RI_NO = " + choicenum);
        // 방번호를 기준으로 룸 가격, 방 유형을 조회한 결과 값을 rs에 담는다
        while (rs.next()) {
            roomtype = rs.getString(1);
            roomprice = rs.getInt(2);
            // 각 원하는 값을 전역변수로 저장한다
        }
        // idate 날짜, choicenum 원하는 방 번호, rs 원하는 방 타입, 가격

        // 지금까지 선택한 결과 모두 출력
        System.out.println("고객님께서 고르신 방 번호는 " + choicenum + " 입니다.");
        System.out.println("고객님께서 고르신 날짜는 " + idate + " 입니다.");
        System.out.println("고객님께서 고르신 방 타입은 " + roomtype + " 입니다.");
        System.out.println("고객님께서 고르신 방 가격은 " + roomprice + " 입니다.");
        System.out.println("고객님께서 고르신 룸서비스 가격은 " + roomOpPrice + " 입니다.");
        totalPrice = roomOpPrice + roomprice;
        System.out.println("고객님께서 선택하신 상품의 총 가격은 " + totalPrice + " 입니다.");

        bl = true;
        while (bl) {
            System.out.println("결제 하시겠습니까?");
            System.out.print("결제 하시려면 Y, 홈으로 돌아가시려면 N을 누르세요 : ");
            String howmuch = br.readLine();

            if (howmuch.equalsIgnoreCase("y")) {

                int n = 0;

                while (n != 1 && n != 2) {
                    System.out.println("1. 카드 결제 \t 2. 무통장입금");
                    System.out.print("결제 방법을 선택하세요 : ");
                    n = Integer.parseInt(br.readLine());

                    // 입력 받은 결과 값으로 원하는 메뉴 호출
                    switch (n) {
                        case 1: // 신용카드 결제
                            Payment.Charge();
                            break;
                        case 2: // 무통장입금
                            Payment.deposit();
                            break;
                        default:
                            System.out.println("잘못 눌렀습니다. 다시 누르세요.");
                            break;
                    }
                }

                ReservationCheck.ReservationT(); // 결제가 끝나면 예약 화면 출력하는 메소드 호출

                bl = false;

            } else if (howmuch.equalsIgnoreCase("n")) {
                //모두 끝나면 바로 홈 화면으로 가게
                bl = false;
            } else {
                System.out.println("잘못 누르셨습니다.");
            }
        } //결제 원하시나요?

    }
}
