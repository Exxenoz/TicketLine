package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private Integer strikes = 0;

    @Column(nullable = true)
    private String passwordChangeKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "authorities", joinColumns = {
        @JoinColumn(name = "username", referencedColumnName = "username")
    })
    @Column(name = "authority", nullable = false)
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }

    public String getPasswordChangeKey() {
        return passwordChangeKey;
    }

    public void setPasswordChangeKey(String passwordChangeKey) {
        this.passwordChangeKey = passwordChangeKey;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private boolean enabled;
        private int strikes;
        private String passwordChangeKey;
        private Set<String> roles = new HashSet<>();

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder strikes(int strikes) {
            this.strikes = strikes;
            return this;
        }

        public UserBuilder passwordChangeKey(String passwordChangeKey) {
            this.passwordChangeKey = passwordChangeKey;
            return this;
        }

        public UserBuilder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setEnabled(enabled);
            user.setStrikes(strikes);
            user.setPasswordChangeKey(passwordChangeKey);
            user.setRoles(new HashSet<>(roles));
            return user;
        }
    }
}
