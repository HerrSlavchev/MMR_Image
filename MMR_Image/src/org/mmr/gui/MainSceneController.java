package org.mmr.gui;

import java.io.File;
import java.io.IOException;
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
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mmr.core.Context;
import org.mmr.core.Document;
import org.mmr.core.EContentType;
import org.mmr.core.Engine;
import org.mmr.core.Extractor;
import org.mmr.core.PersistentIndex;
import org.mmr.core.Similarity;

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
	private TextField queryDocumentTextField;

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
	private HBox queryDocumentHBox;

	@FXML
	private ListView<Similarity> similarityListView;

	private final DirectoryChooser indexDirectoryChooser = new DirectoryChooser();

	private final DirectoryChooser dataDirectoryChooser = new DirectoryChooser();

	private final FileChooser queryDocumentChooser = new FileChooser();

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

		queryDocumentChooser.setTitle("Choose a query file");
		queryDocumentChooser.getExtensionFilters().add(getSupportedExtensionFilter());

		hueImportanceTextField.setText(Context.getHueWeight() + "");
		saturationImportanceTextField.setText(Context.getSaturationWeight() + "");
		brightnessImportanceTextField.setText(Context.getBrightnessWeight() + "");

		similarityListView.setCellFactory((listView) -> {
			return new ListCell<Similarity>() {
				@Override
				public void updateItem(final Similarity similarity, final boolean empty) {
					super.updateItem(similarity, empty);

					if (similarity != null) {
						final GridPane imageGridPane = new GridPane();

						final StackPane imagePane = new StackPane();
						imagePane.setPrefSize(150, 150);
						imagePane.setAlignment(Pos.TOP_LEFT);

						final ImageView imageView = new ImageView();
						imageView.setImage(new Image("file:" + similarity.getDocument().getPath(), true));
						imageView.setPreserveRatio(true);
						imageView.fitWidthProperty().bind(imagePane.widthProperty());
						imageView.fitHeightProperty().bind(imagePane.heightProperty());
						imagePane.getChildren().add(imageView);

						final Label label = new Label("" + similarity.getValue());
						label.setStyle("-fx-background-color: white;");
						imagePane.getChildren().add(label);

						imageGridPane.add(imagePane, 0, 0);

						final float[][] histogramHSB = similarity.getDocument().getHistrogramHSB();
						imageGridPane.add(createHistogram("", histogramHSB[0], 150, 150), 1, 0);
						imageGridPane.add(createHistogram("", histogramHSB[1], 150, 150), 2, 0);
						imageGridPane.add(createHistogram("", histogramHSB[2], 150, 150), 3, 0);

						imageGridPane.add(new Label(similarity.getDocument().getPath()), 4, 0);

						setGraphic(imageGridPane);
					}
				}
			};
		});

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
			changeIndexDirectoryTextField(null);
		}
	}

	@FXML
	private void changeIndexDirectoryTextField(final ActionEvent actionEvent) {
		final String indexDirectoryPath = indexDirectoryTextField.getText();

		if (!indexDirectoryPath.isEmpty()) {
			try {
				final boolean loaded = PersistentIndex.load(Paths.get(indexDirectoryPath));
				if (loaded) {
					indexDirectoryTextField.setText(Context.getIndexDirectory().get().toString());
					dataDirectoryTextField.setText(Context.getDataDirectory().get().toString());
					histogramBinsChoiceBox.setValue(Context.getHistogramBinCount());

					final Set<EContentType> allowedContentTypes = Context.getAllowedContentTypes();
					bmpCheckBox.setSelected(allowedContentTypes.contains(EContentType.BMP));
					jpegCheckBox.setSelected(allowedContentTypes.contains(EContentType.JPEG));
					pngCheckBox.setSelected(allowedContentTypes.contains(EContentType.PNG));

					final Optional<Document> queryDocument = Context.getQueryDocument();
					if (queryDocument.isPresent()) {
						queryDocumentTextField.setText(queryDocument.get().getPath());
					}

					hueImportanceTextField.setText(Context.getHueWeight() + "");
					saturationImportanceTextField.setText(Context.getSaturationWeight() + "");
					brightnessImportanceTextField.setText(Context.getBrightnessWeight() + "");

					dataExplorationTitledPane.setDisable(false);
				}
			} catch (final IOException exception) {
				Logger.getGlobal().log(Level.SEVERE, null, exception);
				indexDirectoryTextField.requestFocus();
			}
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

			try {
				Engine.createIndex();
			} catch (IOException eIO) {
				throw new RuntimeException("Could not open file!");
			}

			similarityListView.getItems().clear();
			dataExplorationTitledPane.setDisable(false);
			queryDocumentTextField.requestFocus();
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
		final Optional<File> queryFile = chooseFile(queryDocumentChooser);
		if (queryFile.isPresent()) {
			queryDocumentTextField.setText(queryFile.get().getAbsolutePath());
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
			try {
				final Path queryDocumentPath = Paths.get(queryDocumentTextField.getText());
				final Optional<Document> queryDocument = Extractor.extract(queryDocumentPath, false);

				if (queryDocument.isPresent()) {
					final Document presentQueryDocument = queryDocument.get();

					Context.setQueryDocument(presentQueryDocument);
					Context.setHueWeight(Float.parseFloat(hueImportanceTextField.getText()));
					Context.setSaturationWeight(Float.parseFloat(saturationImportanceTextField.getText()));
					Context.setBrightnessWeight(Float.parseFloat(brightnessImportanceTextField.getText()));

					updateQueryDocumentPresentation(presentQueryDocument);

					similarityListView.getItems().clear();
					similarityListView.getItems().addAll(Engine.search());
				} else {
					queryDocumentTextField.requestFocus();
				}
			} catch (final Throwable throwable) {
				logAndShowDialog(throwable);
			}
		}

		searchButton.setDisable(false);
		searchProgressIndicator.setVisible(false);
	}

	private boolean isDataExplorationFormValid() {
		final String queryFilePath = queryDocumentTextField.getText();
		final Path queryFile = Paths.get(queryFilePath);

		if (queryFilePath.trim().isEmpty() || !Files.exists(queryFile) || Files.isDirectory(queryFile)) {
			queryDocumentTextField.requestFocus();

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

	private void updateQueryDocumentPresentation(final Document documentBean) {
		queryDocumentHBox.getChildren().clear();

		final Pane queryDocumentImageViewPane = new Pane();
		queryDocumentImageViewPane.setPrefSize(150, 150);

		final ImageView queryDocumentImageView = new ImageView();
		queryDocumentImageView.setImage(new Image("file:" + documentBean.getPath(), true));
		queryDocumentImageView.setPreserveRatio(true);
		queryDocumentImageView.fitWidthProperty().bind(queryDocumentImageViewPane.widthProperty());
		queryDocumentImageView.fitHeightProperty().bind(queryDocumentImageViewPane.heightProperty());

		queryDocumentImageViewPane.getChildren().add(queryDocumentImageView);
		queryDocumentHBox.getChildren().add(queryDocumentImageViewPane);

		final float[][] histogramHSB = documentBean.getHistrogramHSB();
		queryDocumentHBox.getChildren().add(createHistogram("Hue", histogramHSB[0], 150, 150));
		queryDocumentHBox.getChildren().add(createHistogram("Saturation", histogramHSB[1], 150, 150));
		queryDocumentHBox.getChildren().add(createHistogram("Brightness", histogramHSB[2], 150, 150));
	}

	private BarChart<String, Number> createHistogram(final String name, final float[] values, final int width, final int height) {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

		if (name != null && !name.isEmpty()) {
			barChart.setTitle(name);
		}

		barChart.setBarGap(0);
		barChart.setLegendVisible(false);

		if (width > 0) {
			barChart.setMaxWidth(width);
		}

		if (height > 0) {
			barChart.setMaxHeight(height);
		}

		for (int index = 0; index < values.length; index++) {
			final Series series = new Series();

			final Data data = new Data("", values[index]);
			data.nodeProperty().addListener((i, j, node) -> {
				if (node != null) {
					data.getNode().setStyle("-fx-bar-fill: skyblue;");
				}
			});
			series.getData().add(data);

			barChart.getData().add(series);
		}

		return barChart;
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
