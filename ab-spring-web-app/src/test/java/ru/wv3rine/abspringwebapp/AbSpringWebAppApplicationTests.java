package ru.wv3rine.abspringwebapp;

import org.hibernate.type.descriptor.jdbc.ObjectNullAsBinaryTypeJdbcType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.wv3rine.abspringwebapp.dao.UsersDAO;
import ru.wv3rine.abspringwebapp.models.User;
import ru.wv3rine.abspringwebapp.models.UserIdAndLogin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// https://sysout.ru/testirovanie-spring-boot-prilozheniya-s-testresttemplate/
// частично отсюда

// тестовое покрытие неполное, но и приложение
// простое



/// !!!!!!!!!!!!!! ///
/// Добавить тестов ///
/// !!!!!!!!!!!!!!!! ///
@ComponentScan("ru.wv3rine.abspringwebapp")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AbSpringWebAppApplicationTests.class)
@WithMockUser(username="admin",roles="ADMIN")
class AbSpringWebAppApplicationTests {
    private final TestRestTemplate testRestTemplate;

    //@Mock
    private final UsersDAO usersDAO;

    @Autowired
    AbSpringWebAppApplicationTests(TestRestTemplate testRestTemplate, UsersDAO usersDAO) {
        this.testRestTemplate = testRestTemplate;
        this.usersDAO = usersDAO;
    }

    // Какой есть хороший способ очищать БД? Я пока плохо понимаю,
    // какие из них лучше, потому что в интернете просто как будто пишут
    // "этот лучше" без объяснений))
    @AfterEach
    public void resetDb() {
        usersDAO.deleteAll();
    }

    @Test
    public void whenCreateUser_thenHttpStatusOk() {
        User user = addTestUser("peergynt", "12345");
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getLogin(), is(user.getLogin()));
        assertThat(response.getBody().getPassword(), is(user.getPassword()));
    }

    @Test
    public void whenGetBadUser_thenHttpStatusNotFound() {
        addTestUser("Peer Gynt", "12345");
        ResponseEntity<User> response = testRestTemplate.exchange("/users/{id}", HttpMethod.GET, null, User.class, 100);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void whenAddUser_thenHttpStatusOk() {
        User user = new User(null, "Peer Gynt", "peergynt", "12345", "urlol");
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void whenBadUserAdd_thenHttpStatusBadRequest() {
        User user = new User(null, "Peer Gynt", "peergynt", null, "urlol");
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void whenGetUsers_thenHttpStatusOkay_andAnswerIsOkay() {
        User user0 = addTestUser("peergynt", "12345");
        User user1 = addTestUser("hamlet", "123456");
        // Я здесь принимаю User, а не UserIdAndLogin, потому что с ним не работает))
        // нужно его походу классом делать, а тогда внутри dao не будет работать
        ResponseEntity<List<User>> response = testRestTemplate.exchange("/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), hasSize(2));
        assertThat(response.getBody().get(0).getId(), is(user0.getId()));
        assertThat(response.getBody().get(1).getId(), is(user1.getId()));
        assertThat(response.getBody().get(0).getLogin(), is(user0.getLogin()));
        assertThat(response.getBody().get(1).getLogin(), is(user1.getLogin()));
    }

    @Test
    public void whenPutUser_thenHttpStatusOkay_andAnswerIsOkay() {
        User userById = addTestUser("peergynt", "12345");
        HttpEntity<User> entity = new HttpEntity<>(new User(null, null, "hamlet", null, "url"));
        ResponseEntity<User> response = testRestTemplate.exchange("/users/{id}",
                HttpMethod.PUT, entity, User.class, userById.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(userById.getId()));
        assertThat(response.getBody().getLogin(), is("hamlet"));
        assertThat(response.getBody().getPassword(), is("12345"));
        assertThat(response.getBody().getUrl(), is("url"));
    }


    private User addTestUser(String login, String password) {
        User user = new User(null, null, login, password, "");
        return usersDAO.save(user);
    }

}
