package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find all unread news entries for the given username.
     *
     * @param username the username of the user
     * @param pageable the page filter
     * @return ordered list of all unread news entries for the given username
     */
    @Query(value = "SELECT n" +
            " FROM News n, Users u" +
            " WHERE u.username = :username AND n NOT MEMBER OF u.readNews")
    Page<News> findAllUnreadByUsername(@Param("username") String username, Pageable pageable);

    /**
     * Find all read news entries for the given username.
     *
     * @param username the username of the user
     * @param pageable the page filter
     * @return ordered list of all read news entries for the given username
     */
    @Query(value = "SELECT n" +
        " FROM News n, Users u" +
        " WHERE u.username = :username AND n MEMBER OF u.readNews")
    Page<News> findAllReadByUsername(@Param("username") String username, Pageable pageable);

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();

}
