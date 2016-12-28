package pl.edu.agh.model.ws;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.UUID;

/**
 * Created by Kamil on 26.12.2016.
 */

@Data
@NoArgsConstructor
@Table(value = "tests_tree")
public class DbTestTree {

    @PrimaryKey("id") private UUID id;

    @Column("moduleName") private String moduleName;
    @Column("jsonData") private String jsonData;

    public DbTestTree(UUID id) {
        this.setId(id);
    }
}