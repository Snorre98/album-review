package domainlogic;

/**
 * Core AlbumReview object class. Manages a list of Review objects.
 */
public class Review {

  private String user;
  private int rating;

  /**
   * Constructor for Review class.
   * 
   * @param user user connected to Review object.
   * @param rating connected to Review object.
   */
  public Review(String user, int rating) {
    this.user = user;
    this.rating = rating;
  }

  public String getUserName() {
    return this.user;
  }

  public int getRating() {
    return this.rating;
  }

  @Override
  public String toString() {
    return "Username: " + user + " Rating: " + rating;
  }

}
