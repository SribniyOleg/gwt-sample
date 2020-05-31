package com.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gwt.sample.shared.NumberVerifier.isNumeric;
import static com.gwt.sample.shared.NumberVerifier.isValidNumberOfNumbers;

public class Gwt implements EntryPoint {

    private int count;

    public void onModuleLoad() {

        final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        final Label errorLabel = new Label();

        sendButton.addStyleName("sendButton");
        RootPanel.get("nameFieldContainer").add(nameField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);

        // Focus the cursor on the name field when the app loads
        nameField.setFocus(true);
        nameField.selectAll();

        sendButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get("nameFieldContainer").clear();
                RootPanel.get("sendButtonContainer").clear();
                RootPanel.get("errorLabelContainer").clear();
                displayNumbers(nameField.getText(), event);
            }
        });
    }

    private void displayNumbers(String inputText, ClickEvent event) {
        if (!isNumeric(inputText) || !isValidNumberOfNumbers(inputText)) {
            String message = "This Is not valid number of numbers , please type a digit between 0 to 1000";
            showErrorPopup(message, event);
            return;
        }
        Integer numberOfButtons = Integer.valueOf(inputText);
        int numRows = 10;
        int numColumns;

        if ((numberOfButtons % 10) != 0) {
            numColumns = (numberOfButtons / 10) + 1;
        } else {
            numColumns = numberOfButtons / 10;
        }

        List<String> numbers = generateRandomNumbers(numberOfButtons);

        FlexTable flexTable = new FlexTable();
        addDataToTable(numColumns, numRows, numbers, flexTable);

        Button sortButton = new Button("Sort");
        sortButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addDataToTable(numColumns, numRows, numbers, flexTable);
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.Location.reload();
            }
        });

        RootPanel.get("sortButtonContainer").add(sortButton);
        RootPanel.get("resetButtonContainer").add(resetButton);
    }

    private List<String> generateRandomNumbers(int numberOfButtons) {
        int half = numberOfButtons / 2;
        List<String> numbers = Stream.generate(() -> getRandomIntToString(1, 1000))
                .limit(half)
                .collect(Collectors.toList());

        List<String> lessThenRandom = Stream.generate(() -> getRandomIntToString(1, 30))
                .limit(numberOfButtons - half)
                .collect(Collectors.toList());

        numbers.addAll(lessThenRandom);
        Collections.shuffle(numbers);
        return numbers;
    }

    private void addDataToTable(int numCols, int numRows, List<String> data, FlexTable table) {
        if (count != 0) {
            RootPanel.get("tableContainer").clear();
            data = sortList(data);
        }
        int numberOfButtons = data.size();
        int countOfButtons = 0;
        // Add buttons in flextable
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                if (countOfButtons == numberOfButtons) {
                    break;
                }
                addButton(col, row, table, data.get(countOfButtons));
                countOfButtons++;
            }
        }
        RootPanel.get("tableContainer").add(table);
        count++;
    }

    private List<String> sortList(List<String> data) {
        if ((count % 2) != 0) {
            return data.stream()
                    .map(Integer::valueOf)
                    .sorted(Collections.reverseOrder())
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return data.stream()
                .map(Integer::valueOf)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.toList());

    }

    private void addButton(int col, int row, FlexTable flexTable, String value) {
        Button button = new Button(value);
        button.addStyleName("myButton");
        flexTable.setWidget(row, col, button);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String number = escapeHtml(button.getHTML());
                if (Integer.valueOf(number) <= 30) {
                    count = 0;
                    RootPanel.get("sortButtonContainer").clear();
                    RootPanel.get("resetButtonContainer").clear();
                    RootPanel.get("tableContainer").clear();
                    displayNumbers(getRandomIntToString(1, 1000), event);
                } else {
                    String message = "Please select a value smaller or equal to 30.";
                    showErrorPopup(message, event);
                }
            }
        });
    }

    private void showErrorPopup(String excMessage, ClickEvent event) {
        final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
        simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
        simplePopup.setWidth("300px");
        simplePopup.setWidget(new HTML(excMessage));
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft() + 10;
        int top = source.getAbsoluteTop() + 10;
        simplePopup.setPopupPosition(left, top);
        simplePopup.show();
    }

    //tried to use ThreadLocalRandom to generate random int but it every time was exception "you forget inherit LocalThreadRandom"
    private String getRandomIntToString(int from, int to) {
        return String.valueOf(Random.nextInt(to - from + 1) + from);
    }

    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

}
