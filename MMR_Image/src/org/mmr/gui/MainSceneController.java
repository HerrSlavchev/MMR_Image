package org.mmr.gui;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mmr.core.Context;
import org.mmr.core.Document;
import org.mmr.core.EContentType;

public class MainSceneController implements Initializable {

	@FXML
	private TitledPane indexCreationTitledPane;

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

	@FXML
	private TitledPane dataExplorationTitledPane;

	@FXML
	private TextField queryFileTextField;

	@FXML
	private TextField hueImportanceTextField;

	@FXML
	private TextField saturationImportanceTextField;

	@FXML
	private TextField brightnessImportanceTextField;

	@FXML
	private Button searchButton;

	@FXML
	private ProgressIndicator searchProgressIndicator;

	@FXML
	private GridPane queryFileGridPane;

	private final DirectoryChooser indexDirectoryChooser = new DirectoryChooser();

	private final DirectoryChooser dataDirectoryChooser = new DirectoryChooser();

	private final FileChooser queryFileChooser = new FileChooser();

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
		indexCreationTitledPane.setOnKeyPressed((keyEvent) -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER)) {
				createIndexButton.requestFocus();
				clickCreateIndexButton(null);
			}
		});

		histogramBinsChoiceBox.setItems(FXCollections.observableArrayList(8, 16, 32, 64, 128, 256));
		histogramBinsChoiceBox.setValue(Context.getHistogramBinCount());

		indexDirectoryChooser.setTitle("Choose an index directory");
		dataDirectoryChooser.setTitle("Choose a data directory");

		final Set<EContentType> allowedContentTypes = Context.getAllowedContentTypes();

		bmpCheckBox.setSelected(allowedContentTypes.contains(EContentType.BMP));
		jpegCheckBox.setSelected(allowedContentTypes.contains(EContentType.JPEG));
		pngCheckBox.setSelected(allowedContentTypes.contains(EContentType.PNG));

		dataExplorationTitledPane.setDisable(true);
		dataExplorationTitledPane.setOnKeyPressed((keyEvent) -> {
			if (keyEvent.getCode().equals(KeyCode.ENTER)) {
				searchButton.requestFocus();
				clickSearchButton(null);
			}
		});

		queryFileChooser.setTitle("Choose a query file");
		queryFileChooser.getExtensionFilters().add(getSupportedExtensionFilter());

		hueImportanceTextField.setText(Context.getHueImportance() + "");
		saturationImportanceTextField.setText(Context.getSaturationImportance() + "");
		brightnessImportanceTextField.setText(Context.getBrightnessImportance() + "");

		Platform.runLater(() -> indexDirectoryTextField.requestFocus());
	}

	private FileChooser.ExtensionFilter getSupportedExtensionFilter() {
		final List<String> supportedExtensions = Arrays.stream(EContentType.values())
				.map(contentType -> "*." + contentType.getExtension())
				.collect(Collectors.toList());

		return new FileChooser.ExtensionFilter("Image files", supportedExtensions);
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

			dataExplorationTitledPane.setDisable(false);
			queryFileTextField.requestFocus();
		}

		createIndexButton.setDisable(false);
		createIndexProgressIndicator.setVisible(false);
	}

	private boolean isIndexCreationFormValid() {
		if (!isDirectoryPathTextField(indexDirectoryTextField)) {
			return false;
		}

		if (!isDirectoryPathTextField(dataDirectoryTextField)) {
			return false;
		}

		final boolean noContentTypeSelected = !(bmpCheckBox.isSelected() || jpegCheckBox.isSelected() || pngCheckBox.isSelected());
		if (noContentTypeSelected) {
			bmpCheckBox.requestFocus();

			return false;
		}

		return true;
	}

	private boolean isDirectoryPathTextField(final TextField textField) {
		final String directoryPath = textField.getText();
		final Path directory = Paths.get(directoryPath);

		if (directoryPath.trim().isEmpty() || !Files.exists(directory) || !Files.isDirectory(directory)) {
			textField.requestFocus();

			return false;
		}

		return true;
	}

	@FXML
	private void clickChooseQueryFileButton(final ActionEvent actionEvent) {
		final Optional<File> queryFile = chooseFile(queryFileChooser);
		if (queryFile.isPresent()) {
			queryFileTextField.setText(queryFile.get().getAbsolutePath());
		}
	}

	/**
	 * Show a file chooser. Extra: remembers the last chosen directory.
	 */
	private Optional<File> chooseFile(final FileChooser fileChooser) {
		final File chosenFile = fileChooser.showOpenDialog(MainClass.getStage());
		if (chosenFile != null) {
			//save last choice
			final File parentDirectory = chosenFile.getParentFile();
			if (parentDirectory != null) {
				fileChooser.setInitialDirectory(parentDirectory);
			}
		}

		return Optional.ofNullable(chosenFile);
	}

	@FXML
	private void clickSearchButton(final ActionEvent actionEvent) {
		searchButton.setDisable(true);
		searchProgressIndicator.setVisible(true);

		if (isDataExplorationFormValid()) {
			//TODO
			Context.setQueryDocument(null);
			Context.setHueImportance(Float.parseFloat(hueImportanceTextField.getText()));
			Context.setSaturationImportance(Float.parseFloat(saturationImportanceTextField.getText()));
			Context.setBrightnessImportance(Float.parseFloat(brightnessImportanceTextField.getText()));

			updateQueryFilePresentation(null);

			searchButton.setDisable(false);
			searchProgressIndicator.setVisible(false);
		}
	}

	private boolean isDataExplorationFormValid() {
		final String queryFilePath = queryFileTextField.getText();
		final Path queryFile = Paths.get(queryFilePath);

		if (queryFilePath.trim().isEmpty() || !Files.exists(queryFile) || Files.isDirectory(queryFile)) {
			queryFileTextField.requestFocus();

			return false;
		}

		if (!isFloatTextField(hueImportanceTextField)) {
			return false;
		}

		if (!isFloatTextField(saturationImportanceTextField)) {
			return false;
		}

		if (!isFloatTextField(brightnessImportanceTextField)) {
			return false;
		}

		return true;
	}

	private void updateQueryFilePresentation(final Document documentBean) {
		queryFileGridPane.getChildren().clear();

		final Pane queryFileImageViewPane = new Pane();

		final ImageView queryFileImageView = new ImageView();
		queryFileImageView.setImage(new Image("file:" + queryFileTextField.getText(), true));
		queryFileImageView.setPreserveRatio(true);
		queryFileImageView.setSmooth(true);
		queryFileImageView.fitWidthProperty().bind(queryFileImageViewPane.widthProperty());
		queryFileImageView.fitHeightProperty().bind(queryFileImageViewPane.heightProperty());

		queryFileImageViewPane.getChildren().add(queryFileImageView);
		queryFileGridPane.add(queryFileImageViewPane, 0, 0);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Hue");
		barChart.setBarGap(0);
		/*
		final float[] hue = documentBean.getHistrogramHSB()[1];

		System.out.println(hue.length);

		for (int i = 0; i < hue.length; i++) {
			float j = hue[i];
			System.out.println(j);
			XYChart.Series series = new XYChart.Series();
			series.getData().add(new XYChart.Data("", j));

			barChart.getData().add(series);
		}

		queryFileGridPane.add(barChart, 1, 0);
		*/
	}

	private boolean isFloatTextField(final TextField textField) {
		final String floatAsString = textField.getText();
		boolean floatTextField = true;

		if (floatAsString.isEmpty()) {
			floatTextField = false;
		} else {
			try {
				Float.parseFloat(floatAsString);
			} catch (final NumberFormatException exception) {
				floatTextField = false;
				Logger.getGlobal().log(Level.INFO, exception.getMessage(), exception);
			}
		}

		if (!floatTextField) {
			textField.requestFocus();
		}

		return floatTextField;
	}

	/**
	 * Simple utility to show dialogs related to exceptions.
	 */
	private void logAndShowDialog(final Throwable throwable) {
		Logger.getGlobal().log(Level.SEVERE, throwable.getMessage(), throwable);

		final Stage dialogStage = new Stage();
		dialogStage.setResizable(false);
		dialogStage.setAlwaysOnTop(true);
		dialogStage.initModality(Modality.APPLICATION_MODAL);

		final VBox vbox = new VBox();
		vbox.getChildren().add(new Text(throwable.getMessage()));
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(5));

		dialogStage.setScene(new Scene(vbox));
		dialogStage.show();
	}

}
