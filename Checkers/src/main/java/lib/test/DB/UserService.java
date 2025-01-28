package lib.test.DB;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<MainTable> getAllUsers() {
        return userRepository.findAll();
    }

    public MainTable saveUser(MainTable user) {
        return userRepository.save(user);
    }

    public Iterable<Object> findAll() {
        return Collections.singleton(userRepository.findAll());
    }

    // Other service methods
}