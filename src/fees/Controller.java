package fees;

import dao.FeesDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Fee;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	public Button homeButton;
	public Button usersButton;
	public Button feesButton;
	public Button exitButton;
	public TableView<Fee> feesTable;
	public Button addButton;
	public TextField amountTextField;
	public TextField rollnoTextField;
	public Button updateButton;
	public Button clearButton;
	private FeesDao dao;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		dao = new FeesDao();
		feesTable.setItems(dao.getFees());
		addButton.setOnAction((event -> {
			if (dao.addFeeToDatabase(new Fee(
					getText(rollnoTextField),
					Double.parseDouble(getText(amountTextField))
			))) clearFields();
		}));

		updateButton.setOnAction((event -> {
			updateButton.setVisible(false);
			addButton.setVisible(true);
			Fee fee = feesTable.getSelectionModel().getSelectedItem();
			fee.setRollno(getText(rollnoTextField));
			fee.setAmount(Double.parseDouble(getText(amountTextField)));
			if (dao.updateFee(fee)) clearFields();
			feesTable.refresh();
		}));

		clearButton.setOnAction((event -> {
			clearFields();
			if (updateButton.isVisible()) {
				updateButton.setVisible(false);
				addButton.setVisible(true);
			}
		}));
	}

	String getText(TextField field) {
		return field.getText().toUpperCase();
	}

	private void clearFields() {
		rollnoTextField.setText("");
		amountTextField.setText("");
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
			Fee fee = feesTable.getSelectionModel().getSelectedItem();
			System.out.println(dao.removeFeeFromDatabase(fee));
			feesTable.refresh();
			clearFields();
		} else if (keyCode == KeyCode.LEFT) {
			// shift the focus to listview
		}
	}

	public void onMouseClicked(MouseEvent mouseEvent) {
		Fee fee = feesTable.getSelectionModel().getSelectedItem();
		amountTextField.setText(String.valueOf(fee.getAmount()));
		rollnoTextField.setText(fee.getRollno());
		addButton.setVisible(false);
		updateButton.setVisible(true);
	}

}
