package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Student;
import model.User;
import utils.DatabaseHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentsDao {

	private ObservableList<Student> students;

	// Columns for students table
	public enum COLUMNS {
		ID,
		NAME,
		COLLEGE,
		ROOMNO,
		ROLLNO
	}

	public static final String TABLE_NAME = "STUDENTS";

	// insert query for product
	private final String INSERT_QUERY = "INSERT INTO " + TABLE_NAME + " (" +
			COLUMNS.ID.toString() + "," +
			COLUMNS.NAME.toString() + "," +
			COLUMNS.COLLEGE.toString() + "," +
			COLUMNS.ROOMNO.toString() + "," +
			COLUMNS.ROLLNO.toString() +
			")  ";

	// create table query
	private final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " ( " +
			COLUMNS.ID.toString() + " varchar(255), " +
			COLUMNS.NAME.toString() + " varchar(255), " +
			COLUMNS.COLLEGE.toString() + " varchar(255), " +
			COLUMNS.ROOMNO.toString() + " varchar(255), " +
			COLUMNS.ROLLNO.toString() + " varchar(255) " +
			")";

	// update query
	private final String UPDATE_QUERY =
			"UPDATE " + TABLE_NAME +
					" SET " + COLUMNS.NAME + "= ? , " +
					COLUMNS.COLLEGE + " = ?, " +
					COLUMNS.ROOMNO + " = ?, " +
					COLUMNS.ROLLNO + "= ? " +
					"WHERE id = ?";

	public StudentsDao() {
		createTable();
		students = FXCollections.observableArrayList();
		students.addAll(loadStudents());
	}

	public ObservableList<Student> getStudents() {
		return students;
	}

	/**
	 * load Students from database
	 *
	 * @return
	 */
	public List<Student> loadStudents() {
		String query = "SELECT * FROM " + TABLE_NAME;
		ResultSet resultSet = DatabaseHelper.executeSelectQuery(query);
		return parseStudentsResultSet(resultSet);
	}

	/**
	 * add student to database
	 *
	 * @param student to be added to database
	 */

	public boolean addStudentToDatabase(Student student) {
		String query = INSERT_QUERY + " VALUES (?,?,?,?,?)";
		int result = DatabaseHelper.executePreparedStatement(query, student);
		System.out.println(result);
		if (result == 1) {
			students.add(student);
			return true;
		}
		return false;
	}


	public boolean updateStudent(Student student) {
		return DatabaseHelper.updateEntity(UPDATE_QUERY, student) == 1;
	}

	/**
	 * remove student from database
	 *
	 * @param student to be removed
	 */
	public int removeStudentFromDatabase(Student student) {
		String query = "DELETE FROM  " + TABLE_NAME + " WHERE " + COLUMNS.ID + " = (?)";
		if (DatabaseHelper.executeQuery(query, student.getUuid().toString()) == 1) {
			students.remove(student);
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
	private List<Student> parseStudentsResultSet(ResultSet resultSet) {
		List<Student> studentList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				// while there is data
				Student student = new Student();
				// set the parameters to product
				student.setUuid(UUID.fromString(resultSet.getString(COLUMNS.ID.toString())));
				student.setName(resultSet.getString(COLUMNS.NAME.toString()));
				student.setRollno(resultSet.getString(COLUMNS.ROLLNO.toString()));
				student.setCollege(resultSet.getString(COLUMNS.COLLEGE.toString()));
				student.setRoomno(resultSet.getString(COLUMNS.ROOMNO.toString()));
				studentList.add(student);
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
		return studentList;
	}

}
