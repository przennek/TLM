package pl.edu.agh.model.ws;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;
import java.util.UUID;

/**
 * Created by Kamil on 26.12.2016.
 */
@Data
@NoArgsConstructor
@Table(value = "tests")
public class DbTest {

    @PrimaryKey("tokenId") private UUID id;

    @Column("className") private String className;
    @Column("moduleName") private String moduleName;
    @Column("editLog") private List<DbTestLog> editLog;
    @Column("jsonData") private String jsonData;

    public DbTest(UUID id) {
        this.setId(id);
    }
}