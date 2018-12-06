package hello;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import javax.sql.DataSource;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
 
@Service
public class CatServiceImpl implements CatService {
        @Autowired
        private DataSource dataSource;
        private JdbcTemplate jdbcTemplate;
        private long generatedKey;
        private String rescuedstring;
 
        public void setDataSource(DataSource dataSource) {
                this.dataSource = dataSource;
                this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
        
        public void addCat(String name, Date rescued, Boolean vaccinated){
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.update("INSERT INTO cat(name,rescued,vaccinated)VALUES(?,?,?)",name,rescued,vaccinated );
        }

        public void createDb(){
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.update("CREATE table cat(id int not null auto_increment, name varchar(100) not null, rescued date not null, vaccinated tinyint(1) not null, primary key (id));");
        }
        
        public void deleteCat(String name, Long id){
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.update("DELETE FROM cat WHERE name='"+name+"' AND id="+id);
        }
        
        public List atriskcats(Date rescued){
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String stringRescued = df.format(rescued);
                String sql = "SELECT * FROM cat WHERE rescued < '"+ stringRescued +"' AND vaccinated = '0'";
                List catList = new ArrayList();
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.query(sql, new ResultSetExtractor() {
                        public List extractData(ResultSet rs) throws SQLException {
                            while (rs.next()) {
                                    String name = rs.getString("name");
                                    catList.add(name);
                            }
                            return catList;
                        }
                    }
                );
                System.out.println("catlist");
                return catList;        
        }
 
        public long getGeneratedKey(String name, Date rescued, Boolean vaccinated) {
                String sql ="INSERT INTO cat(name,rescued,vaccinated) VALUES(?,?,?)";
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                KeyHolder holder = new GeneratedKeyHolder();
                java.sql.Date rescuedsql = new java.sql.Date(rescued.getTime());
                System.out.println(rescuedsql);
                jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement statement = connection.prepareStatement(sql.toString(),
                                        Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, name);
                        statement.setDate(2, rescuedsql );
                        statement.setBoolean(3, vaccinated);
                        return statement;
                }
                }, holder);
                generatedKey = holder.getKey().longValue();
                System.out.println("generated key is " + generatedKey);
                return generatedKey;
        }
 
}