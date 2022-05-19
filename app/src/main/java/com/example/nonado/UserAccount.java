package com.example.nonado;

/**사용자 계정 정보 모델 클래스
 *
 */
public class UserAccount {
    private String id; //이메일? 아이디
    private String idToken; //firebase Uid(고유 토큰정보)
    private String password; //비밀번호
    private String name; //이름

    public UserAccount() { }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

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
}
