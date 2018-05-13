package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_performance_id")
    @SequenceGenerator(name = "seq_customer_id", sequenceName = "seq_customer_id")
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 30)
    private String telephoneNumber;

    @Size(max = 50)
    private String email;

    public Customer() {

    }

    public Customer(@Size(min = 2, max = 50) String firstName, @Size(min = 2, max = 50) String lastName, @Size(max = 30) String telephoneNumber, @Size(max = 50) String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
            Objects.equals(firstName, customer.firstName) &&
            Objects.equals(lastName, customer.lastName) &&
            Objects.equals(telephoneNumber, customer.telephoneNumber) &&
            Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, telephoneNumber, email);
    }
}
