package restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domainlogic.Album;
import domainlogic.AlbumList;
import statepersistence.AlbumReviewPersistence;

/**
 * Album List Api Controller Test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = {AlbumListApiController.class, AlbumListService.class, AlbumReviewApplication.class})
public class AlbumListApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AlbumListService albumListService;


  /**
   * Set up.
   */
  // @BeforeEach
  // public void setup() {
  // Album testAlbum = new Album("testArtist", "testAlbum");
  // mockMvc.perform(post("/addAlbum/testArtist/testAlbum"))
  // .content(new ObjectMapper().writeValueAsString(testAlbum))
  // .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).andReturn();

  // }

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    // albumListService = new AlbumListService();
    objectMapper = AlbumReviewPersistence.createObjectMapper();
  }

  /**
   * Exception.
   */
 
  public void addTestAlbum() throws Exception {
    Album testAlbum = new Album("TestAlbum", "TestArtist");
    mockMvc
        .perform(post("/api/albumlist/addAlbum").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testAlbum)).accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Test
  public void testGetAlbumList() throws Exception {
    
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/api/albumlist/getAlbumList")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    String response = result.getResponse().getContentAsString();
    AlbumList albumList = objectMapper.readValue(response, AlbumList.class);

    assertNotNull(albumList);
  }

  @Test
  public void testGetAlbum() throws Exception {
    addTestAlbum();
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/albumlist/getAlbum"))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    String responseBody = result.getResponse().getContentAsString();
    Album returnedAlbum = objectMapper.readValue(responseBody, Album.class);

    assertEquals(returnedAlbum.getArtist(), "TestArtist");
    assertEquals(returnedAlbum.getName(), "TestAlbum");
  }

  @Test
  public void testAddAlbum() throws Exception {
    Album testAlbum = new Album("Radiohead", "In Rainbows");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/albumlist/addAlbum")
            .content(objectMapper.writeValueAsString(testAlbum)).accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void testSortAlbumsByName() throws Exception {
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/api/albumlist/sortAlbumsByName")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
    String response = result.getResponse().getContentAsString();
    List<Album> sortedAlbums =
        objectMapper.readValue(response, new TypeReference<List<Album>>() {});

    assertEquals(sortedAlbums, result);
  }

  @Test
  public void testSortAlbumsByArtist() throws Exception {
    MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/api/albumlist/sortAlbumsByArtist")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
    String response = result.getResponse().getContentAsString();
    List<Album> sortedAlbumsByArtist =
        objectMapper.readValue(response, new TypeReference<List<Album>>() {});

    assertEquals(sortedAlbumsByArtist, result);
  }
}
