package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "Authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String authority;

    public static AuthorityBuilder builder() {
        return new AuthorityBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class AuthorityBuilder {
        private Long id;
        private String username;
        private String authority;

        public AuthorityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AuthorityBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthorityBuilder authority(String authority) {
            this.authority = authority;
            return this;
        }

        public Authority build() {
            Authority authority = new Authority();
            authority.setId(this.id);
            authority.setUsername(this.username);
            authority.setAuthority(this.authority);
            return authority;
        }
    }
}
