package Backend.ChessApp.Users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        TypedQuery<User> theQuery = entityManager.createQuery("FROM User", User.class);
        // return query results
        return theQuery.getResultList();
    }

    @Override
    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUserEmail(String userEmail) {
        // create query
        TypedQuery<User> theQuery = entityManager.createQuery("" +
                "FROM User WHERE userEmail=:theData", User.class);

        // set query parameter
        theQuery.setParameter("theData", userEmail);

        // return query results
        return theQuery.getSingleResult();
    }

    @Override
    public User findByUserName(String userName) {
        // create query
        TypedQuery<User> theQuery = entityManager.createQuery("" +
                "FROM User WHERE userName=:theData", User.class);

        // set query parameter
        theQuery.setParameter("theData", userName);

        // return query results
        return theQuery.getSingleResult();
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    public void deleteById(int id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }

    @Override
    public boolean existsByUserEmail(String userEmail) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userEmail = :userEmail", Long.class)
                .setParameter("userEmail", userEmail)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByUserName(String userName) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.userName = :userName", Long.class)
                .setParameter("userName", userName)
                .getSingleResult();
        return count > 0;
    }
}
