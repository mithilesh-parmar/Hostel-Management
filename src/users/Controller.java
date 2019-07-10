package users;

import dao.UsersDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	public Button homeButton;
	public Button usersButton;
	public TextField nameTextField;
	public TextField emailTextField;
	public PasswordField passTextField;
	public Button addButton;
	public Button updateButton;
	public Button clearButton;
	public TableView<User> usersTable;
	private UsersDao dao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dao = new UsersDao();

		usersTable.setItems(dao.getUsers());

		addButton.setOnAction((event -> {
			if (dao.addUserToDatabase(new User(
					getText(nameTextField),
					getText(emailTextField),
					getText(passTextField)
			)))clearFields();
		}));

		updateButton.setOnAction((event -> {
			updateButton.setVisible(false);
			addButton.setVisible(true);
			User user = usersTable.getSelectionModel().getSelectedItem();
			user.setName(getText(nameTextField));
			user.setEmail(getText(emailTextField));
			user.setPassword(getText(passTextField));
			if (dao.updateUser(user))clearFields();
			usersTable.refresh();
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
		emailTextField.setText("");
		passTextField.setText("");
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
		if (keyCode == KeyCode.BACK_SPACE){
			// delete the order from invoice table
			User u = usersTable.getSelectionModel().getSelectedItem();
			dao.removeUserFromDatabase(u);
			usersTable.refresh();
			clearFields();
		}else if (keyCode == KeyCode.LEFT){
			// shift the focus to listview
		}
	}

	public void onMouseClicked(MouseEvent mouseEvent) {
		User user = usersTable.getSelectionModel().getSelectedItem();
		nameTextField.setText(user.getName());
		emailTextField.setText(user.getEmail());
		passTextField.setText(user.getPassword());
		addButton.setVisible(false);
		updateButton.setVisible(true);
	}
}
