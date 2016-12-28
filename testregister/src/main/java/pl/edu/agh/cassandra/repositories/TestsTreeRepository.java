package pl.edu.agh.cassandra.repositories;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.model.ws.DbTestTree;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kamil on 26.12.2016.
 */
public interface TestsTreeRepository extends CrudRepository<DbTestTree, UUID> {

    @Query("SELECT * from tests_tree where id = ?0")
    DbTestTree findTreeByModuleName(UUID id);

    @Query("SELECT * from tests_tree")
    List<DbTestTree> findAllTrees();

    @Query("INSERT INTO  tests_tree (id, moduleName, jsonData) VALUES (?0, ?1, ?2)")
    void  addTree(UUID id, String moduleName, String jsonData);

    @Query("UPDATE tests_tree SET moduleName = ?1, jsonData = ?2 WHERE id = ?0")
    void updateTree(UUID id, String moduleName, String jsonData);

    @Query("DELETE FROM tests_tree WHERE id = ?0")
    void  deleteTree(UUID id);
}