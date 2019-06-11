// Controller for main screen.

package gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;


import gui.model.GuiModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import utils.GenericSingletonFactory;
import utils.StdOut;

public class Controller implements Initializable {
	
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
	// Lower display area
	public TextArea displayArea;
	// Wing stations pane
	public Pane stationsPane;
	// Hash map of ComboBoxs
	private HashMap<Integer, ComboBox<String>> comboBoxes = new HashMap<Integer, ComboBox<String>>();
	// Model for interaction with data
	private GuiModel dataGuiModel;

	// Initialize function to setup data
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
			.forEach(entry -> comboBoxes.put(Integer.valueOf(entry.getId().substring(3,4)),entry));
		
		// Iterate over combo box to add approved configs
		for (Map.Entry<Integer, ComboBox<String>> value : comboBoxes.entrySet()) {
			HashMap<Integer, String> map = dataGuiModel.pylonsHashMap.get(value.getKey());
			
			// Create observable list of items for each station
			ObservableList<String> list = FXCollections.observableArrayList();
			list.addAll(map.values());
			value.getValue().getItems().addAll(list);
			value.getValue().setConverter(new StringConverter<String>() {

				@Override
				public String toString(String object) {
					if (object != null)
						object = object.split("\\|")[1];
					return object;
				}

				@Override
				public String fromString(String string) {
					// TODO Auto-generated method stub
					return string;
				}
				
			});
		}
	}
	
	// METHODS
	// Action event for when pylon item selected	
	public void pylonChanged(ActionEvent event) {
		ComboBox<String> box = (ComboBox<String>)event.getSource();
		//StdOut.println(box.getId() + box.getValue());
		String[] storeArrayStrings = new String[1];
		String boxId = box.getId().substring(3);
		storeArrayStrings[0] = boxId;
		String[] storeInput = box.getValue().split("\\|");
		String[] storeInputComplete = Stream.of(storeArrayStrings, storeInput)
				.flatMap(Stream::of)
                .toArray(String[]::new);
		boolean added = dataGuiModel.config.aircraft.addStoreToAircraft(storeInputComplete);
		if (added) {
			displayArea.setText(dataGuiModel.config.aircraft.toString());
		} else {
			displayArea.setText("Empty");
		}
		
	}
	
	public void loadConfig(ActionEvent event) {
		StdOut.println("loadConfig fired");
		StdOut.println("Event is" + event.toString());
	}
	
	public void saveConfig(ActionEvent event) {
		StdOut.println("saveConfig fired");
		StdOut.println("Event is" + event.toString());
	}
	
	public void removeStore(MouseEvent event) {
		StdOut.println("removeStore fired");
		StdOut.println("Event is" + event.toString());
	}
	
	// HELPER METHODS
	// Get pane nodes
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
}
