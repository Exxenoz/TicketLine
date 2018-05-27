package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class UserPasswordResetRequestDTO {

    @ApiModelProperty(readOnly = true, name = "Client generated token to change a users password with")
    private String passwordChangeKey;

    @ApiModelProperty(readOnly = true, name = "User, whose password will be reset")
    private UserDTO userDTO;

    public String getPasswordChangeKey() {
        return passwordChangeKey;
    }

    public void setPasswordChangeKey(String passwordChangeKey) {
        this.passwordChangeKey = passwordChangeKey;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public static UserPasswordResetRequestDTO.UserPasswordResetRequestDTOBuilder builder() {
        return new UserPasswordResetRequestDTO.UserPasswordResetRequestDTOBuilder();
    }

    @Override
    public String toString() {
        return "UserPasswordResetRequestDTO{" +
            "passwordChangeKey='" + passwordChangeKey + '\'' +
            ", userDTO=" + userDTO +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPasswordResetRequestDTO that = (UserPasswordResetRequestDTO) o;
        return Objects.equals(passwordChangeKey, that.passwordChangeKey) &&
            Objects.equals(userDTO, that.userDTO);
    }

    @Override
    public int hashCode() {

        return Objects.hash(passwordChangeKey, userDTO);
    }

    public static final class UserPasswordResetRequestDTOBuilder {
        private String passwordChangeKey;
        private UserDTO userDTO;

        public UserPasswordResetRequestDTOBuilder passwordChangeKey(String passwordChangeKey) {
            this.passwordChangeKey = passwordChangeKey;
            return this;
        }

        public UserPasswordResetRequestDTOBuilder userDTO(UserDTO userDTO) {
            this.userDTO = userDTO;
            return this;
        }

        public UserPasswordResetRequestDTO build() {
            UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
            userPasswordResetRequestDTO.setPasswordChangeKey(passwordChangeKey);
            userPasswordResetRequestDTO.setUserDTO(userDTO);
            return userPasswordResetRequestDTO;
        }
    }
}
