package wtf.metio.yosql.utils;

import com.example.persistence.SchemaRepository;

import javax.sql.DataSource;

public class Schemata {

  public static void initSchema(DataSource dataSource) {
    final SchemaRepository schemaRepository = new SchemaRepository(dataSource);
    schemaRepository.dropCompaniesTable();
    schemaRepository.dropItemsTable();
    schemaRepository.dropPersonsTable();
    schemaRepository.createCompaniesTable();
    schemaRepository.createCompaniesTable();
    schemaRepository.createPersonsTable();
    schemaRepository.createPersonsTable2();
    repository.writePerson(1, "YEP-1");
  }

}
