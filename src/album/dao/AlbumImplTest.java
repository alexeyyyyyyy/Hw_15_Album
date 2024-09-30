package album.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import album.model.Photo;

class AlbumImplTest {
	Album album;
	Photo[] photos = new Photo[6];
	LocalDateTime now = LocalDateTime.now();
	Comparator<Photo> comparator = (p1, p2) -> {
		int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
		return res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
		
	};

	
	@BeforeEach
	void setUp() throws Exception {
		album = new AlbumImpl();
		photos[0] = new Photo(1, 1, "title1", "url1", now.minusDays(7));
		photos[1] = new Photo(2, 2, "title2", "url2", now.minusDays(6));
		photos[2] = new Photo(2, 3, "title3", "url3", now.minusDays(5));
		photos[3] = new Photo(4, 4, "title4", "url4", now.minusDays(4));
		photos[4] = new Photo(5, 5, "title5", "url5", now.minusDays(3));
		photos[5] = new Photo(6, 6, "title6", "url6", now.minusDays(2));
		for (int i = 0; i < photos.length-1; i++) {
			album.addPhoto(photos[i]);
		}
	}

	@Test
	void testAddPhoto() {
		assertFalse(album.addPhoto(null));
		assertFalse(album.addPhoto(photos[1]));
		assertEquals(5, album.size());
		assertTrue(album.addPhoto(photos[5]));
		assertEquals(6, album.size());
	
		
		
		
	}

	@Test
	void testRemovePhoto() {
		assertFalse(album.removePhoto(12, 12));
		assertEquals(5, album.size());
		assertTrue(album.removePhoto(1, 1));
		assertEquals(4, album.size());
	}

	@Test
	void testUpdatePhoto() {
		assertFalse(album.updatePhoto(15, 12, null));
		assertTrue(album.updatePhoto(1, 1, "url25"));
		 assertEquals("url25", photos[0].getUrl());
	}

	@Test
	void testGetPhotoFromAlbum() {
		assertEquals(photos[0], album.getPhotoFromAlbum(1, 1));
		assertNotNull(album.getPhotoFromAlbum(2, 2));
		
	}

	@Test
	void testGetAllPhotoFromAlbum() {
		Photo[] actual = album.getAllPhotoFromAlbum(2);
		Arrays.sort(actual, comparator);
		Photo[] expected = {photos[1], photos[2]};
		assertArrayEquals(expected, actual);
	}

	@Test
	void testGetPhotoBetweenDate() {
		LocalDate localDate = LocalDate.now();
		Photo[] actual = album.getPhotoBetweenDate(localDate.minusDays(5), localDate.minusDays(2));
		Arrays.sort(actual,comparator);
		Photo[]expected = {photos[2],photos[3],photos[4]};
		assertArrayEquals(expected, actual);
	}

	@Test
	void testSize() {
		assertEquals(5,album.size());
	}

}
