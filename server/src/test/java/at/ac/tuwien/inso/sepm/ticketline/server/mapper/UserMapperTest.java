package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class UserMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private UserMapper userMapper;

    private static final long USER_ID = 1;
    private static final String USER_NAME = "test";
    private static final String USER_PASS = "test";
    private static final boolean USER_ENABLED = true;
    private static final int USER_STRIKES = 3;
    private static final String USER_PASS_CHANGE_KEY = null;
    private static final HashSet<String> USER_ROLES = new HashSet<>(Arrays.asList("USER"));
    private static final HashSet<News> USER_READ_NEWS = new HashSet<News>();

    @Test
    public void shouldMapUserToUserDTO() {
        User user = User.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .password(new BCryptPasswordEncoder(10).encode(USER_PASS))
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .passwordChangeKey(USER_PASS_CHANGE_KEY)
            .roles(USER_ROLES)
            .readNews(USER_READ_NEWS)
            .build();

        UserDTO userDTO = userMapper.userToUserDTO(user);
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getId()).isEqualTo(USER_ID);
        assertThat(userDTO.getUsername()).isEqualTo(USER_NAME);
        assertThat(userDTO.isEnabled()).isEqualTo(USER_ENABLED);
        assertThat(userDTO.getStrikes()).isEqualTo(USER_STRIKES);
        assertThat(userDTO.getRoles()).containsExactlyElementsOf(USER_ROLES);
    }

    @Test
    public void shouldMapUserDTOToUser() {
        UserDTO userDTO = UserDTO.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .roles(USER_ROLES)
            .build();

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getUsername()).isEqualTo(USER_NAME);
        assertThat(user.isEnabled()).isEqualTo(USER_ENABLED);
        assertThat(user.getStrikes()).isEqualTo(USER_STRIKES);
        assertThat(user.getRoles()).containsExactlyElementsOf(USER_ROLES);
    }

    @Test
    public void shouldMapUserListToUserDTOList() {
        User user = User.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .password(new BCryptPasswordEncoder(10).encode(USER_PASS))
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .passwordChangeKey(USER_PASS_CHANGE_KEY)
            .roles(USER_ROLES)
            .readNews(USER_READ_NEWS)
            .build();

        List<UserDTO> userDTOList = userMapper.userListToUserDTOList(new ArrayList<>(Arrays.asList(user, user, user)));

        assertThat(userDTOList).isNotNull();
        Assert.assertTrue(userDTOList.size() == 3);

        for (UserDTO userDTO : userDTOList) {
            assertThat(userDTO).isNotNull();
            assertThat(userDTO.getId()).isEqualTo(USER_ID);
            assertThat(userDTO.getUsername()).isEqualTo(USER_NAME);
            assertThat(userDTO.isEnabled()).isEqualTo(USER_ENABLED);
            assertThat(userDTO.getStrikes()).isEqualTo(USER_STRIKES);
            assertThat(userDTO.getRoles()).containsExactlyElementsOf(USER_ROLES);
        }
    }

    @Test
    public void shouldMapUserDTOListToUserList() {
        UserDTO userDTO = UserDTO.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .roles(USER_ROLES)
            .build();

        List<User> userList = userMapper.userListDTOToUserList(new ArrayList<>(Arrays.asList(userDTO, userDTO, userDTO)));

        assertThat(userList).isNotNull();
        Assert.assertTrue(userList.size() == 3);

        for (User user : userList) {
            assertThat(user).isNotNull();
            assertThat(user.getId()).isEqualTo(USER_ID);
            assertThat(user.getUsername()).isEqualTo(USER_NAME);
            assertThat(user.isEnabled()).isEqualTo(USER_ENABLED);
            assertThat(user.getStrikes()).isEqualTo(USER_STRIKES);
            assertThat(user.getRoles()).containsExactlyElementsOf(USER_ROLES);
        }
    }
}
