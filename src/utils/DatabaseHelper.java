package utils;

import com.sun.rowset.CachedRowSetImpl;
import model.Fee;
import model.Student;
import model.User;

import java.sql.*;

public abstract class DatabaseHelper {

	private static final String DATABASE_NAME = "HOSTEL_MANAGEMENT";
	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String CONNECTION_URL = "jdbc:derby:"+DATABASE_NAME+";create=true;derby.language.sequence.preallocator=1";
	private static Connection connection;

	/**
	 * function to make a connection to database
	 */
	private static void connectDatabase(){
		// register the driver
		try{
			Class.forName(DRIVER).newInstance();
			connection = DriverManager.getConnection(CONNECTION_URL);
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * execute query to get RESULTSET (For Select statement)
	 * @param query
	 * @return
	 */
	public static ResultSet executeSelectQuery(String query){
		Statement statement = null;
		ResultSet resultSet = null;
		CachedRowSetImpl cachedRowSet = null;
		try{
			connectDatabase();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			cachedRowSet = new CachedRowSetImpl(); // cache the resultset
			cachedRowSet.populate(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				// close the connections
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return cachedRowSet;
	}

	/**
	 * used to execute query (drop statement)
	 *
	 * @param
	 * @param query
	 * @return
	 */
	public static int executeQuery(String query, String parameter){
		PreparedStatement statement = null;
		int result =0;
		try{
			connectDatabase(); // connect to database
			statement = connection.prepareStatement(query);
			statement.setString(1,parameter);
			result = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase(); // disconnect from database
		}
		return result;
	}

	/**
	 * used to execute prepared statement (Insert statements)
	 * @param query
	 * @return
	 */
	public static int executePreparedStatement(String query, User user){
		PreparedStatement statement = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			// set the parameters
			statement.setString(1,user.getUuid().toString());
			statement.setString(2,user.getName());
			statement.setString(3,user.getEmail());
			statement.setString(4,user.getPassword());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return 0;
	}


	/**
	 * used to execute prepared statement (Insert statements)
	 * @param query
	 * @return
	 */
	public static int executePreparedStatement(String query, Fee fee){
		System.out.println(query);
		PreparedStatement statement = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			// set the parameters
			statement.setString(1,fee.getUuid().toString());
			statement.setString(2,fee.getRollno());
			statement.setString(3, String.valueOf(fee.getAmount()));
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return 0;
	}

	/**
	 * used to execute prepared statement (Insert statements)
	 * @param query
	 * @return
	 */
	public static int executePreparedStatement(String query, Student student){
		PreparedStatement statement = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			// set the parameters
			statement.setString(1,student.getUuid().toString());
			statement.setString(2,student.getName());
			statement.setString(3,student.getCollege());
			statement.setString(4,student.getRoomno());
			statement.setString(5,student.getRollno());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return 0;
	}

	public static int updateEntity(String query,User user){
		PreparedStatement statement  = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			statement.setString(1,user.getName());
			statement.setString(2,user.getEmail());
			statement.setString(3,user.getPassword());
			statement.setString(4,user.getUuid().toString());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int updateEntity(String query,Fee fee){
		PreparedStatement statement  = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			statement.setString(1, String.valueOf(fee.getAmount()));
			statement.setString(2,fee.getUuid().toString());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int updateEntity(String query,Student student){
		PreparedStatement statement  = null;
		try{
			connectDatabase();
			statement = connection.prepareStatement(query);
			statement.setString(1,student.getName());
			statement.setString(2,student.getCollege());
			statement.setString(3,student.getRoomno());
			statement.setString(4,student.getRollno());
			statement.setString(5,student.getUuid().toString());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * create table if doesn't exists
	 * @param query
	 * @param tableName name of the table to be created
	 * @return
	 */
	public static int createTable(String query,String tableName){
		DatabaseMetaData databaseMetaData ;
		ResultSet resultSet = null;
		Statement statement = null;
		int result = 0;
		try{
			connectDatabase();
			databaseMetaData = connection.getMetaData();
			// find if any table with same name exists
			resultSet = databaseMetaData.getTables(null,null,tableName,null);
			if (!resultSet.next()) {
				// if not the create one
				statement = connection.createStatement();
				result = statement.executeUpdate(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement != null) statement.close();
				if (resultSet != null) resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return result;
	}

	/**
	 * drop the table from database
	 * @param tableName
	 * @return
	 */
	public static boolean dropTable(String tableName){
		String query = "drop table "+tableName;
		Statement statement = null;
		boolean result = false;
		try{
			connectDatabase();
			System.out.println("Query: "+query);
			statement = connection.createStatement();
			result = statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try{
				if (statement !=  null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			disconnectDatabase();
		}
		return result;
	}

	/**
	 * disconnect driver from database
	 */
	private static void disconnectDatabase(){
		try{
			if (connection != null) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
