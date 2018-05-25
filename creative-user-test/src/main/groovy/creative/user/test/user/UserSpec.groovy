package creative.user.test.user

import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test

import creative.user.test.CreativeUserClient

class UserSpec {

    @Before
    public void setUp() throws Exception {
        //        CreativeUserInit.setUp()
    }

    @After
    public void tearDown() throws Exception {
        //        CreativeUserInit.tearDown()
    }

    @Test
    public void loggedInCurrentUser() {
        def client = new CreativeUserClient()

        when: "I log in as a valid user"
        def resp = client.login("brandon@example.com", "password07")
        assert resp

        and: 'I get current user details'
        resp=client.getCurrentUser();
        assert resp;

        then: "I should get an OK  response"
        assert resp.status == HttpStatus.SC_OK

        and:"I should get the logged in user details back in the response"
        assert resp.responseData

        and: "The user should be logged in"
        def user=resp.responseData
        assert user.firstName == "Brandon"
        assert user.lastName == "Johnson"
        assert user.nickname == "bjohnson"
    }
    @Test
    public void anonymousCurrentUser() {
        def client = new CreativeUserClient()

        when: "I am not logged in as a valid user"

        and: 'I get current user details'
        def resp=client.getCurrentUser();
        assert resp;

        then: "I should get an OK  response"
        assert resp.status == HttpStatus.SC_OK

        and:"I should get the logged in user details back in the response"
        assert resp.responseData

        and: "The user should be logged in"
        def user=resp.responseData
        assert user.firstName == "Brandon"
        assert user.lastName == "Johnson"
        assert user.nickname == "bjohnson"
    }
}
