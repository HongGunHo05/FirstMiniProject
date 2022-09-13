package view;

import Controller.Login;

import java.io.IOException;
import java.sql.SQLException;

public class MainEntry { // 메인 클래스
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        // 실행시 호출 된다
        Login.login();
    }
}