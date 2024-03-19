package step.learning.dal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.News;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class NewsDao {
    private final DbService dbService;
    private final Logger logger ;

    @Inject
    public NewsDao(DbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }
    public News getById( String id ) {
        String sql = "SELECT * FROM News WHERE id=?" ;
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString(1, id);
            ResultSet res = prep.executeQuery();
            return res.next() ? News.fromResultSet(res) : null ;
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + sql );
        }
        return null ;
    }

    public List<News> getAll() {
        return getAll(false);
    }
    public List<News> getAll(boolean withDeleted) {
        List<News> ret = new ArrayList<>();
        String sql = "SELECT * FROM News" ;
        if( ! withDeleted) {
            sql += " WHERE deleted_dt IS NULL" ;
        }
        try( Statement statement = dbService.getConnection().createStatement() ) {
            ResultSet res = statement.executeQuery( sql ) ;
            while( res.next() ) {
                ret.add( News.fromResultSet(res) ) ;
            }
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + sql );
        }
        return ret;
    }
    public boolean addNews(News news) {
        String sql = "INSERT INTO News(id, title, spoiler, `text`, image_url, created_dt)" +
                " VALUES( UUID(), ?, ?, ?, ?, ?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString(1, news.getTitle());
            prep.setString(2, news.getSpoiler());
            prep.setString(3, news.getText());
            prep.setString(4, news.getImageUrl());
            prep.setTimestamp(5, new Timestamp(news.getCreateDt().getTime()) );
            prep.executeUpdate();
            return true ;
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + sql );
        }
        return false ;
    }

    public boolean deleteNews(String id) {
        String sql = "UPDATE News SET deleted_dt = CURRENT_TIMESTAMP WHERE id=?";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString(1, id);
            prep.executeUpdate();
            return true ;
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + sql );
        }
        return false ;
    }

    public boolean installTable() {
        String sql = "CREATE TABLE  IF NOT EXISTS  News(" +
                "id         CHAR(36)     PRIMARY KEY DEFAULT( UUID() )," +
                "title      VARCHAR(256) NOT NULL," +
                "spoiler    VARCHAR(512) NOT NULL," +
                "text       TEXT         NOT NULL," +
                "image_url  VARCHAR(256) NOT NULL," +
                "created_dt DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP," +
                "deleted_dt DATETIME     NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4";

        try( Statement statement = dbService.getConnection().createStatement() ) {
            statement.executeUpdate( sql ) ;
            return true ;
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE, ex.getMessage() + " -- " + sql );
        }
        return false ;
    }
}
/*
Д.З. Авторизація з ролями
Обмежити доступ до детального перегляду новини, якщо вона видалена.
- додати параметр "withDeleted" методу NewsDao::getById( withDeleted )
- контролювати доступ користувача з сервлету і викликати відповідну форму NewsDao::getById
- змінити представлення детального перегляду новини: за умови, що вона видалена,
   відображати відмінним стилем або з повідомленням, що вона видалена
   !тільки користувачів, що мають на це право.
 */