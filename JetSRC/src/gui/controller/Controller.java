// Controller for main screen.

package gui.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import gui.model.GuiModel;
import gui.model.TableDataModel;
import gui.model.TableDataModel.TableData;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import stores.AAStore;
import stores.AGStore;
import stores.OtherStore;
import stores.Store;
import utils.ApprovedConfigurationsConnection;
import utils.GenericSingletonFactory;
import utils.StdOut;
import utils.StringIsValidInt;

public class Controller implements Initializable, ClipboardOwner {
	
	// CONSTANTS
	String storeInputRegexString = "\\|";
	String displayAreaErrorString = "Problem loading Store to Pylon, contact support";
	String emptyPylonDisplayString = "";
	
	// CLASS FXML REFERENCES
	// See main.fxml for fx:id references
	@FXML
	// Main Label
	public Label descriptionLabel;
	// save and load menu
	public ComboBox<String> savedConfigs;
	public Button saveButton;
	// Wing station selection boxes
	public ComboBox<String> stn1;
	public ComboBox<String> stn2;
	public ComboBox<String> stn3;
	public ComboBox<String> stn4;
	public ComboBox<String> stn5;
	public ComboBox<String> stn6;
	public ComboBox<String> stn7;
	public ComboBox<String> stn8;
	public ComboBox<String> stn9;
	// wing station labels
	public Label labelStn1;
	public Label labelStn2;
	public Label labelStn3;
	public Label labelStn4;
	public Label labelStn5;
	public Label labelStn6;
	public Label labelStn7;
	public Label labelStn8;
	public Label labelStn9;
	// Lower table area
	public TableView<Store> tableArea;
	public TableColumn<Store, String> wpnColumn;
	public TableColumn<Store, String> carriageColumn;
	public TableColumn<Store, String> releaseColumn;
	public TableColumn<Store, String> jettisonColumn;
	public TableColumn<Store, String> seperationColumn;
	// confirm delete checkbox
	public CheckBox confirmDeleteCheckBox;
	// Lower display area
	public TextArea displayArea;
	// Wing stations pane
	public Pane stationsPane;
	// Hash map of ComboBoxs
	private HashMap<Integer, ComboBox<String>> comboBoxes = new HashMap<Integer, ComboBox<String>>();
	// Hash map of labels
	private HashMap<Integer, Label> labelHashMap = new HashMap<Integer, Label>();
	// Model for interaction with data
	private GuiModel dataGuiModel;
	// Observable list for table data
	
	
	// SETUP
	
	// Initialize function to setup data
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Get single data model
		dataGuiModel = GenericSingletonFactory.getInstance(GuiModel.class);
		
		// Set main label to current data set
		descriptionLabel.setText(dataGuiModel.config.aircraft.getName());
		
		// Set load configs data
		ObservableList<String> savedConfigsObservableList = FXCollections.observableArrayList(dataGuiModel.config.getAllSavedAircraft());
		savedConfigs.getItems().addAll(savedConfigsObservableList);
		
		// Get reference to station pane
		List<Node> stations = paneNodes(stationsPane);
		
		// stream comboBoxes from UI into comboBoxes collection
		stations.stream()
			.filter(ComboBox.class::isInstance)
			.map(ComboBox.class::cast)
			.forEach(entry -> comboBoxes.put(Integer.valueOf(entry.getId().substring(3,4)), entry));
		
		// stream labels from UI into labels collection
		stations.stream()
			.filter(Label.class::isInstance)
			.map(Label.class::cast)
			.filter(entry -> StringIsValidInt.isValid(entry.getId().substring(8, 9)))
			.forEach(entry -> labelHashMap.put(Integer.valueOf(entry.getId().substring(8, 9)), entry));
		
		// Iterate over combo box to add approved configs
		updateAircraftDropDowns();
		setupTableColumns();
	}
	
	// METHODS
	
	// ACTION EVENTS INITIALIZED BY USER INPUTS
	
	// Action event for when pylon item selected	
	public void pylonChanged(ActionEvent event) {
		// event get source type safe due to specific action on ComboBox
		@SuppressWarnings("unchecked")
		ComboBox<String> box = (ComboBox<String>)event.getSource();
		
		// we need the is and string fro m the combobox
		if(box.getValue() != null) {
			String boxId = box.getId();
			String storeInput = box.getValue();
			
			// we try and add the data to the aircraft
			boolean added = dataGuiModel.addStoreToPylon(boxId,storeInput);
			
			// if it worked we refresh the view
			if (added) {
				updateTextArea(dataGuiModel.config.aircraft.toString());
				updateUI();
			} else {
				updateTextArea(displayAreaErrorString);
			}
		}
		filterAircraftPylons();
		
	}
	
	// Action event for when selection made in saved configurations drop down.
	@SuppressWarnings("unchecked")
	public void loadConfig(ActionEvent event) {
		ComboBox<String> comboBox = (ComboBox<String>)event.getSource();
		String aircraftString = comboBox.getValue();
		StdOut.println(aircraftString);
		dataGuiModel.getSavedAircraft(aircraftString);
		updateUI();
		updateTextArea(dataGuiModel.config.aircraft.toString());
	}
	
	// Action event for when save config button is clicked
	public void saveConfig(ActionEvent event) {
		dataGuiModel.saveCurrentAircraft();
		ObservableList<String> savedConfigsObservableList = FXCollections.observableArrayList(dataGuiModel.config.getAllSavedAircraft());
		savedConfigs.getItems().clear();
		savedConfigs.getItems().addAll(savedConfigsObservableList);
	}
	
	// Action event when store label is clicked to remove store
	public void removeStore(MouseEvent event) {
		
		// we require label Id from event
		Label label = (Label)event.getSource();
		String labelId = label.getId();
		String pylonString = labelId.substring(8);
		
		// now we remove the store
		
		// confirm with user if CONFIRM ON DELETE option selected
		if (confirmDeleteCheckBox.isSelected()) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete store from Station " + pylonString + "?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				if (StringIsValidInt.isValid(pylonString)) {
					dataGuiModel.clearStoreFromPylon(pylonString);
					Integer pylonInteger = Integer.valueOf(pylonString);
					clearPylonDropdown(pylonInteger);
					updateTextArea(dataGuiModel.config.aircraft.toString());
					filterAircraftPylons();
					updateUI();
				}
			}
		} else {
			if (StringIsValidInt.isValid(pylonString)) {
				dataGuiModel.clearStoreFromPylon(pylonString);
				Integer pylonInteger = Integer.valueOf(pylonString);
				clearPylonDropdown(pylonInteger);
				updateTextArea(dataGuiModel.config.aircraft.toString());
				filterAircraftPylons();
				updateUI();
			}
		}
	}
	
	// HEADER MENU EVENTS
	
	// debug view for database
	public void viewDatabase(ActionEvent e) {
		Stage tableStage = new Stage();
		Stage stage = (Stage) displayArea.getScene().getWindow();
		tableStage.initModality(Modality.APPLICATION_MODAL);
        tableStage.initOwner(stage);
		TableView<Map<String, String>> table = new TableView<>();
		ApprovedConfigurationsConnection connection = GenericSingletonFactory.getInstance(ApprovedConfigurationsConnection.class);
		TableDataModel tdm = new TableDataModel();
		
		Connection conn = connection.connection;
		String dbTableName = "configs";

		Task<TableData> loadDataTask = new Task<TableData>() {
		    @Override
		    public TableData call() throws Exception {
		        return tdm.readData(conn, dbTableName);
		    }
		};
		loadDataTask.setOnSucceeded(event -> {
		    table.getColumns().clear();

		    TableData tableData = loadDataTask.getValue();
		    for (String columnName : tableData.getColumnNames()) {
		        TableColumn<Map<String, String>, String> col = new TableColumn<>(columnName);
		        col.setCellValueFactory(cellData -> 
		            new ReadOnlyStringWrapper(cellData.getValue().get(columnName)));
		        table.getColumns().add(col);
		    }
		    table.getItems().setAll(tableData.getData());
		});

		loadDataTask.setOnFailed(event -> loadDataTask.getException().printStackTrace());

		Thread loadThread = new Thread(loadDataTask);
		loadThread.setDaemon(true);
		loadThread.start();
		Scene tableScene = new Scene(table, 600, 300);
		tableStage.setScene(tableScene);
        tableStage.show();
	}
	
	public void copyCRJ() {
		StringBuilder clipboardString = new StringBuilder();
		ObservableList<TableColumn<Store, ?>> columns = tableArea.getColumns();

		columns.forEach(c->{
			clipboardString.append(c.getText());
			clipboardString.append('\t');
		});
		clipboardString.append('\n');
        for (Store store : tableArea.getItems()) {
        	// Name
        	clipboardString.append(store.getName());
            clipboardString.append('\t');
            // CAR
            clipboardString.append(store.getCarriageLimit());
            clipboardString.append('\t');
            // REL
            if (store instanceof AGStore) {
            	clipboardString.append(((AGStore)store).getReleaseLimitDouble().toString());
            	clipboardString.append('\t');
            } else {
            	clipboardString.append("NA");
            	clipboardString.append('\t');
            }
            //JET
            if (store instanceof OtherStore)
            	clipboardString.append("NA");
        		clipboardString.append('\t');
        	if (store instanceof AAStore)
        		clipboardString.append(((AAStore)store).getJettisonLimitDouble().toString());
        		clipboardString.append('\t');
		    if (store instanceof AGStore)
		    	clipboardString.append(((AGStore)store).getJettisonLimitDouble().toString());
		    	clipboardString.append('\t');
	     	// SEP
		    if (store instanceof AGStore) {
		    	clipboardString.append(((AGStore)store).getSeperationString());
		    	clipboardString.append('\t');
		    } else {
		    	clipboardString.append("NA");
        		clipboardString.append('\t');
		    }
		    	
		     	
            clipboardString.append('\n');

        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());
        // StringSelection stringSelection = new StringSelection(clipboardString.toString());
        StringSelection stringSelection = new StringSelection(clipboardString.toString());
        // set clipboard content
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
        //StdOut.println(clipboardString.toString());
	}
	 public void print() {
		 
	 }
	 public void copyConfig() {
		 
	 }
	 
	 // Import external files
	 public void importConfig() {
		//Handle open button action.
		 FileChooser fileChooser = new FileChooser();
		 Stage stage = (Stage) displayArea.getScene().getWindow();
		 File selectedFile = fileChooser.showOpenDialog(stage);
		 String fileName = selectedFile.getName();
		 String fullPath = dataGuiModel.config.AIRCRAFT_SAVE_PATH.concat(fileName);
		 File file = new File(fullPath);
		 try {
			FileUtils.copyFile(selectedFile, file);
			StdOut.print("file saved");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 public void exportConfig() {
		 
	 }
	 public void quit() {
		 
	 }
	 public void about() {
		 
	 }
	
	// DATABASE INTERACTIONS 
	
	// get filtered drop downs, needed for when user selects a store 
	// and there are restrictions to the other available store.
	private void filterAircraftPylons() {
		
		if 	(dataGuiModel.config.aircraft.isClean()) {
			dataGuiModel.buildPylonMap();
			updateAircraftDropDowns();
		} else {
			// We need a HashMap containing just the changed pylons and their values
			HashMap<String, String> currentStores = dataGuiModel.config.aircraft.getStoresHashMap();
			
			// We send this to the datamodel to filter the database
			dataGuiModel.filterAircraftDropDowns(currentStores);
			
			updateAircraftDropDowns();
		}
	}
	
	// HELPER METHODS
	// Get pane nodes (we need this to access all items in a specific pane)
	public static <T extends Pane> List<Node> paneNodes(T parent) {
        return paneNodes(parent, new ArrayList<Node>());
    }

    private static <T extends Pane> List<Node> paneNodes(T parent, List<Node> nodes) {
        for (Node node : parent.getChildren()) {
            if (node instanceof Pane) {
                paneNodes((Pane) node, nodes);
            } else {
                nodes.add(node);
            }
        }

        return nodes;
    }
    
    // things have changed so we need to update the UI
    private void updateUI() {
    	updateAircraftLabels();
    	updateTableArea();
    }
    
    // or we need to specifially update the text area
    private void updateTextArea(String string) {
    	displayArea.setText(string); 
    }
    
    // we want the pylon drop downs to not remained selected
    private void clearPylonDropdown(Integer pylon) {
    	comboBoxes.get(pylon).setValue(null);
    }
    
    // the labels below the dropdowns reflect what is actually selected
    private void updateAircraftLabels() {
    	labelHashMap.forEach((k,v) -> {
    		if (!dataGuiModel.config.aircraft.getPylons().get(k).getStores().isEmpty()) {
    			v.setText(dataGuiModel.config.aircraft.getPylons().get(k).getStores().get(0).getName());
    			Tooltip tooltip = new Tooltip("Click to remove");
    			v.setTooltip(tooltip);
			} else {
				v.setText(emptyPylonDisplayString);
			}
    		
    	});
    };
    
    // the dropdowns should reflect what is available to the user
    private void updateAircraftDropDowns() {
    	Platform.runLater(() -> {
            try {
            	for (Map.Entry<Integer, ComboBox<String>> value : comboBoxes.entrySet()) {
    				HashMap<Integer, String> map = dataGuiModel.pylonsHashMap.get(value.getKey());
    				
    				// Create observable list of items for each station
    				ObservableList<String> list = FXCollections.observableArrayList();
    				list.addAll(map.values());
    				value.getValue().setOnAction(null);
    				value.getValue().getItems().clear();
    				value.getValue().getItems().addAll(list);
    				value.getValue().setConverter(new StringConverter<String>() {

    					@Override
    					public String toString(String object) {
    						if (object != null)
    							object = object.split(storeInputRegexString)[1];
    						return object;
    					}

    					@Override
    					public String fromString(String string) {
    						// TODO Auto-generated method stub
    						return string;
    					}
    					
    				});
    				value.getValue().setOnAction((event) -> pylonChanged(event));
    			}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    // TABLE VIEW HELPERS
    
    private void setupTableColumns() {
    	wpnColumn.setCellValueFactory(new Callback<CellDataFeatures<Store, String>, ObservableValue<String>>() {
    	     public ObservableValue<String> call(CellDataFeatures<Store, String> p) {
    	         return new ReadOnlyObjectWrapper<String>(p.getValue().getName());
    	     }
    	});
    	carriageColumn.setCellValueFactory(new Callback<CellDataFeatures<Store, String>, ObservableValue<String>>() {
			 public ObservableValue<String> call(CellDataFeatures<Store, String> p) {
			     return new ReadOnlyObjectWrapper<String>(p.getValue().getCarriageLimit().toString());
			 }
   	  	});
    	releaseColumn.setCellValueFactory(new Callback<CellDataFeatures<Store, String>, ObservableValue<String>>() {
			 public ObservableValue<String> call(CellDataFeatures<Store, String> p) {
			     if (p.getValue() instanceof AGStore)
			    	 return new ReadOnlyObjectWrapper<String>(((AGStore)p.getValue()).getReleaseLimitDouble().toString());
			     return new ReadOnlyObjectWrapper<String>("");
			 }
   	  	});
    	jettisonColumn.setCellValueFactory(new Callback<CellDataFeatures<Store, String>, ObservableValue<String>>() {
			 public ObservableValue<String> call(CellDataFeatures<Store, String> p) {
			     if (p.getValue() instanceof OtherStore)
			    	 return new ReadOnlyObjectWrapper<String>("");
			     if (p.getValue() instanceof AAStore)
			    	 return new ReadOnlyObjectWrapper<String>(((AAStore)p.getValue()).getJettisonLimitDouble().toString());
			     if (p.getValue() instanceof AGStore)
			    	 return new ReadOnlyObjectWrapper<String>(((AGStore)p.getValue()).getJettisonLimitDouble().toString());
			     return new ReadOnlyObjectWrapper<String>("");		
			 }
   	  	});
    	seperationColumn.setCellValueFactory(new Callback<CellDataFeatures<Store, String>, ObservableValue<String>>() {
			 public ObservableValue<String> call(CellDataFeatures<Store, String> p) {
			     if (p.getValue() instanceof AGStore)
			    	 return new ReadOnlyObjectWrapper<String>(((AGStore)p.getValue()).getSeperationString());
			     return new ReadOnlyObjectWrapper<String>("");		
			 }
   	  	});
    }
    
    private void updateTableArea() {
    	if 	(dataGuiModel.config.aircraft.isClean()) {
			tableArea.getItems().clear();
		} else {
			tableArea.getItems().clear();
			dataGuiModel.config.aircraft.getPylons().forEach((i,p) -> {
				if (!p.isEmpty())
					if (!tableArea.getItems().contains(p.getStores().get(0))) {
						tableArea.getItems().add(p.getStores().get(0));
				}
			});
		}
    }

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// do nothing
		
	}
}
