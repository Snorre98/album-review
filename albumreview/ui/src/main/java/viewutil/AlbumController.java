package viewutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domainlogic.Album;
import domainlogic.AlbumList;
import domainlogic.Review;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import statepersistence.LoadFromFile;
import statepersistence.WriteToFile;
import statepersistence.serializer.AlbumReviewModule;
import viewutil.AlbumListController;

/**
 * Controller for the Album.fxml and Album classes.
 */
public class AlbumController implements Initializable {

  private String username;

  private int selectedReview;
  private Album album;

  private AlbumList albumList;

  private static final String saveFile = "IT1901gr2337/AlbumReviewApp/albumreviews.json";
  Path saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);

  public void setSaveFilePath(Path saveFile) {
    this.saveFilePath = saveFile;
  }

  @FXML
  private Button openReview;

  @FXML
  private Button removeReview;

  @FXML
  private Label albumLabel;

  @FXML
  private Label artistLabel;

  @FXML
  private TextField rating;

  @FXML
  private ListView<Review> reviewListView;

  @FXML
  private Label usernameLabel;

  RestModel restModel = new RestModel();


  public void setUsername(String username) {
    //TODO API controll??
    this.username = username;
    usernameLabel.setText(username);
  }

  @FXML
  public void newReview(ActionEvent event) {
    try {
      Review review = new Review(username, Integer.parseInt(rating.getText()));
      restModel.addReview(album, review.getUserName(), review.getRating());
      //updateReviewListView(getReviewList());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public AlbumList albumListObjectMapper(String responseBody) throws JsonProcessingException {
    ObjectMapper ob = new ObjectMapper();
    ob.registerModule(new AlbumReviewModule());
    return ob.readValue(responseBody, AlbumList.class);
  }

  public void updateReviewListView(Album album) throws IOException, InterruptedException {
    ObservableList<Review> observableAlbums = FXCollections.observableArrayList(album.getReviews());
    this.reviewListView.getItems().setAll(observableAlbums);
  }


  /*
  public void getAlbum(Album album) {
    try {
      Album selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
      List<Album> albumList = getAlbumList().getAlbums();
      for (Album album : albumList) {
        if (album.getArtist().equals(selectedAlbum.getArtist()) && album.getName().equals(selectedAlbum.getName())) {
          setSelectedAlbum(album);
        }
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }*/

  /*
  public void updateAlbumListView(AlbumList albumList) throws IOException, InterruptedException {
    ObservableList<Album> observableAlbums = FXCollections.observableArrayList(albumList.getAlbums());
    this.albumListView.getItems().setAll(observableAlbums);
  }
  */

  /*
  public List<Review> getReviewList() throws IOException, InterruptedException {
    String getAlbumListRequest = restModel.getReviewList();
    return albumListObjectMapper(getAlbumListRequest);
  }*/
  /*
  public void updateReviewList(HttpResponse<String> httpResponse) throws JsonProcessingException {

    //TODO: remove httpResponse parameter
    //String albumListResponse = httpResponse.body();

    AlbumList albumList = getAlbumListObject();
    if (listResponse.statusCode() == 200) {
      updateAlbumListView(listResponse);
    } else {
      System.out.println("Failed to fetch updated album list");
    }
  }

    ObjectMapper ob = new ObjectMapper();
    ob.registerModule(new AlbumReviewModule());

    albumList = ob.readValue(albumListResponse, AlbumList.class);

    //ObservableList<Album> observableAlbums = FXCollections.observableArrayList(reviewList.getReviews());

    reviewListView.getItems().setAll(albumList.getAlbum(album).getReviews());
  }*/

  /*
  public void getAlbumList() throws IOException, InterruptedException {
    HttpRequest listRequest = HttpRequest.newBuilder()
        .uri(URI.create(backendBaseUrl + "/api/albumlist/getAlbumList"))
        .GET()
        .build();
    HttpResponse<String> listResponse = httpClient.send(listRequest, HttpResponse.BodyHandlers.ofString());
    if (listResponse.statusCode() == 200) {
      updateAlbumListView(listResponse);
    } else {
      System.out.println("Failed to fetch updated album list");
    }
  }
  */
  @FXML
  public void removeReview(ActionEvent event) {
    //TODO: API controll
    try {
      albumList.getAlbum(album).removeReview(selectedReview, username);
      reviewListView.getItems().setAll(albumList.getAlbum(album).getReviews());
    } catch (IllegalStateException e) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Warning");
      alert.setContentText("You can not remove a review you did not make");
      alert.showAndWait();
    }
  }

  void handleSave() {
    WriteToFile.writeToFile(albumList, saveFilePath);
  }

  void initReviewListView() throws IOException {
    //TODO: API controll
    albumList = LoadFromFile.loadFromFile(saveFilePath, true);
    reviewListView.getItems().setAll(album.getReviews());
  }

  /**
   * This sets the Album name and Artist at the top of the scene.
   * 
   * @param album The album, to set in controller.
   */
  public void setAlbum(Album album) {

    //TODO: remove??
    this.album = album;
    artistLabel.setText(album.getArtist());
    albumLabel.setText(album.getName());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    reviewListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        //TODO: do this with API
        selectedReview = reviewListView.getSelectionModel().getSelectedIndex();
      }
    });
  }
}
