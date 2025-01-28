package lib.test.DB;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MainTable, Long> {
    // Custom query methods can be added here
}