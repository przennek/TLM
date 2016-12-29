package pl.edu.agh.cassandra.repositories;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.model.ws.DbTest;
import pl.edu.agh.model.ws.DbTestLog;
import pl.edu.agh.model.ws.DbTestTree;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kamil on 26.12.2016.
 */
public interface TestsRepository extends CrudRepository<DbTest, UUID> {

    @Query("SELECT * from tests where tokenId = ?0")
    DbTest findTestById(UUID tokenId);

    @Query("SELECT * from tests")
    List<DbTest> findAllTests();

    @Query("INSERT INTO  tests (tokenId, className, moduleName, editLog, jsonData) VALUES (?0, ?1, ?2, [], ?3)")
    DbTestTree addTest(UUID tokenId, String className, String moduleName,  String jsonData);

    @Query("UPDATE tests SET className = ?1, moduleName = ?2 , jsonData = ?3 WHERE tokenId = ?0;")
    DbTestTree updateTest(UUID tokenId, String className, String moduleName, String jsonData);

    @Query("UPDATE tests SET editLog =  editLog + [?1] WHERE tokenId = ?0;")
    DbTestTree updateTestLog(UUID tokenId, String editLog);

    @Query("DELETE FROM tests WHERE tokenId = ?0")
    DbTestTree deleteTest(UUID id);
}