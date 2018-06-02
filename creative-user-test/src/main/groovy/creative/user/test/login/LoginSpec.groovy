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
        CreativeUserInit.tearDown()
    }

    @Test
    public void loginUnknownUser() {
        def client = new CreativeUserClient()

        when: "I log in as an unknown user"
        def resp = client.login("foo", "bar")
        assert resp

        then: "I should get an unauthorised error response"
        resp.status == HttpStatus.SC_UNAUTHORIZED

        and: "I should not have an Access Token cookie"
        !resp.getHeaders('Set-Cookie')
    }

    @Test
    public void loginIncorrectPassword() {
        def client = new CreativeUserClient()

        when: "I log in as an known user with an incorrect password"
        def resp = client.login("brandon@example.com", "bar")
        assert resp

        then: "I should get an unauthorised error response"
        resp.status == HttpStatus.SC_UNAUTHORIZED
    }

    @Test
    public void loginValidUser() {
        def client = new CreativeUserClient()

        when: "I log in with a valid username and password"
        def resp = client.login("brandon@example.com", "password07")
        assert resp

        then: "I should get a created success response"
        resp.status == HttpStatus.SC_CREATED

        and:"I should get the logged in user back in the response"
        resp.responseData

        and: "I should have an Access Token cookie"
        resp.getHeaders('Set-Cookie').each {
            assert it.value.toString().startsWith("ACCESS_TOKEN=");
        }

        and: "The user should be logged in"
        def user=resp.responseData
        user.firstName == "Brandon"
        user.lastName == "Johnson"
        user.nickname == "bjohnson"
    }
}
