package org.mmr.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mmr.core.Context;
import org.mmr.core.DocumentBean;
import org.mmr.core.Engine;
import org.mmr.core.EContentType;

public class MainSceneController implements Initializable {

    //Inject controls (names must correspond to IDs from fxml)
    //---required for indexing
    @FXML
    private CheckBox chbBMP;

    @FXML
    private CheckBox chbJPEG;

    @FXML
    private CheckBox chbPNG;

    @FXML
    private CheckBox chbTIFF;

    @FXML
    private TextField tfDir;

    //---required for search purposes
    @FXML
    private TextField tfQuery;

    //---required for result visualisation
    @FXML
    private TextArea taResults;

    private DirectoryChooser chooser = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooser = new DirectoryChooser();
        chooser.setTitle("Choose a directory");
    }

    /**
     * Show a directory chooser. Extra: remembers the last chosen directory.
     */
    @FXML
    private void jbDirClicked(ActionEvent ae) {
        File chosenDir = chooser.showDialog(MainClass.getStage());
        if (chosenDir != null) {
            Context.setChosenDirectory(chosenDir.toPath());
            //save last choice
            String dirPath = chosenDir.getAbsolutePath();
            File parentDir = chosenDir.getParentFile();
            if (parentDir != null) {
                chooser.setInitialDirectory(parentDir);
            }
            tfDir.setText(dirPath);
        }
    }

    @FXML
    private void jbBuildClicked(ActionEvent ae) {
        List<EContentType> chosenMIMEs = new ArrayList<>();
        if (chbBMP.isSelected()) {
            chosenMIMEs.add(EContentType.BMP);
        }
        if (chbJPEG.isSelected()) {
            chosenMIMEs.add(EContentType.JPEG);
        }
        if (chbPNG.isSelected()) {
            chosenMIMEs.add(EContentType.PNG);
        }
        if (chbTIFF.isSelected()) {
            chosenMIMEs.add(EContentType.TIFF);
        }

        Context.setAllowedContentTypes(chosenMIMEs);

        try {
            Engine.createIndex();
        } catch (RuntimeException eR) {
            eR.printStackTrace();
            showDialog(eR.getMessage());
        }
    }

    @FXML
    private void jbSearchClicked(final ActionEvent actionEvent) {
        final String query = tfQuery.getText();

        try {
            final List<DocumentBean> documentBeans = Engine.search(query);

            taResults.setText(documentBeans.size() + " results:\n\n");

            documentBeans.stream().forEach((documentBean) -> {
                taResults.appendText(documentBean.toString() + "\n");
            });
        } catch (IOException /*| ParseException*/ exception) {
            final String errorMessage = exception.getMessage();

            Logger.getGlobal().log(Level.SEVERE, errorMessage, exception);
        } catch (RuntimeException eR) {
            eR.printStackTrace();
            showDialog(eR.getMessage());
        }
    }

    /**
     * Simple utility to show dialogs.
     *
     * @param message - the text to be shown.
     */
    private void showDialog(String message) {
        Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(message)).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.show();
    }
}
