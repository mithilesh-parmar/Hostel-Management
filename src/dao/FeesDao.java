package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fee;
import model.Student;
import utils.DatabaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FeesDao {

	private ObservableList<Fee> fees;

	// Columns for table
	public enum COLUMNS {
		ID,
		ROLLNO,
		AMOUNT,
	}

	public static final String TABLE_NAME = "FEES";

	// insert query for product
	private final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " (" +
			COLUMNS.ID.toString() + "," +
			COLUMNS.ROLLNO.toString() + "," +
			COLUMNS.AMOUNT.toString() +
			")  ";

	// create table query
	private final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ( " +
			COLUMNS.ID.toString() + " varchar(255), " +
			COLUMNS.ROLLNO.toString() + " varchar(255), " +
			COLUMNS.AMOUNT.toString() + " varchar(255) " +
			")";

	// update query
	private final String UPDATE_QUERY =
			"UPDATE " + TABLE_NAME +
					" SET " + COLUMNS.AMOUNT + "= ? " +
					"WHERE id = ?";


	public FeesDao() {
		createTable();
		fees = FXCollections.observableArrayList();
		fees.addAll(loadFees());
	}

	public ObservableList<Fee> getFees() {
		return fees;
	}

	/**
	 * load  from database
	 *
	 * @return
	 */
	public List<Fee> loadFees() {
		String query = "SELECT * FROM " + TABLE_NAME;
		ResultSet resultSet = DatabaseHelper.executeSelectQuery(query);
		return parseResultSet(resultSet);
	}

	/**
	 * add  to database
	 *
	 * @param fee to be added to database
	 */

	public boolean addFeeToDatabase(Fee fee) {
		String query = INSERT_QUERY + " VALUES (?,?,?)";
		int result = DatabaseHelper.executePreparedStatement(query, fee);
		System.out.println(result);
		if (result == 1) {
			fees.add(fee);
			return true;
		}
		return false;
	}

	public boolean updateFee(Fee fee) {
		return DatabaseHelper.updateEntity(UPDATE_QUERY, fee) == 1;
	}


	/**
	 * remove student from database
	 *
	 * @param fee to be removed
	 */
	public int removeFeeFromDatabase(Fee fee) {
		String query = "DELETE FROM  " + TABLE_NAME + " WHERE " + COLUMNS.ID + " = (?)";
		if (DatabaseHelper.executeQuery(query, fee.getUuid().toString()) == 1) {
			fees.remove(fee);
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
	private List<Fee> parseResultSet(ResultSet resultSet) {
		List<Fee> feeList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				// while there is data
				Fee fee = new Fee();
				// set the parameters to product
				fee.setUuid(UUID.fromString(resultSet.getString(COLUMNS.ID.toString())));
				fee.setAmount(Double.parseDouble(resultSet.getString(COLUMNS.AMOUNT.toString())));
				fee.setRollno(resultSet.getString(COLUMNS.ROLLNO.toString()));
				feeList.add(fee);
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
		return feeList;
	}
}
