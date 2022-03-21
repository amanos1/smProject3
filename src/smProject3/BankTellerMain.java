package smProject3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.text.Text; 
import javafx.stage.Stage;

public class BankTellerMain extends Application
{
	/*private void launch()
	{
	}*/

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Hello");

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(8);

		Label label1 = new Label("Action:");
		GridPane.setConstraints(label1, 0, 0);

		ChoiceBox<String> action = new ChoiceBox<>();
		action.getItems().addAll("Open", "Close", "Deposit", "Withdraw", "Print", "Print by Type", "Print with Interest and Fee", "Update Balances");
		GridPane.setConstraints(action, 1, 0);

		Label label2 = new Label("Acct Type:");
		GridPane.setConstraints(label2, 0, 1);

		ChoiceBox<String> type = new ChoiceBox<>();
		type.getItems().addAll("Checking", "College Checking", "Savings", "Money Market");
		GridPane.setConstraints(type, 1, 1);

		Label label3 = new Label("Acct Holder:");
		GridPane.setConstraints(label3, 0, 2);

		TextField holderName = new TextField();
		GridPane.setConstraints(holderName, 1, 2);

		Label label4 = new Label("Holder DOB:");
		GridPane.setConstraints(label4, 0, 3);

		TextField holderDOB = new TextField();
		holderDOB.setPromptText("MM/DD/YYYY");
		GridPane.setConstraints(holderDOB, 1, 3);

		Label label5 = new Label("Init Deposit:");
		GridPane.setConstraints(label5, 0, 4);

		TextField initInput = new TextField();
		GridPane.setConstraints(initInput, 1, 4);

		Label label6 = new Label("Deposit/Withdrawl Amount:");
		GridPane.setConstraints(label6, 0, 5);

		TextField amountInput = new TextField();
		GridPane.setConstraints(amountInput, 1, 5);

		Button quit = new Button("Quit");
		GridPane.setConstraints(quit, 0, 6);

		Button submit = new Button("Submit");
		GridPane.setConstraints(submit, 1, 6);

		quit.setOnAction(e -> stage.close());
		submit.setOnAction(e ->
		{
			System.out.println("U pressed submit bitch!");
		});

		grid.getChildren().addAll(label1, action, label2, type, label3, holderName, label4, holderDOB, label5, initInput, label6, amountInput, quit, submit);

		Scene scene = new Scene(grid, 300, 700);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
