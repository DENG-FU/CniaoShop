package cniao5shop.com.cniao5.cniaoshop.bean;

import java.io.Serializable;

/**
 * Created by DENGFU on 2016/5/24.
 */
public class Campaign implements Serializable {
    private Long id;
    private String title;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
