package se.frihak.album;

public class Album {

	final private String namn;

	public Album(String name) {
		this.namn = name;
	}

	public String getNamn() {
		return namn;
	}

	@Override
	public String toString() {
		return "Album [namn=" + namn + "]";
	}

}
