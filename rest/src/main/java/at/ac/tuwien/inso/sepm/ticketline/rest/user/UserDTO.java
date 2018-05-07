package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UserDTO", description = "A User DTO")
public class UserDTO {


    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The username of the useraccount")
    private String username;

    @ApiModelProperty(required = true, readOnly = true, name = "The password of the useraccount")
    private String password;

    @ApiModelProperty(required = true, readOnly = true, name = "Indicates whether the user is enabled or not")
    private boolean enabled;

    @ApiModelProperty(readOnly = true, name = "The number of tries the user had for authorization")
    private Integer strikes = 0;

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

    @Override
    public String toString() {
        return "User{"
            + "id=" + id
            + ", username='" + username + '\''
            + ", isEnabled=" + enabled
            + ", strikes=" + strikes +
            "}";
    }
    public static final class UserDTOBuilder {
        private Long id;
        private String username;
        private String password;
        private boolean enabled;
        private Integer strikes;

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

        public UserDTO build() {

            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setEnabled(enabled);
            userDTO.setStrikes(strikes);
            return userDTO;
        }
    }
}
