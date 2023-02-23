package fpt.project.bsmart.moodle.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MoodleLoginResponse implements Serializable{

    private String $id;
    private int status;
    private String message;
    private List<LoginResponseData> data = new ArrayList<>();
    private int code;

    public static class LoginResponseData implements Serializable {
        private int status;
        private String message;
        private Long userid;
        private String token;
        private String typelogin;
        private Instant connecttime;
        private String versionno;
        private String imei;
        private Instant expire;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getUserid() {
            return userid;
        }

        public void setUserid(Long userid) {
            this.userid = userid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTypelogin() {
            return typelogin;
        }

        public void setTypelogin(String typelogin) {
            this.typelogin = typelogin;
        }

        public Instant getConnecttime() {
            return connecttime;
        }

        public void setConnecttime(Instant connecttime) {
            this.connecttime = connecttime;
        }

        public Instant getExpire() {
            return expire;
        }

        public void setExpire(Instant expire) {
            this.expire = expire;
        }

        public String getVersionno() {
            return versionno;
        }

        public void setVersionno(String versionno) {
            this.versionno = versionno;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoginResponseData> getData() {
        return data;
    }

    public void setData(List<LoginResponseData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
