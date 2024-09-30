package album.dao;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import album.model.Photo;

public class AlbumImpl implements Album {
	private static final int INITIAL_CAPACITY = 10;
	private Photo[] photos;
	private Photo[] photosSortedByDates;
	private int size;

	public AlbumImpl() {
		photos = new Photo[INITIAL_CAPACITY];
		photosSortedByDates = new Photo[INITIAL_CAPACITY];
	}

	private static Comparator<Photo> comparatorByDate = (p1, p2) -> {
		int res = p1.getDate().compareTo(p2.getDate());
		res = res != 0 ? res : Integer.compare(p1.getAlbumId(), p2.getAlbumId());
		return res = res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
	};
	private static Comparator<Photo> comparator = (p1, p2) -> {
		int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
		res = res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
		return res = res != 0 ? res : p1.getDate().compareTo(p2.getDate());
	};

	@Override
	public boolean addPhoto(Photo photo) {
		if (photo == null || photos.length == size
				|| getPhotoFromAlbum(photo.getAlbumId(), photo.getPhotoId()) != null) {
			return false;
		}
		if (photos.length == size) {
			photos = Arrays.copyOf(photos, photos.length * 2);

		}
		int index = Arrays.binarySearch(photos, 0, size, photo, comparator);
		index = index >= 0 ? index : -index - 1;
		System.arraycopy(photos, index, photos, index + 1, size - index);
		photos[index] = photo;

		index = Arrays.binarySearch(photosSortedByDates, 0, size, photo, comparatorByDate);
		index = index >= 0 ? index : -index - 1;
		System.arraycopy(photosSortedByDates, index, photosSortedByDates, index + 1,size - index);
		photosSortedByDates[index] = photo;

		size++;
		return true;
	}

	@Override
	public boolean removePhoto(int photoId, int albumId) {
		int index = searchById(photoId, albumId);
		if (index < 0) {
			return false;
		}
		System.arraycopy(photos, index + 1, photos, index, size - index - 1);
		photos = Arrays.copyOf(photos, photos.length - 1);
		
		for (int i = 0; i < size; i++) {
			if (photosSortedByDates[i].getAlbumId() == albumId && photosSortedByDates[i].getPhotoId() == photoId) {
				index = i;
			}
		}
		if(index < 0) {
			return false;
		}
		System.arraycopy(photosSortedByDates, index + 1, photosSortedByDates, index, size - index - 1);
		photosSortedByDates = Arrays.copyOf(photosSortedByDates, photosSortedByDates.length - 1);

		size--;
		return true;
	}

	@Override
	public boolean updatePhoto(int photoId, int albumId, String url) {
		Photo photo = getPhotoFromAlbum(albumId, photoId);
		if (photo == null) {
			return false;
		}
		photo.setUrl(url);
		return true;
	}

	@Override
	public Photo getPhotoFromAlbum(int photoId, int albumId) {
		int index = searchById(albumId, photoId);
		if (index < 0) {
			return null;
		}
		return photos[index];
	}

	@Override
	public Photo[] getAllPhotoFromAlbum(int albumId) {

		Predicate<Photo> filterByAlbumId = photo -> photo.getAlbumId() == albumId;

		Photo[] filteredPhotos = findPhotosByPredicate(filterByAlbumId);

		Arrays.sort(filteredPhotos, comparator);

		return filteredPhotos;
	}

	@Override
	public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
		Photo pattern = new Photo(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null, dateFrom.atStartOfDay());
		int from = -Arrays.binarySearch(photosSortedByDates,0,size,pattern,comparatorByDate)-1;
		
		pattern = new Photo(Integer.MAX_VALUE, Integer.MAX_VALUE, null, null, dateTo.atStartOfDay());
		int to = -Arrays.binarySearch(photosSortedByDates,0,size,pattern,comparatorByDate)-1;
		return Arrays.copyOfRange(photosSortedByDates,from,to);
	}

	@Override
	public int size() {

		return size;
	}

	private int searchById(int photoId, int albumId) {
		for (int i = 0; i < size; i++) {
			if (photos[i].getAlbumId() == albumId && photos[i].getPhotoId() == photoId) {
				return i;
			}
		}
		return -1;
	}

	private Photo[] findPhotosByPredicate(Predicate<Photo> predicate) {
		Photo[] res = new Photo[size];
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (predicate.test(photos[i])) {
				res[j++] = photos[i];
			}
		}
		return Arrays.copyOf(res, j);
	}

}
