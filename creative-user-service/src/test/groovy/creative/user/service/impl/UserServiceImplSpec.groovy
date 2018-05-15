package creative.user.service.impl;

import creative.user.model.RegistrationRequest
import creative.user.model.User
import creative.user.model.UserCredentials
import creative.user.repository.AccessTokenRepository
import creative.user.repository.UserCredentialsRepository
import creative.user.repository.UserRepository
import creative.user.validator.UserValidator

class UserServiceImplSpec extends spock.lang.Specification {
    UserServiceImpl s = new UserServiceImpl();

    AccessTokenRepository accessTokenRepository = Mock();
    UserCredentialsRepository userCredentialsRepository = Mock();
    UserRepository userRepository = Stub() {
        save (_) >> sampleUser;
    }

    UserValidator userValidator = Mock();

    User sampleUser = new User(
    firstName: "Vihung",
    lastName: "Marathe",
    mobile: "01234567890",
    nickname: "vmarathe"
    );

    UserCredentials sampleUserCredentials = new UserCredentials(
    email: "vihung@marathe.net",
    hashedPassword: "abcdefg"
    );


    def setup() {
        s.accessTokenRepository=accessTokenRepository;
        s.userCredentialsRepository=userCredentialsRepository;
        s.userRepository=userRepository;
        s.userValidator=userValidator;

        userCredentialsRepository.save(sampleUserCredentials) >> sampleUserCredentials;
        userRepository.save(sampleUser) >> sampleUser;
    }

    def 'Register User'(){
        given:
        RegistrationRequest registrationRequest = new RegistrationRequest(
                firstName: "Vihung",
                lastName: "Marathe",
                mobile: "01234567890",
                nickname: "vmarathe",
                email: "vihung@marathe.net",
                password: "password123"
                );

        //when:
        //s.registerUser(registrationRequest);

        //then:
        //1 * s.checkEmailExists("vihung@marathe.net");
    }
}