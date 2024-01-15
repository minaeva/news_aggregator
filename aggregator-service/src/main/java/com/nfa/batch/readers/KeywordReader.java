package com.nfa.batch.readers;

import com.nfa.entity.secondary.SecondaryKeyword;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class KeywordReader extends JdbcCursorItemReader<SecondaryKeyword> implements ItemReader<SecondaryKeyword> {

    public KeywordReader(@Autowired @Qualifier("secondaryDataSource") DataSource secondaryDataSource) {
        setDataSource(secondaryDataSource);
        setSql("SELECT name FROM keyword ");
        setFetchSize(100);
        setRowMapper(new KeywordsRowMapper());
    }

    public static class KeywordsRowMapper implements RowMapper<SecondaryKeyword> {

        public SecondaryKeyword mapRow(ResultSet rs, int rowNum) throws SQLException {
            SecondaryKeyword secondaryKeyword = new SecondaryKeyword();
            secondaryKeyword.setName(rs.getString("name"));
            return secondaryKeyword;
        }

    }
}
