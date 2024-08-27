package ulb.infof307.g10.app.views.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is used to display pop up messages
 */
public final class PopUp {

    private PopUp(){}

    /**
     * This function is used to display an error pop-up
     * @param message The message that will be shown as error
     */
    public static void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This function is used to display an error pop-up
     * @param message The message that will be shown as error
     * @param title The title of the pop-up
     * @param header The header of the pop-up
     */
    public static void showError(String message, String title, String header){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Image image = new Image("https://tinyurl.com/4tsjsy22"); // Link to the image got from the internet
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    /**
     * This function is used to display a success pop-up
     * @param message The message that will be shown as success
     */
    public static void showSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This function is used to display a confirmation pop-up
     * @param message The message that will be shown as question for the confirmation
     * @param title The title of the pop-up
     */
    public static boolean showWarning(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> answer = alert.showAndWait();
        if(Objects.equals(answer.get(), yesButton)) return true;
        else if (Objects.equals(answer.get(), noButton)) return false;
        return false;
    }
}
