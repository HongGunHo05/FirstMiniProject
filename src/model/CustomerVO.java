package model;

public class CustomerVO { // 테이블명 + VO
    public final String ClassName = "Customer"; // 테이블 명

    private int C_No; // 테이블 속성을 타입과 이름에 맞게 변수 선언
    private String C_Name, C_Birth, C_Phone, C_Address, C_Sex, C_Email, C_ID, C_Password;


    //선언한 변수들의 Getter, Setter
    public String getClassName() {
        return ClassName;
    }

    public int getC_No() {
        return C_No;
    }

    public void setC_No(int c_No) {
        C_No = c_No;
    }

    public String getC_Name() {
        return C_Name;
    }

    public void setC_Name(String c_Name) {
        C_Name = c_Name;
    }

    public String getC_Birth() {
        return C_Birth;
    }

    public void setC_Birth(String c_Birth) {
        C_Birth = c_Birth;
    }

    public String getC_Phone() {
        return C_Phone;
    }

    public void setC_Phone(String c_Phone) {
        C_Phone = c_Phone;
    }

    public String getC_Address() {
        return C_Address;
    }

    public void setC_Address(String c_Address) {
        C_Address = c_Address;
    }

    public String getC_Sex() {
        return C_Sex;
    }

    public void setC_Sex(String c_Sex) {
        C_Sex = c_Sex;
    }

    public String getC_Email() {
        return C_Email;
    }

    public void setC_Email(String c_Email) {
        C_Email = c_Email;
    }

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }

    public String getC_Password() {
        return C_Password;
    }

    public void setC_Password(String c_Password) {
        C_Password = c_Password;
    }
}
