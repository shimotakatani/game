package game.data.entity;

import game.consts.SessionConst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "session")
public class SessionEntity extends AbstractEntity {

    @Column(name = "token")
    private String token;

    @Column(name = "username")
    private String username;

    @Column(name = "active_to_time")
    private Long activeToTime;

    @Column(name = "create_time")
    private Long createTime;

    protected SessionEntity() {
    }

    private Long getActiveTime(){
        return Calendar.getInstance().getTimeInMillis() + SessionConst.ACTIVE_TIME_IN_MINUTES * 60 * 1000;
    }

    public SessionEntity(String token, String username, Long activeToTime) {
        this.token = token;
        this.username = username;
        this.activeToTime = activeToTime;
        this.createTime = Calendar.getInstance().getTimeInMillis();
    }

    public SessionEntity(String username) {
        this.username = username;
        this.activeToTime = getActiveTime();
        this.createTime = Calendar.getInstance().getTimeInMillis();
        this.token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getActiveToTime() {
        return activeToTime;
    }

    public void setActiveToTime(Long activeToTime) {
        this.activeToTime = activeToTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
