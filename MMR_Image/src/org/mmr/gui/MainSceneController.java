package org.mmr.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
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

	@FXML
	private TextField indexDirectoryTextField;

	@FXML
	private TextField dataDirectoryTextField;

	@FXML
	private ChoiceBox<Integer> histogramBinsChoiceBox;

	@FXML
	private CheckBox bmpCheckBox;

	@FXML
	private CheckBox jpegCheckBox;

	@FXML
	private CheckBox pngCheckBox;

	@FXML
	private Button createIndexButton;

	@FXML
	private ProgressIndicator createIndexProgressIndicator;

	private final DirectoryChooser indexDirectoryChooser = new DirectoryChooser();

	private final DirectoryChooser dataDirectoryChooser = new DirectoryChooser();

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
		histogramBinsChoiceBox.setItems(FXCollections.observableArrayList(8, 16, 32, 64, 128, 256));
		histogramBinsChoiceBox.setValue(Context.getHistogramBinCount());

		indexDirectoryChooser.setTitle("Choose an index directory");
		dataDirectoryChooser.setTitle("Choose a data directory");

		final Set<EContentType> allowedContentTypes = Context.getAllowedContentTypes();

		bmpCheckBox.setSelected(allowedContentTypes.contains(EContentType.BMP));
		jpegCheckBox.setSelected(allowedContentTypes.contains(EContentType.JPEG));
		pngCheckBox.setSelected(allowedContentTypes.contains(EContentType.PNG));
	}

	@FXML
	private void clickChooseIndexFolderButton(final ActionEvent actionEvent) {
		final Optional<File> indexDirecotry = chooseDirectory(indexDirectoryChooser);
		if (indexDirecotry.isPresent()) {
			indexDirectoryTextField.setText(indexDirecotry.get().getAbsolutePath());
		}
	}

	@FXML
	private void clickChooseDataFolderButton(final ActionEvent actionEvent) {
		final Optional<File> dataDirecotry = chooseDirectory(dataDirectoryChooser);
		if (dataDirecotry.isPresent()) {
			dataDirectoryTextField.setText(dataDirecotry.get().getAbsolutePath());
		}
	}

	/**
	 * Show a directory chooser. Extra: remembers the last chosen directory.
	 */
	private Optional<File> chooseDirectory(final DirectoryChooser directoryChooser) {
		final File chosenDirectory = directoryChooser.showDialog(MainClass.getStage());
		if (chosenDirectory != null) {
			//save last choice
			final File parentDirectory = chosenDirectory.getParentFile();
			if (parentDirectory != null) {
				directoryChooser.setInitialDirectory(parentDirectory);
			}
		}

		return Optional.ofNullable(chosenDirectory);
	}

	@FXML
	private void clickCreateIndexButton(final ActionEvent actionEvent) {
		createIndexButton.setDisable(true);
		createIndexProgressIndicator.setVisible(true);

		if (isIndexCreationFormValid()) {
			Context.setIndexDirectory(Paths.get(indexDirectoryTextField.getText()));
			Context.setDataDirectory(Paths.get(dataDirectoryTextField.getText()));
			Context.setHistogramBinCount(histogramBinsChoiceBox.getValue());

			List< EContentType> selectedContentTypes = new ArrayList<>();

			if (bmpCheckBox.isSelected()) {
				selectedContentTypes.add(EContentType.BMP);
			}

			if (jpegCheckBox.isSelected()) {
				selectedContentTypes.add(EContentType.JPEG);
			}

			if (pngCheckBox.isSelected()) {
				selectedContentTypes.add(EContentType.PNG);
			}

			Context.setAllowedContentTypes(selectedContentTypes);
		}

		createIndexButton.setDisable(false);
		createIndexProgressIndicator.setVisible(false);
	}

	private boolean isIndexCreationFormValid() {
		final String indexDirectoryPath = indexDirectoryTextField.getText();
		if (indexDirectoryPath.isEmpty() || !isDirectoryPath(indexDirectoryPath)) {
			indexDirectoryTextField.setText("");
			indexDirectoryTextField.requestFocus();

			return false;
		}

		final String dataDirectoryPath = dataDirectoryTextField.getText();
		if (dataDirectoryPath.isEmpty() || !isDirectoryPath(dataDirectoryPath)) {
			dataDirectoryTextField.setText("");
			dataDirectoryTextField.requestFocus();

			return false;
		}

		final boolean noContentTypeSelected = !(bmpCheckBox.isSelected() || jpegCheckBox.isSelected() || pngCheckBox.isSelected());
		if (noContentTypeSelected) {
			bmpCheckBox.requestFocus();

			return false;
		}

		return true;
	}

	private boolean isDirectoryPath(final String path) {
		final Path file = Paths.get(path);

		return Files.exists(file) && Files.isDirectory(file);
	}

	@FXML
	private void jbSearchClicked(final ActionEvent actionEvent) {
		final String query = null;//tfQuery.getText();

		try {
			final List<DocumentBean> documentBeans = Engine.search(query);

			//taResults.setText(documentBeans.size() + " results:\n\n");
			documentBeans.stream().forEach((documentBean) -> {
				//	taResults.appendText(documentBean.toString() + "\n");
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
