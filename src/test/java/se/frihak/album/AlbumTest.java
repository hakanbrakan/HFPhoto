package se.frihak.album;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.frihak.picture.PicturedateHelper;


public class AlbumTest {

	private Album album;

	@Before
	public void setUp() throws Exception {
		album = new Album(null);
	}

	@Test
	public void treSpalterFjortonIndex() {
		List<String> allaIndex = Arrays.asList("Adam", "Bertil", "Ceasar", "David", "Erik", "Filip", "Gustav", "Helge", "Ivar", "Johan", "Kalle", "Ludvig", "Martin", "Niklas");
		assertEquals("[[Adam, Bertil, Ceasar, David, Erik], [Filip, Gustav, Helge, Ivar, Johan], [Kalle, Ludvig, Martin, Niklas]]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void treSpalterFemtonIndex() {
		List<String> allaIndex = Arrays.asList("Adam", "Bertil", "Ceasar", "David", "Erik", "Filip", "Gustav", "Helge", "Ivar", "Johan", "Kalle", "Ludvig", "Martin", "Niklas", "Olof");
		assertEquals("[[Adam, Bertil, Ceasar, David, Erik, Filip], [Gustav, Helge, Ivar, Johan, Kalle, Ludvig], [Martin, Niklas, Olof]]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void treSpalterSextonIndex() {
		List<String> allaIndex = Arrays.asList("Adam", "Bertil", "Ceasar", "David", "Erik", "Filip", "Gustav", "Helge", "Ivar", "Johan", "Kalle", "Ludvig", "Martin", "Niklas", "Olof", "Petter");
		assertEquals("[[Adam, Bertil, Ceasar, David, Erik, Filip], [Gustav, Helge, Ivar, Johan, Kalle, Ludvig], [Martin, Niklas, Olof, Petter]]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void treSpalterNollIndex() {
		List<String> allaIndex = new ArrayList<>();
		assertEquals("[[], [], []]", album.skapaSpalter(allaIndex, 3).toString());
	}
}
