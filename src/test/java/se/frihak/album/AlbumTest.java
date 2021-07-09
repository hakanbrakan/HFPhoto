package se.frihak.album;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


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
		assertEquals("[[Adam, Bertil, Ceasar, David, Erik], [Filip, Gustav, Helge, Ivar, Johan], [Kalle, Ludvig, Martin, Niklas, Olof]]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void treSpalterFemtonIndexTake2() {
		List<String> allaIndex = createIndexlist(15);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 3);
		assertEquals(3, spalter.size());
		assertEquals(5, spalter.get(0).size());
		assertEquals(5, spalter.get(1).size());
		assertEquals(5, spalter.get(2).size());
	}

	@Test
	public void treSpalterSextonIndex() {
		List<String> allaIndex = Arrays.asList("Adam", "Bertil", "Ceasar", "David", "Erik", "Filip", "Gustav", "Helge", "Ivar", "Johan", "Kalle", "Ludvig", "Martin", "Niklas", "Olof", "Petter");
		assertEquals("[[Adam, Bertil, Ceasar, David, Erik, Filip], [Gustav, Helge, Ivar, Johan, Kalle], [Ludvig, Martin, Niklas, Olof, Petter]]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void treSpalterNollIndex() {
		List<String> allaIndex = new ArrayList<>();
		assertEquals("[[], [], []]", album.skapaSpalter(allaIndex, 3).toString());
	}

	@Test
	public void femSpalterSexIndex() {
		List<String> allaIndex = createIndexlist(6);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(2, spalter.get(0).size());
		assertEquals(1, spalter.get(1).size());
		assertEquals(1, spalter.get(2).size());
		assertEquals(1, spalter.get(3).size());
		assertEquals(1, spalter.get(4).size());
	}

	@Test
	public void femSpalterAttaIndex() {
		List<String> allaIndex = createIndexlist(8);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(2, spalter.get(0).size());
		assertEquals(2, spalter.get(1).size());
		assertEquals(2, spalter.get(2).size());
		assertEquals(1, spalter.get(3).size());
		assertEquals(1, spalter.get(4).size());
	}

	@Test
	public void femSpalterElvaIndex() {
		List<String> allaIndex = createIndexlist(11);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(3, spalter.get(0).size());
		assertEquals(2, spalter.get(1).size());
		assertEquals(2, spalter.get(2).size());
		assertEquals(2, spalter.get(3).size());
		assertEquals(2, spalter.get(4).size());
	}

	@Test
	public void femSpalterFemtonIndex() {
		List<String> allaIndex = createIndexlist(15);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(3, spalter.get(0).size());
		assertEquals(3, spalter.get(1).size());
		assertEquals(3, spalter.get(2).size());
		assertEquals(3, spalter.get(3).size());
		assertEquals(3, spalter.get(4).size());
	}

	@Test
	public void femSpalterFjortonIndex() {
		List<String> allaIndex = createIndexlist(14);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(3, spalter.get(0).size());
		assertEquals(3, spalter.get(1).size());
		assertEquals(3, spalter.get(2).size());
		assertEquals(3, spalter.get(3).size());
		assertEquals(2, spalter.get(4).size());
	}

	@Test
	public void femSpalterSextonIndex() {
		List<String> allaIndex = createIndexlist(16);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(4, spalter.get(0).size());
		assertEquals(3, spalter.get(1).size());
		assertEquals(3, spalter.get(2).size());
		assertEquals(3, spalter.get(3).size());
		assertEquals(3, spalter.get(4).size());
	}

	@Test
	public void femSpalterSjuttonIndex() {
		List<String> allaIndex = createIndexlist(17);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 5);
		assertEquals(5, spalter.size());
		assertEquals(4, spalter.get(0).size());
		assertEquals(4, spalter.get(1).size());
		assertEquals(3, spalter.get(2).size());
		assertEquals(3, spalter.get(3).size());
		assertEquals(3, spalter.get(4).size());
	}

	@Test
	public void treSpalterSextonIndexTake2() {
		List<String> allaIndex = createIndexlist(16);
		List<List<String>> spalter = album.skapaSpalter(allaIndex, 3);
		assertEquals(3, spalter.size());
		assertEquals(6, spalter.get(0).size());
		assertEquals(5, spalter.get(1).size());
		assertEquals(5, spalter.get(2).size());
	}

	private List<String> createIndexlist(int i) {
		List<String> indexes = new ArrayList<>();
		
		for (int j = 0; j < i; j++) {
			indexes.add("idx" + j);
		}

		return indexes;
	}
}
