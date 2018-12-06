package hello.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hello.model.Cat;

@Repository
public interface CatRepository extends CrudRepository<Cat,Long>{
    Cat findByName(String name);
}