package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.DatabaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersDao {

	private ObservableList<User> users;

	// Columns for users table
	public enum COLUMNS {
		ID,
		NAME,
		EMAIL,
		PASSWORD,
	}

	public static final String TABLE_NAME = "USERS";

	// insert query for product
	private final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " (" +
			COLUMNS.ID.toString() + "," +
			COLUMNS.NAME.toString() + "," +
			COLUMNS.EMAIL.toString() + "," +
			COLUMNS.PASSWORD.toString() +
			")  ";

	// create table query
	private final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ( " +
			COLUMNS.ID.toString() + " varchar(255), " +
			COLUMNS.NAME.toString() + " varchar(255), " +
			COLUMNS.EMAIL.toString() + " varchar(255), " +
			COLUMNS.PASSWORD.toString() + " varchar(255) " +
			")";

	// update query
	private final String UPDATE_QUERY =
			"UPDATE " + TABLE_NAME +
					" SET " + COLUMNS.NAME + "= ? , " +
					COLUMNS.EMAIL + " = ?, " +
					COLUMNS.PASSWORD + "= ? " +
					"WHERE id = ?";

	public UsersDao() {
		createTable();
		users = FXCollections.observableArrayList();
		users.addAll(loadUsers());
		// create table on first initialization if not exists
	}

	public ObservableList<User> getUsers() {
		return users;
	}

	/**
	 * load Users from database
	 *
	 * @return
	 */
	public List<User> loadUsers() {
		String query = "SELECT * FROM " + TABLE_NAME;
		ResultSet resultSet = DatabaseHelper.executeSelectQuery(query);
		return parseUsersResultSet(resultSet);
	}

	/**
	 * add user to database
	 *
	 * @param user to be added to database
	 */

	public boolean addUserToDatabase(User user) {
		String query = INSERT_QUERY + " VALUES (?,?,?,?)";
		int result = DatabaseHelper.executePreparedStatement(query, user);
		System.out.println(result);
		if (result == 1) {
			users.add(user);
			return true;
		}
		return false;
	}

	public boolean updateUser(User user) {
		return DatabaseHelper.updateEntity(UPDATE_QUERY, user) == 1;
	}

	/**
	 * remove user from database
	 *
	 * @param user to be removed
	 */
	public int removeUserFromDatabase(User user) {
		String query = "DELETE FROM  " + TABLE_NAME + " WHERE " + COLUMNS.ID + " = (?)";
		if (DatabaseHelper.executeQuery(query, user.getUuid().toString())==1){
			users.remove(user);
			return 1;
		}
		return 0;
	}

	/**
	 * create table if does not exists
	 */
	public void createTable() {
		DatabaseHelper.createTable(CREATE_TABLE_QUERY, TABLE_NAME);
	}

	/**
	 * parse the resultset received into a list of users
	 *
	 * @param resultSet
	 * @return
	 */
	private List<User> parseUsersResultSet(ResultSet resultSet) {
		List<User> userList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				// while there is data
				User user = new User();
				// set the parameters to product
				user.setUuid(UUID.fromString(resultSet.getString(COLUMNS.ID.toString())));
				user.setEmail(resultSet.getString(COLUMNS.EMAIL.toString()));
				user.setPassword(resultSet.getString(COLUMNS.PASSWORD.toString()));
				user.setName(resultSet.getString(COLUMNS.NAME.toString()));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return userList;
	}

}
