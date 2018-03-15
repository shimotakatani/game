package game.data.repositories;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */
import java.util.List;

import game.data.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}
