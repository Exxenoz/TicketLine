package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "UserDTO", description = "A User DTO")
public class UserDTO {


    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The username of the useraccount")
    private String username;

    @ApiModelProperty(required = true, readOnly = true, name = "The password of the useraccount")
    private String password;

    @ApiModelProperty(required = true, readOnly = true, name = "Indicates whether the users is enabled or not")
    private boolean enabled = true;

    @ApiModelProperty(readOnly = true, name = "The number of tries the users had for authorization")
    private Integer strikes = 0;

    @ApiModelProperty(readOnly = true, name = "A list of authority strings for the user")
    private List<String> authorities;

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

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

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", enabled=" + enabled +
            ", strikes=" + strikes +
            ", authorities=" + authorities +
            '}';
    }

    public static final class UserDTOBuilder {
        private Long id;
        private String username;
        private String password;
        private boolean enabled;
        private Integer strikes;
        private List<String> authorities;

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDTOBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserDTOBuilder strikes(Integer strikes) {
            this.strikes = strikes;
            return this;
        }

        public UserDTOBuilder authorities(List<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setEnabled(enabled);
            userDTO.setStrikes(strikes);
            userDTO.setAuthorities(authorities);
            return userDTO;
        }
    }
}
