package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class UserPasswordChangeRequestDTO {

    @ApiModelProperty(readOnly = true, name = "From admin generated token to change a users password with")
    private String passwordChangeKey;

    @ApiModelProperty(required = true, readOnly = true, name = "The username of the useraccount")
    private String username;

    @ApiModelProperty(required = true, readOnly = true, name = "The new password of the useraccount")
    private String password;

    public String getPasswordChangeKey() {
        return passwordChangeKey;
    }

    public void setPasswordChangeKey(String passwordChangeKey) {
        this.passwordChangeKey = passwordChangeKey;
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

    public static UserPasswordChangeRequestDTO.UserPasswordChangeRequestDTOBuilder builder() {
        return new UserPasswordChangeRequestDTO.UserPasswordChangeRequestDTOBuilder();
    }

    @Override
    public String toString() {
        return "UserPasswordChangeRequestDTO{" +
            "passwordChangeKey='" + passwordChangeKey + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPasswordChangeRequestDTO that = (UserPasswordChangeRequestDTO) o;
        return Objects.equals(passwordChangeKey, that.passwordChangeKey) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(passwordChangeKey, username, password);
    }


    public static final class UserPasswordChangeRequestDTOBuilder {
        private String passwordChangeKey;
        private String username;
        private String password;

        public UserPasswordChangeRequestDTOBuilder passwordChangeKey(String passwordChangeKey) {
            this.passwordChangeKey = passwordChangeKey;
            return this;
        }

        public UserPasswordChangeRequestDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserPasswordChangeRequestDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserPasswordChangeRequestDTO build() {
            UserPasswordChangeRequestDTO userPasswordResetRequestDTO = new UserPasswordChangeRequestDTO();
            userPasswordResetRequestDTO.setPasswordChangeKey(passwordChangeKey);
            userPasswordResetRequestDTO.setUsername(username);
            userPasswordResetRequestDTO.setPassword(password);
            return userPasswordResetRequestDTO;
        }
    }
}
