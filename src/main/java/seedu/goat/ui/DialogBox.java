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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * One line of the conversation: a message beside the speaker's avatar.
 * <p>
 * Both speakers share this class and differ only in which side they sit on, so the two
 * halves of the transcript can never drift apart in wording or spacing.
 */
public class DialogBox extends HBox {

    /** The message text. */
    @FXML
    private Label dialog;

    /** The speaker's avatar. */
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a box showing a message beside an avatar, laid out for the user's side.
     *
     * @param message the text to show
     * @param avatar the speaker's picture
     */
    private DialogBox(String message, Image avatar) {
        try {
            // This class is both the root and the controller of its own FXML, which is
            // what lets a DialogBox be constructed and added like any other node.
            FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(message);
        displayPicture.setImage(avatar);

        // Clipping to a circle inscribed in the image turns a square picture round.
        double radius = displayPicture.getFitWidth() / 2;
        displayPicture.setClip(new Circle(radius, radius, radius));
    }

    /**
     * Returns a box showing one of the user's messages, avatar on the right.
     *
     * @param message the text to show
     * @param avatar the user's picture
     * @return the box to add to the transcript
     */
    public static DialogBox getUserDialog(String message, Image avatar) {
        return new DialogBox(message, avatar);
    }

    /**
     * Returns a box showing one of GOAT's replies, avatar on the left.
     *
     * @param message the text to show
     * @param avatar GOAT's picture
     * @return the box to add to the transcript
     */
    public static DialogBox getGoatDialog(String message, Image avatar) {
        DialogBox box = new DialogBox(message, avatar);
        box.flip();
        return box;
    }

    /**
     * Moves the avatar to the left, so GOAT's replies sit on the opposite side of the
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
