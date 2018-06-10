
package me.bloodybadboy.popularmovies.data.model;

import com.squareup.moshi.Json;

@SuppressWarnings("unused") public class Review {

  @Json(name = "author") private String mAuthor;
  @Json(name = "content") private String mContent;
  @Json(name = "id") private String mId;
  @Json(name = "url") private String mUrl;

  public String getAuthor() {
    return mAuthor;
  }

  public void setAuthor(String author) {
    mAuthor = author;
  }

  public String getContent() {
    return mContent;
  }

  public void setContent(String content) {
    mContent = content;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    mUrl = url;
  }
}
