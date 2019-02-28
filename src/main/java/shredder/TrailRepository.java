package shredder;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TrailRepository extends CrudRepository<Trail, Long> {
//    Set<Trail> findByUserId(Long id);
}
