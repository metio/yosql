package wtf.metio.yosql.utils;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSources {

  public static DataSource createDataSource(final int yep) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:h2:mem:YEP-" + yep);
    dataSource.setUsername("sa");
    dataSource.setMinimumIdle(1);
    dataSource.setMaximumPoolSize(1);
    return dataSource;
  }

}
