package restserver;

import domainlogic.Album;
import domainlogic.AlbumList;
import org.springframework.stereotype.Service;
import statepersistence.LoadFromFile;
import statepersistence.WriteToFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * AlbumList API service.
 * */
@Service
public class AlbumListService {

  private static final String saveFile = "IT1901gr2337/AlbumReviewApp/albumreviews.json";
  Path saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);

  /**
   * empty constructor. Used for initialization.
   * */
  public AlbumListService() {}

  public AlbumList loadAlbumList() throws IOException {
    return LoadFromFile.loadFromFile(saveFilePath, true);
  }

  /**
   * Service for adding album.
   * @param artist artist name
   * @param name album name
   * @return HTTP response status
   * */
  public String addAlbum(String artist, String name) {
    try {
      AlbumList albumList = loadAlbumList();
      Album album = new Album(artist, name);
      albumList.addAlbum(album);
      WriteToFile.writeToFile(albumList, saveFilePath);
      return "200_OK";
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not add album");
    }
  }

  /**
   * Service for adding album.
   * @param newAlbum artist name
   * @return HTTP response status
   * */
  public String addAlbumObject(Album newAlbum) {
    try {
      AlbumList albumList = loadAlbumList();
      albumList.addAlbum(newAlbum);
      WriteToFile.writeToFile(albumList, saveFilePath);
      return "200_OK";
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not add album");
    }
  }

  /**
   * Service for removing album at index.
   * @param index album index
   * */
  public String removeAlbum(int index) {
    try {
      AlbumList albumList = loadAlbumList();
      albumList.removeAlbum(index);
      return "200_OK";
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not remove album");
      //return "BAD_REQUEST";
    }
  }

  /**
   * Service for getting AlbumList.
   * */
  public AlbumList getAlbumList() {
    try {
      AlbumList albumList = loadAlbumList();
      return albumList;
      //200_OK
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not get list of albums");
      //BAD_REQUEST
    }
  }

  /**
   * Service for getting album at index.
   * @param index index of album
   * */
  public Album getAlbum(int index) {
    try {
      AlbumList albumList = loadAlbumList();
      return albumList.getAlbumByIndex(index);
      //200_OK
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not get album at {index}");
      //BAD_REQUEST
    }
  }

  public Album getAlbumById(UUID albumId) {
    try {
      AlbumList albumList = loadAlbumList();
      return albumList.getAlbumById(albumId);
      //200_OK
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not get album at {index}");
      //BAD_REQUEST
    }
  }

  /**
   * Service for sorting albums by album name.
   * */
  /*
  public String sortAlbumsByName() {
    try {
      AlbumList albumList = loadAlbumList();
      albumList.sortAlbum();
      return "200_OK";
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not sort album by name");
    }
  }*/
  public AlbumList sortAlbumsByName() {
    try {
      AlbumList albumList = loadAlbumList();
      albumList.sortAlbum();
      return albumList;
    } catch (Exception e) {
      throw new UnsupportedOperationException("BAD_REQUEST");
    }
  }

  /**
   * Service for sorting albums by artist.
   * */
  public AlbumList sortAlbumsByArtist() {
    try {
      AlbumList albumList = loadAlbumList();
      albumList.sortArtist();
      return albumList;
    } catch (Exception e) {
      throw new UnsupportedOperationException(e + "could not sort album by artist");
    }
  }
}
