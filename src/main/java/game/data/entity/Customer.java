package game.data.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */

@Entity
@Table(name = "customer")
public class Customer extends AbstractEntity implements Serializable {

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    protected Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, firstName='%s', lastName='%s']", getId(), firstName, lastName);
    }
}