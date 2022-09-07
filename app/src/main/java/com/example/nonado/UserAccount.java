package com.example.nonado;

/**사용자 계정 정보 모델 클래스
 *
 */
public class UserAccount {
    private String id; //이메일? 아이디
    private String password; //비밀번호
    private String name; //이름
    private int point;
    private String location; // 위치 정보

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

    public String getLocation(){return location;}

    public void setLocation(String location){this.location = location;}


    public UserAccount(String id, String password, String name, int point, String location){
        this.id = id;
        this.password = password;
        this.name=name;
        this.point = point;
        this.location = location;
    }

}
