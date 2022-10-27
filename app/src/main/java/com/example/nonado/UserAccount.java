package com.example.nonado;

/**사용자 계정 정보 모델 클래스
 *
 */
public class UserAccount {
    private String id; //이메일? 아이디
    private String password; //비밀번호
    private String name; //이름
    private String location; //위치
    private int point; // 포인트
    private String number; // 전화번호
    private String token; // 내 토큰

    public UserAccount() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint(){return point;}

    public void setPoint(int point){this.point = point;}

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setNumber(String number){this.number = number;}
    public String getNumber(){return number;}

    public void setToken(String token){this.token = token;}
    public String getToken(){return token;}

    public UserAccount(String id, String password, String name, int point, String location, String number, String token){
        this.id = id;
        this.password = password;
        this.name=name;
        this.point = point;
        this.location = location;
        this.number = number;
        this.token = token;
    }

}
