package creative.user.model

import org.junit.Test

class UserSpec extends spock.lang.Specification {
    creative.user.model.User u = new creative.user.model.User();

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
    def 'Set and Get First Name'(){
        given:
        u != null;

        when:
        u.firstName = 'Vihung';

        then:
        u.firstName == 'Vihung';
    }

    @Test
    def 'Set and Get Last Name'(){
        given:
        u != null;

        when:
        u.lastName = 'Marathe';

        then:
        u.lastName == 'Marathe';
    }

    @Test
    def 'Set and Get Nickname'(){
        given:
        u != null;

        when:
        u.nickname = 'vmarathe';

        then:
        u.nickname == 'vmarathe';
    }

    @Test
    def 'Set and Get Mobile'(){
        given:
        u != null;

        when:
        u.mobile = '12345678';

        then:
        u.mobile == '12345678';
    }
}