package statepersistence.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import domainlogic.Album;
import domainlogic.Review;
import java.io.IOException;

/**
 * Deserializes java.
 */
public class AlbumDeserializer extends JsonDeserializer<Album> {

  private ReviewDeserializer reviewDeserializer = new ReviewDeserializer();

  @Override
  public Review deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JacksonException {
    JsonNode jsonNode = parser.getCodec().readTree(parser);
    if (jsonNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) jsonNode;

      JsonNode nameNode = objectNode.get("name");
      String name = null;
      if (nameNode instanceof TextNode) {
        name = ((TextNode) nameNode).asText();
      }

      JsonNode artistNode = objectNode.get("artist");
      String artist = null;
      if (artistNode instanceof TextNode) {
        artist = ((TextNode) artistNode).asText();
      }

      Album album = new Album(artist, name);
      JsonNode reviewsNode = objectNode.get("reviews");
      if (reviewsNode instanceof ArrayNode) {
        for (JsonNode elementNode : ((ArrayNode) reviewsNode)) {
          Review review = ReviewDeserializer.deserialize(elementNode);
          if (review != null) {
            albums.addAlbumReview(album);
          }
        }
      }
      return album;
    }
    return null;
  }
}
