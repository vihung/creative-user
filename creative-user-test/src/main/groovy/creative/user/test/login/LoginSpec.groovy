package creative.user.test.login

import static org.junit.Assert.*

import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test

import creative.user.test.CreativeUserClient
import creative.user.test.CreativeUserInit

class LoginSpec {

    @Before
    public void setUp() throws Exception {
        CreativeUserInit.setUp()
    }

    @After
    public void tearDown() throws Exception {
        CreativeUserInit.setUp()
    }

    @Test
    public void loginUnknownUser() {
        def client = new CreativeUserClient()

        when: "I log in as an unknown user"
        def resp = client.login("foo", "bar")
        assert resp

        then: "I should get an unauthorised error response"
        assert resp.status == HttpStatus.SC_UNAUTHORIZED
    }

    @Test
    public void loginIncorrectPassword() {
        def client = new CreativeUserClient()

        when: "I log in as an known user with an incorrect password"
        def resp = client.login("brandon@example.com", "bar")
        assert resp

        then: "I should get an unauthorised error response"
        assert resp.status == HttpStatus.SC_UNAUTHORIZED
    }

    @Test
    public void loginValidUser() {
        def client = new CreativeUserClient()

        when: "I log in with a valid username and password"
        def resp = client.login("brandon@example.com", "password07")
        assert resp

        then: "I should get a created success response"
        assert resp.status == HttpStatus.SC_CREATED

        and:"I should get the logged in user back in the response"
        assert resp.responseData

        and: "The user should be logged in"
        def user=resp.responseData
        assert user.firstName == "Brandon"
        assert user.lastName == "Johnson"
        assert user.nickname == "bjohnson"
    }
}
