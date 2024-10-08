package domainlogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Testing AlbumList and sorting classes.
 */
public class AlbumListTest {
  private AlbumList albumList;

  @BeforeEach
  public void setUp() {
    albumList = new AlbumList();
  }

  @Test
  public void testAddAlbum() {
    Album album = new Album("testArtist", "testName");
    albumList.addAlbum(album);
    assertEquals(1, albumList.getAlbums().size());
    assertEquals("testArtist", albumList.getAlbumByIndex(0).getArtist());
    assertEquals("testName", albumList.getAlbumByIndex(0).getName());
    }

  @Test
  public void testAddSameAlbum() {
    Album album = new Album("testArtist", "testName");
    Album album2 = new Album("testArtist", "testName");
    albumList.addAlbum(album);
    assertThrows(IllegalStateException.class, () -> albumList.addAlbum(album2));
  }

  @Test
  public void testRemoveAlbum() {
    Album album1 = new Album("artist1", "name1");
    Album album2 = new Album("artist2", "name2");
    albumList.addAlbum(album1);
    albumList.addAlbum(album2);

    albumList.removeAlbum(0);
    assertEquals(1, albumList.getAlbums().size());
    assertEquals("artist2", albumList.getAlbumByIndex(0).getArtist());
    assertEquals("name2", albumList.getAlbumByIndex(0).getName());
  }

  @Test
  public void testSortByName() {
    Album a1 = new Album("C", "C");
    Album a2 = new Album("A", "A");
    Album a3 = new Album("B", "B");

    albumList.addAlbum(a1);
    albumList.addAlbum(a2);
    albumList.addAlbum(a3);

    albumList.sortAlbum();

    assertEquals("A", albumList.getAlbumByIndex(0).getName());
    assertEquals("B", albumList.getAlbumByIndex(1).getName());
    assertEquals("C", albumList.getAlbumByIndex(2).getName());
  }

  @Test
  public void testSortByRating() {
    Album a1 = new Album("C", "C");
    Album a2 = new Album("A", "A");
    Album a3 = new Album("B", "B");

    albumList.addAlbum(a1);
    albumList.addAlbum(a2);
    albumList.addAlbum(a3);

    albumList.sortArtist();

    assertEquals("A", albumList.getAlbumByIndex(0).getArtist());
    assertEquals("B", albumList.getAlbumByIndex(1).getArtist());
    assertEquals("C", albumList.getAlbumByIndex(2).getArtist());
  }
}
