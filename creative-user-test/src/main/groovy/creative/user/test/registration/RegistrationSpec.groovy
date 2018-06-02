package creative.user.test.registration

import static org.junit.Assert.*

import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test

import creative.user.test.CreativeUserClient
import creative.user.test.CreativeUserInit

class RegistrationSpec {

    @Before
    public void setUp() throws Exception {
        CreativeUserInit.setUp()
    }

    @After
    public void tearDown() throws Exception {
        CreativeUserInit.tearDown()
    }

    @Test
    public void invalidRegistrationMissingFirstName() {
        given: "I am a new potential user"
        def client = new CreativeUserClient();

        when: "I register with valid registration details"
        def resp=client.register(null, "Marathe", "vmarathe", "vihung@marathe.net", "password");
        assert resp;

        then: "I should get a CREATED response"
        resp.status == HttpStatus.SC_CREATED
    }

    @Test
    public void invalidRegistrationMissingLastName() {
        given: "I am a new potential user"
        def client = new CreativeUserClient();

        when: "I register with valid registration details"
        def resp=client.register("Vihung", null,  "vmarathe", "vihung@marathe.net", "password");
        assert resp;

        then: "I should get a BAD REQUEST response"
        resp.status == HttpStatus.SC_BAD_REQUEST

        and: "THe Error code should be as specified"
        assert resp.responseData.errorCode == "ERR_REGISTRATION_REQUEST_INVALID"
    }

    @Test
    public void invalidRegistrationMissingNickname() {
        given: "I am a new potential user"
        def client = new CreativeUserClient();

        when: "I register with valid registration details"
        def resp=client.register("Vihung", "Marathe",  null, "vihung@marathe.net", "password");
        assert resp;

        then: "I should get a CREATED response"
        resp.status == HttpStatus.SC_CREATED
    }


    @Test
    public void duplicateEmailRegistration() {
        given: "I am a new potential user"
        def client1 = new CreativeUserClient();
        def client2 = new CreativeUserClient();

        and: "I register a known user with valid registration details"
        def resp=client1.register("Vihung1", "Marathe1", "vmarathe1", "vihung@marathe.net", "password1");
        assert resp;

        and: "I  get a CREATED response"
        assert resp.status == HttpStatus.SC_CREATED

        when : "I register a new user with the same email again"
        def resp2=client2.register("Vihung2", "Marathe2", "vmarathe2", "vihung@marathe.net", "password2");
        assert resp2;

        then: "I should get a CONFLICT response"
        resp2.status == HttpStatus.SC_CONFLICT

        and: "The error code should indicate the conflict is because of the email"
        def errorResponse = resp2.responseData;
        assert errorResponse.errorCode == "ERR_EMAIL_EXISTS"
    }

    @Test
    public void duplicateNicknameRegistration() {
        given: "I am a new potential user"
        def client1 = new CreativeUserClient();
        def client2 = new CreativeUserClient();

        and: "I register a known user with valid registration details"
        def resp=client1.register("Vihung1", "Marathe1", "vmarathe", "vihung1@marathe.net", "password1");
        assert resp;

        and: "I  get a CREATED response"
        assert resp.status == HttpStatus.SC_CREATED

        when : "I register a new user with the same email again"
        def resp2=client2.register("Vihung2", "Marathe2", "vmarathe", "vihung2@marathe.net", "password2");
        assert resp2;

        then: "I should get a CONFLICT response"
        resp2.status == HttpStatus.SC_CONFLICT

        and: "The error code should indicate the conflict is because of the email"
        def errorResponse = resp2.responseData;
        assert errorResponse.errorCode == "ERR_NICKNAME_EXISTS"
    }

    @Test
    public void validRegistration() {
        given: "I am a new potential user"
        def client = new CreativeUserClient();

        when: "I register with valid registration details"
        def resp=client.register("Vihung", "Marathe", "vmarathe", "vihung@marathe.net", "password");
        assert resp;

        then: "I should get a CREATED response"
        resp.status == HttpStatus.SC_CREATED

        and: "I should get the created user details in the response"
        def user=resp.responseData
        assert user.firstName == "Vihung"
        assert user.lastName == "Marathe"
        assert user.nickname == "vmarathe"
    }
}