package game.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @JoinColumn(name = "rabbit_id")
    private RabbitEntity rabbitEntity;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RabbitEntity getRabbitEntity() {
        return rabbitEntity;
    }

    public void setRabbitEntity(RabbitEntity rabbitEntity) {
        this.rabbitEntity = rabbitEntity;
    }
}
