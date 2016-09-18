package cniao5shop.com.cniao5.cniaoshop.msg;

/**
 * Created by DENGFU on 2016/7/23.
 */
public class LoginRespMsg<T> extends BaseRespMsg {
    private String token;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
