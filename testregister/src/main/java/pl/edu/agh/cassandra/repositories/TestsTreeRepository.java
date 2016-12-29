package pl.edu.agh.cassandra.repositories;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.model.ws.DbTestTree;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kamil on 26.12.2016.
 */
public interface TestsTreeRepository extends CrudRepository<DbTestTree, String> {

    @Query("SELECT * from tests_tree where moduleName = ?0")
    DbTestTree findTreeByModuleName(String moduleName);

    @Query("SELECT * from tests_tree")
    List<DbTestTree> findAllTrees();

    @Query("INSERT INTO  tests_tree (moduleName, jsonData) VALUES (?0, ?1)")
    void  addTree(String moduleName, String jsonData);

    @Query("UPDATE tests_tree SET moduleName = ?0, jsonData = ?1 WHERE moduleName = ?0")
    void updateTree(String moduleName, String jsonData);

    @Query("DELETE FROM tests_tree WHERE moduleName = ?0")
    void  deleteTree(String moduleName);
}