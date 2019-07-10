package home;

import dao.StudentsDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Student;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	public Button homeButton;
	public Button usersButton;
	public TextField nameTextField;
	public TextField collegeTextField;
	public TextField roomnoTextField;
	public TextField rollNoTextField1;
	public Button addButton;
	public Button updateButton;
	public Button clearButton;
	public TableView<Student> studentTable;
	private StudentsDao dao;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dao =new  StudentsDao();
		studentTable.setItems(dao.getStudents());

		addButton.setOnAction((event -> {
			if (dao.addStudentToDatabase(new Student(
					getText(nameTextField),
					getText(collegeTextField),
					getText(roomnoTextField),
					getText(rollNoTextField1)
			)))clearFields();
		}));

		updateButton.setOnAction((event -> {
			updateButton.setVisible(false);
			addButton.setVisible(true);
			Student student = studentTable.getSelectionModel().getSelectedItem();
			student.setName(getText(nameTextField));
			student.setCollege(getText(collegeTextField));
			student.setRoomno(getText(roomnoTextField));
			student.setRollno(getText(rollNoTextField1));
			if (dao.updateStudent(student))clearFields();
			studentTable.refresh();
		}));

		clearButton.setOnAction((event -> {
			clearFields();
			if (updateButton.isVisible()){
				updateButton.setVisible(false);
				addButton.setVisible(true);
			}
		}));
	}
	String getText(TextField field){
		return field.getText().toUpperCase();
	}
	private void clearFields(){
		nameTextField.setText("");
		roomnoTextField.setText("");
		rollNoTextField1.setText("");
		collegeTextField.setText("");
	}

	public void onUsersButtonClicked(ActionEvent actionEvent) throws IOException {
		homeButton.getScene().setRoot(FXMLLoader.load(getClass().getResource("/users/display.fxml")));
	}

	public void onHomeButtonClicked(ActionEvent actionEvent) throws IOException {
		homeButton.getScene().setRoot(FXMLLoader.load(getClass().getResource("/home/display.fxml")));
	}

	public void onExitButtonClick(ActionEvent actionEvent) {
		System.exit(0);
	}

	public void onFeesButtonClick(ActionEvent actionEvent) throws IOException {
		homeButton.getScene().setRoot(FXMLLoader.load(getClass().getResource("/fees/display.fxml")));
	}

	public void onTableKeyPressed(KeyEvent keyEvent) {
		KeyCode keyCode = keyEvent.getCode();
		if (keyCode == KeyCode.BACK_SPACE) {
			// delete the order from invoice table
			Student student = studentTable.getSelectionModel().getSelectedItem();
			System.out.println(dao.removeStudentFromDatabase(student));
			studentTable.refresh();
			clearFields();
		} else if (keyCode == KeyCode.LEFT) {
			// shift the focus to listview
		}
	}

	public void onMouseClicked(MouseEvent mouseEvent) {
		Student student = studentTable.getSelectionModel().getSelectedItem();
		nameTextField.setText(student.getName());
		collegeTextField.setText(student.getCollege());
		rollNoTextField1.setText(student.getRollno());
		roomnoTextField.setText(student.getRoomno());
		addButton.setVisible(false);
		updateButton.setVisible(true);
	}
}
