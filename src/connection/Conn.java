package connection;

import model.Record;
import model.Session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oceanos on 01.12.2016.
 */
public class Conn {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void connect() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Oceanos/IdeaProjects/3DVisualiser/src/connection\\log_27_october.s3db");
        statmt = conn.createStatement();
        System.out.println("База Подключена!");
    }



    // --------Заполнение таблицы--------
    public static void WriteDB() throws SQLException
    {
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Petya', 125453); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    public static void ReadDB() throws ClassNotFoundException, SQLException
    {
        resSet = statmt.executeQuery("select * from session");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  phone = resSet.getString("type");
            System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "phone = " + phone );
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    public static List getSessions() throws SQLException {
        ArrayList<Session> sessions = new ArrayList<>();
        resSet = statmt.executeQuery("select * from session");
        while (resSet.next()){
            sessions.add(new Session(resSet.getInt("id"), resSet.getString("name"), resSet.getString("type"), resSet.getInt("time")));
        }
        return sessions;
    }

    public static List getRecords(long id) throws SQLException {
        List<Record> records = new ArrayList<>();
        resSet = statmt.executeQuery("select * from record WHERE session_id="+id);

        while (resSet.next()){

        }

        return records;
    }

    // --------Закрытие--------
    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

    public static void main(String[] args) {
        try {
            connect();
            System.out.println(getSessions());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}