package seedu.goat.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * One line of the conversation: a message beside the speaker's label.
 * <p>
 * Both speakers share this class and differ only in which side they sit on, so the two
 * halves of the transcript can never drift apart in wording or spacing.
 */
public class DialogBox extends HBox {

    /** The message text. */
    @FXML
    private Label dialog;

    /** The speaker's label. */
    @FXML
    private Label speaker;

    /**
     * Creates a box showing a message beside a name, laid out for the user's side.
     *
     * @param message the text to show.
     * @param speakerName the name displayed beside the message.
     */
    private DialogBox(String message, String speakerName) {
        try {
            // This class is both the root and the controller of its own FXML, which is
            // what lets a DialogBox be constructed and added like any other node.
            FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load the packaged dialog layout", e);
        }

        dialog.setText(message);
        speaker.setText(speakerName);
    }

    /**
     * Returns a box showing one of the user's messages, name on the right.
     *
     * @param message the text to show.
     * @param speakerName the user label.
     * @return the box to add to the transcript.
     */
    public static DialogBox getUserDialog(String message, String speakerName) {
        return new DialogBox(message, speakerName);
    }

    /**
     * Returns a box showing one of GOAT's replies, name on the left.
     *
     * @param message the text to show.
     * @param speakerName the chatbot label.
     * @return the box to add to the transcript.
     */
    public static DialogBox getGoatDialog(String message, String speakerName) {
        DialogBox box = new DialogBox(message, speakerName);
        box.flip();
        return box;
    }

    /**
     * Moves the name to the left, so GOAT's replies sit on the opposite side of the
     * window from the user's messages.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(children);
        this.getChildren().setAll(children);
        this.setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }
}
