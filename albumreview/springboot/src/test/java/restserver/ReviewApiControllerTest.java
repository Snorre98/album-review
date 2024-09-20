package restserver;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import statepersistence.AlbumReviewPersistence;

/**
 * Album Api Controller Test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = {ReviewApiControllerTest.class, ReviewService.class, AlbumReviewApplication.class})
public class ReviewApiControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    // albumListService = new AlbumListService();
    objectMapper = AlbumReviewPersistence.createObjectMapper();
  }
}
