package ulb.infof307.g10.app.views.Deck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.app.views.Util.PopUp;

/**
 * Controller for the create deck view
 */
public class CreateDeckController extends AbstractEditorController {

    private static final int WINDOW = CREATE_DECK_WINDOW;
    private DeckControllerInterface controller;
    private String categories = "";
    private List<String> listCategory = new ArrayList<>();

    @FXML
    public TextField packnameField;
    @FXML
    public TextField categoryField;

    public void setController(AbstractController controller){
        this.controller = (DeckControllerInterface)controller;
    }

    @FXML
    protected void onSaveClicked() {
        List<Object> information = new ArrayList<>();
        information.add(packnameField.getText());
        information.add(categories);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onAddCategoryClicked(){ //need to change this, it cannot modifie information
        String newCategory = categoryField.getText();
        if(!isCategoryAdded(newCategory)){
            categories += newCategory + ";" ;
            listCategory.add(newCategory);
            categoryField.clear();
        }else{
            PopUp.showError(ConstAppError.CATEGORY_ALREADY_ADDED);
        }
    }

    private boolean isCategoryAdded(String newCategory){
        for (String category : listCategory){
            if(Objects.equals(category, newCategory)) return true;
        }
        return false;
    }

    @FXML
    protected void onImportClicked() {
        List<Object> information = new ArrayList<>();
        information.add(IMPORT_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }
}
