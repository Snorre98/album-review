package restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domainlogic.Album;
import domainlogic.Review;
import statepersistence.AlbumReviewPersistence;

/**
 * Album Api Controller Test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = {AlbumApiController.class, AlbumService.class, AlbumReviewApplication.class})
public class AlbumApiControllerTest {
  
  @Autowired
  MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    objectMapper = AlbumReviewPersistence.createObjectMapper();
  }

  /**
   * Exception.
   */
  @BeforeEach
  public void addTestReview() throws Exception {
    Review testReview = new Review("TestUser", 5);
    mockMvc
        .perform(post("/api/album/addReview")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testReview))
        .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Test
  public void testGetArtist() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");
    
    mockMvc.perform(MockMvcRequestBuilders.get("/api/album/getArtist/{albumId}", testAlbum)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("TestArtist"));
  }

  @Test
  public void testGetName() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");
  
    mockMvc.perform(MockMvcRequestBuilders.get("/api/album/getName/{albumId}", testAlbum)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("TestAlbumName"));
  }

  @Test
  public void testAddReview() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/album/brendank", 5)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
  
  @Test
  public void testRemoveReview() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/album/deleteReview")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testAlbum))
            .param("index", "1")
            .param("username", "TestUser"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("ReviewRemoved"));
  }

  @Test
  public void testGetReview() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");
    Review testReview = new Review("TestUser", 5);
    testAlbum.addReview(testReview);
    
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/album/getReview")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testAlbum)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    Review returnedReview = objectMapper.readValue(responseBody, Review.class);

    assertEquals(responseBody, returnedReview);
  }

  @Test
  public void testGetReviewList() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");
    Review r1 = new Review("User1", 4);
    testAlbum.addReview(r1);
    
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/album/getReviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testAlbum)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    List<Review> returnedReviewList = objectMapper
        .readValue(responseBody, new TypeReference<List<Review>>() {});

    assertEquals(responseBody, returnedReviewList);
  }



  
}
