package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = AUTO, generator = "seq_customer_id")
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

    @Column(nullable = true)
    @Size(max = 50)
    private String email;

    @Column(nullable = true)
    private BaseAddress baseAddress;

    public Customer() {

    }

    public Customer(@Size(min = 2, max = 50) String firstName, @Size(min = 2, max = 50) String lastName, @Size(max = 30) String telephoneNumber, @Size(max = 50) String email, BaseAddress baseAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.baseAddress = baseAddress;
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

    public BaseAddress getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(BaseAddress baseAddress) {
        this.baseAddress = baseAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", telephoneNumber='" + telephoneNumber + '\'' +
            ", email='" + email + '\'' +
            ", baseAddress=" + baseAddress +
            '}';
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
            Objects.equals(email, customer.email) &&
            Objects.equals(baseAddress, customer.baseAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, telephoneNumber, email, baseAddress);
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static final class CustomerBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String telephoneNumber;
        private String email;
        private BaseAddress baseAddress;

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder telephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder address(BaseAddress baseAddress) {
            this.baseAddress = baseAddress;
            return this;
        }

        public Customer build() {
            Customer user = new Customer();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setTelephoneNumber(telephoneNumber);
            user.setEmail(email);
            user.setBaseAddress(baseAddress);
            return user;
        }
    }
}
