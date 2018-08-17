package game.data.entity;

import game.consts.SessionConst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "history_session")
public class HistorySessionEntity extends AbstractEntity {


    @Column(name = "token")
    private String token;

    @Column(name = "username")
    private String username;

    @Column(name = "active_to_time")
    private Long activeToTime;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "archive_time")
    private Long archiveTime;

    protected HistorySessionEntity() {
    }

    public HistorySessionEntity(SessionEntity sessionEntity) {
        this.token = sessionEntity.getToken();
        this.username = sessionEntity.getUsername();
        this.activeToTime = sessionEntity.getActiveToTime();
        this.createTime = sessionEntity.getCreateTime();
        this.archiveTime = Calendar.getInstance().getTimeInMillis();
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
