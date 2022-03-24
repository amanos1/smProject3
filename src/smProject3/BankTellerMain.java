package smProject3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Driver Class that initiates the program
 * @author Aaron Browne, Harshkumar Patel
 */
public class BankTellerMain extends Application
{
	/**
	 * Starts the program and launches the window.
	 * @param stage The stage that will be displayed.
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Bank Teller");
		Parent root = FXMLLoader.load(getClass().getResource("BankTellerView.fxml"));
		stage.setScene(new Scene(root, 483, 687));
		stage.show();
	}

	/**
	 * Kicks off the program.
	 * @param args Command-line arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}
}
