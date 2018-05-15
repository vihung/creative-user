package creative.user.model

import org.junit.Test

class UserCredentialsSpec extends spock.lang.Specification {
    creative.user.model.UserCredentials u = new creative.user.model.UserCredentials();

    @Test
    def 'Set and Get Id'(){
        given:
        u != null;

        when:
        u.id = '1234';

        then:
        u.id == '1234';
    }


    @Test
    def 'Set and Get Email'(){
        given:
        u != null;

        when:
        u.email = 'Vihung';

        then:
        u.email == 'Vihung';
    }

    @Test
    def 'Set and Get Hashed Password'(){
        given:
        u != null;

        when:
        u.hashedPassword = 'abcdefg';

        then:
        u.hashedPassword == 'abcdefg';
    }


    @Test
    def 'Set and Get User Id'(){
        given:
        u != null;

        when:
        u.userId = '1234';

        then:
        u.userId == '1234';
    }
}