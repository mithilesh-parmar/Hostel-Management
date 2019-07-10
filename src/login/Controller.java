package login;

import dao.UsersDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	public PasswordField passwordField;
	public TextField emailField;
	private UsersDao dao;
	private List<User> users;

	public void loginUser(ActionEvent actionEvent) throws IOException {
		if (checkUserCredentials()) {
			emailField.getScene().setRoot(FXMLLoader.load(getClass().getResource("/home/display.fxml")));
		} else showAlert();
	}

	private void showAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText("Email and password does not match");
		alert.initOwner(emailField.getScene().getWindow());
		alert.showAndWait();

	}

	private boolean checkUserCredentials() {
		if (emailField.getText().matches("root@root.com") && passwordField.getText().matches("root")) {
			return true;
		}
		for (User u : users) {
			String pass = u.getPassword();
			String email = u.getEmail();
			if (getText(passwordField).matches(pass) && getText(emailField).matches(email)) {
				return true;
			}
		}
		return false;
	}

	private String getText(TextField field) {
		return field.getText().toUpperCase();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dao = new UsersDao();
		users = new ArrayList<>();
		users.addAll(dao.getUsers());
	}
}
