package album.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Photo {
	private int albumId;
	private int photoId;
	private String title;
	private String url;
	private LocalDateTime date;
	public Photo(int albumId, int photoId, String title, String url, LocalDateTime date) {
		super();
		this.albumId = albumId;
		this.photoId = photoId;
		this.title = title;
		this.url = url;
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getAlbumId() {
		return albumId;
	}
	public int getPhotoId() {
		return photoId;
	}
	public LocalDateTime getDate() {
		return date;
	}
	@Override
	public String toString() {
		return "Photo [albumId=" + albumId + ", photoId=" + photoId + ", title=" + title + ", url=" + url + ", date="
				+ date + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(albumId, photoId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		return albumId == other.albumId && photoId == other.photoId;
	}
	
	
}
