package stacs.Games;

import java.time.LocalDate;
import java.util.ArrayList;

public class Games {
    private int identifier;
    private String title;
    private ArrayList<String> genres;
    private LocalDate releaseDate;
    private String platform;
    private ArrayList<Integer> rating;

    public Games(int identifier, String title, ArrayList<String> genres, LocalDate releaseDate, String platform) {
        this.identifier = identifier;
        this.title = title;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.platform = platform;
    }

    public int getGameId() {
        return identifier;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getTitle() {
        return title;
    }
    
}
