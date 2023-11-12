package viewutil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Login Page Controller Test Class.
 */
public class LoginPageControllerTest extends ApplicationTest {
  private LoginPageController lpc;
  private Path saveFilePath;

  /**
   * Before Each.
   */
  @BeforeEach
  public void setUp() throws URISyntaxException, IOException {
    URL resourceUrl = getClass().getClassLoader().getResource("testFile.json");
    System.out.println(resourceUrl);
    if (resourceUrl == null) {
      throw new IllegalStateException(
          "Cannot find mock JSON file. A fix might be to run 'mvn clean compile'");
    }
    saveFilePath = Paths.get(resourceUrl.toURI());
    lpc.setSaveFilePath(saveFilePath);
    // pageHandler = new PageHandler();
  }

  @Override
  public void start(Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage_test.fxml"));
    final Parent root = loader.load();
    lpc = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  public void testUserLogin() {
    clickOn("#username").write("TestUser");
    clickOn("#button");
  }

}
