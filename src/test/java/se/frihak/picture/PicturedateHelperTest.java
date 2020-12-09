package se.frihak.picture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Before;
import org.junit.Test;


public class PicturedateHelperTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void bildPng() {
		assertEquals("2020-10-29", PicturedateHelper.guessDateFromFilename("2020-10-29 17.07.01.png"));
	}

	@Test
	public void filmMp4() {
		assertEquals("2020-10-30", PicturedateHelper.guessDateFromFilename("2020-10-30 15.55.27.mp4"));
	}

	@Test
	public void bildJpg() {
		assertEquals("2020-10-30", PicturedateHelper.guessDateFromFilename("2020-10-30 18.04.44.jpg"));
	}

	@Test
	public void bildJpg_2() {
		assertEquals("2020-10-30", PicturedateHelper.guessDateFromFilename("2020-10-30 18.04.44-2.jpg"));
	}

	@Test
	public void filmMov() {
		assertEquals("2020-10-31", PicturedateHelper.guessDateFromFilename("2020-10-31 11.11.21.mov"));
	}

	@Test
	public void guessInvalidDate() {
		assertEquals("<datum saknas>", PicturedateHelper.guessDateFromFilename("kallekula.mov"));
	}

	@Test
	public void invalidDate() {
		assertTrue(PicturedateHelper.isInvalidDate("kallekula.mov"));
	}

	@Test
	public void validDate() {
		assertFalse(PicturedateHelper.isInvalidDate("2020-10-22"));
	}

	@Test
	public void bildJpg_3() {
		assertEquals("2010-07-03", PicturedateHelper.guessDateFromFilename("2010-07-03 11.11.02653.jpg"));
	}

	@Test
	public void bildBmp_1() {
		assertEquals("2010-01-01", PicturedateHelper.guessDateFromFilename("2010-01-01 01.01.01.bmp"));
	}

	@Test
	public void bildJpg_4() {
		assertEquals("2007-11-01", PicturedateHelper.guessDateFromFilename("2007-11-01 0009.JPG"));
	}
	
	@Test
	public void bildHDR() {
		assertEquals("2020-10-30", PicturedateHelper.guessDateFromFilename("2020-10-30 18.04.44 HDR.jpg"));
	}
	
	@Test
	public void bildHDR_2() {
		assertEquals("2020-10-30", PicturedateHelper.guessDateFromFilename("2020-10-30 18.04.44 HDR-2.jpg"));
	}
}
