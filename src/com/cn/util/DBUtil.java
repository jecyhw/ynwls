package com.cn.util;

import com.cn.dao.AEntityDao;
import com.cn.test.TestOutput;
import com.cn.util.File.DbParser;
import com.cn.util.File.JSAXParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static Connection conn = getConnection();//数据库连接引用
    static private Connection getConnection() {
        Connection connection = null;
        try {
            DbParser dbParser = DbParser.getInstance();
            Class.forName(dbParser.getDriver());
            connection = DriverManager.getConnection(dbParser.getUrl(), dbParser.getUserName(), dbParser.getPassword());
            TestOutput.println("数据库连接正常");
        } catch (ClassNotFoundException e) {
            TestOutput.println("找不到数据库的驱动文件:");
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            TestOutput.println("数据库连接失败");
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 查询结果返回一条数据
     * @param sql 查询的sql语句
     * @param values 对应参数的值列表
     * @return 返回一个Object对象,查询数据为空时返回null
     */
    static public Object query(AEntityDao dao, String sql, List values) {
        List result = queryMulti(dao, sql, values);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 查询多条数据
     * @param sql 查询的sql语句
     * @param values 对应参数的值列表
     * @return 返回一个List对象,list大小为0表示查询数据为空
     */
    static public List queryMulti(AEntityDao dao, String sql, List values) {
        List result = new ArrayList();
        PreparedStatement stat = null;
        try {
            stat = getPreparedStatement(sql);
            setParametersForStatement(values, stat);
            result = dao.getEntityList(stat.executeQuery());
        } catch (SQLException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(stat);
        }
        return result;
    }

    /**
     * 批量查询,多个select语句
     * @param sql
     * @param values
     * @return
     */
    static public List queryBatch(AEntityDao dao, String sql, List values) {
        List result = new ArrayList();
        PreparedStatement stat = null;
        try {
            stat = getPreparedStatement(sql);
            setParametersForStatement(values, stat);
            stat.execute();
            do {
                ResultSet set = stat.getResultSet();
                result.addAll(dao.getEntityList(set));
            } while (stat.getMoreResults());
        } catch (SQLException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        } finally {
            closePreparedStatement(stat);
        }
        return result;
    }

    /**
     * 插入数据
     * @param sql
     * @param values
     * @return
     */
    static public int insert(String sql, List values) {
        return executeUpdate(sql, values);
    }

    /**
     * 插入数据并返回插入的自增id
     * @param sql
     * @param values
     * @return
     */
    static public int insertAndReturnAutoIncreaseId(String sql, List values) {
        int count = 0;
        PreparedStatement stat = null;
        try {
            beginTrans();
            stat = getPreparedStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            setParametersForStatement(values, stat);
            count = stat.executeUpdate();
            commitTrans();
            ResultSet resultSet = stat.getGeneratedKeys();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            rollbackTrans();
            TestOutput.println(e.getMessage());
        } finally {
            closePreparedStatement(stat);
        }
        return count;
    }

    /**
     * 批量插入
     * @param sql
     * @param valuesList
     * @return
     */
    static public int insertBatch(String sql, List<List> valuesList) {
        return executeBatch(sql, valuesList);
    }

    /**
     * 删除数据
     * @param sql
     * @param values
     * @return
     */
    static public int delete(String sql, List values) {
        return executeUpdate(sql, values);
    }

    /**
     * 批量删除
     * @param sql
     * @param valuesList
     * @return
     */
    static public int deleteBatch(String sql, List<List> valuesList) {
        return executeBatch(sql, valuesList);
    }

    /**
     * 更新数据
     * @param sql
     * @param values
     * @return
     */
    static public int update(String sql, List values) {
        return executeUpdate(sql, values);
    }

    /**
     * 批量更新
     * @param sql
     * @param valuesList
     * @return
     */
    static public int updateBatch(String sql, List<List> valuesList) {
        return executeBatch(sql, valuesList);
    }

    static protected int executeUpdate(String sql, List values) {
        int count = 0;
        PreparedStatement stat = null;
        try {
            beginTrans();
            stat = getPreparedStatement(sql);
            setParametersForStatement(values, stat);
            count = stat.executeUpdate();
            commitTrans();
        } catch (SQLException e) {
            rollbackTrans();
            TestOutput.println(e.getMessage());
        } finally {
            closePreparedStatement(stat);
        }
        return count;
    }

    static protected int executeBatch(String sql, List<List> valuesList) {
        int count = 0;
        PreparedStatement stat = null;
        try {
            beginTrans();
            stat = getPreparedStatement(sql);
            for (List values : valuesList) {
                setParametersForStatement(values, stat);
                stat.addBatch();
            }
            stat.executeBatch();
            commitTrans();
        } catch (SQLException e) {
            rollbackTrans();
            TestOutput.println(e.getMessage());
        } finally {
            closePreparedStatement(stat);
        }
        return count;
    }

    static protected void setParametersForStatement(List values, PreparedStatement stat) throws SQLException {
        for (int i = values.size(); i > 0; i--) {
            stat.setObject(i, values.get(i - 1));
        }
    }

    static public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     * 插入一条记录，获取自动生成id时使用
     *
     * @param sql
     * @param autoGeneratedKeys
     * @return
     * @throws SQLException
     */
    static public PreparedStatement getPreparedStatement(String sql, int autoGeneratedKeys) throws SQLException {
        TestOutput.println(sql);
        if (conn == null || conn.isClosed()) {
            TestOutput.println("Connection closed, reconnection now");
        }
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    static public void closePreparedStatement(PreparedStatement stat) {
        try {
            if (stat != null) {
                TestOutput.println(stat.toString());
                stat.close();
            }
        } catch (SQLException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }

    static public void beginTrans() throws SQLException {
            conn.setAutoCommit(false);
    }

    static public void commitTrans() throws SQLException {
            conn.commit();
            conn.setAutoCommit(true);
    }

    static public void rollbackTrans() {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            TestOutput.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
