package stacs.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import stacs.GameRepo.GameRepo;
import stacs.Games.Games;
import stacs.Users.Users;

import java.io.Serializable;
import java.util.*;

/**
 * GameService is a REST controller that handles operations for games, users, and ratings.
 * It delegates the data storage and retrieval to the GameRepo.
 */
@RestController
@RequestMapping("/api")
public class GameService {

    // GameRepo instance to manage games, users, and ratings
    private final GameRepo gameRepo;

    /**
     * Constructor with dependency injection for GameRepo.
     *
     * @param gameRepo The repository to manage games, users, and ratings.
     */
    @Autowired
    public GameService(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
        // ratingRepo is not used since ratings are stored within gameRepo
    }

    /**
     * Adds a new game.
     *
     * @param game The game object from the request body.
     * @return HTTP 200 OK if successful.
     */
    @PostMapping("/games")
    public ResponseEntity<Void> addGame(@RequestBody Games game) {
        gameRepo.addGame(game);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a new user.
     *
     * @param user The user object from the request body.
     * @return HTTP 200 OK if successful.
     */
    @PostMapping("/users")
    public ResponseEntity<Void> addUser(@RequestBody Users user) {
        gameRepo.addUser(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing user's name.
     *
     * @param userId  The ID of the user as a String from the path variable.
     * @param newName The new name from the request body.
     * @return HTTP 200 OK if update is successful; otherwise, HTTP 404 Not Found with an error message.
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<Map<String, Serializable>> updateUser(@PathVariable String userId, @RequestBody String newName) {
        // Convert the userId to an integer
        int intUserId = Integer.parseInt(userId);
        // Find the user by ID using the repository
        Users user = gameRepo.findUserById(intUserId);
        if(user != null) {
            // If the user exists, update the user's name
            gameRepo.updateUserName(intUserId, newName);
            return ResponseEntity.ok().build();
        } else {
            // If user is not found, return 404 with an error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found", "userId", intUserId));
        }
    }

    /**
     * Adds a rating for a game by a user.
     *
     * @param userId The ID of the user as a String from request parameters.
     * @param gameId The ID of the game as a String from request parameters.
     * @param rating The rating value.
     * @return HTTP 200 OK if successful; otherwise, HTTP 404 Not Found with an error message.
     */
    @PostMapping("/ratings")
    public ResponseEntity<Map<String, Serializable>> rateGame(@RequestParam String userId, @RequestParam String gameId, @RequestParam int rating) {
        // Parse the userId and gameId from String to int
        int intUserId = Integer.parseInt(userId);
        int intGameId = Integer.parseInt(gameId);

        // Retrieve the user and game using the repository
        Users user = gameRepo.findUserById(intUserId);
        Games game = gameRepo.findGameById(intGameId);

        if (user != null && game != null) {
            // If both user and game exist, add the rating
            gameRepo.addRating(user, game, rating);
            return ResponseEntity.ok().build();
        } else {
            // Return 404 if either the user or game is not found, with appropriate error details
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Game or user not found", "userId", intUserId, "gameId", intGameId));
        }
    }

    /**
     * Retrieves a list of games that match the specified genre.
     *
     * @param genre The genre to filter games.
     * @return A list of games matching the genre with HTTP 200 OK.
     */
    @GetMapping("/games")
    public ResponseEntity<List<Games>> listGamesByGenre(@RequestParam String genre) {
        // Create a list of genres (only one in this case) to pass to the repository method
        ArrayList<String> genresList = new ArrayList<>();
        genresList.add(genre);
        return ResponseEntity.ok(gameRepo.listGamesByGenre(genresList));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of users with HTTP 200 OK.
     */
    @GetMapping("/users")
    public ResponseEntity<List<Users>> listUsers() {
        return ResponseEntity.ok(gameRepo.getUsers());
    }

    /**
     * Retrieves the ratings for a specific user.
     *
     * @param userId The ID of the user as a String from the path variable.
     * @return A map of game titles to ratings if the user exists and has ratings;
     *         otherwise, returns HTTP 404 Not Found with an empty map.
     */
    @GetMapping("/ratings/{userId}")
    public ResponseEntity<Map<String, Integer>> listRatingsByUser(@PathVariable String userId) {
        // Convert userId to int
        int intUserId = Integer.parseInt(userId);
        // Find the user by ID
        Users user = gameRepo.findUserById(intUserId);
        // Retrieve the user-game ratings map from the repository
        Map<Users, Map<Games, Integer>> userGameRatings = gameRepo.getUserGameRatings();
        if (user != null && gameRepo.getUserGameRatings().containsKey(user)) {
            // Create a new map to store game title -> rating
            Map<String, Integer> ratings = new HashMap<>();
            // Iterate over the entry set for the specified user
            for (Map.Entry<Games, Integer> entry : userGameRatings.get(user).entrySet()) {
                // Map each game's title to its rating
                ratings.put(entry.getKey().getTitle(), entry.getValue());
            }
            return ResponseEntity.ok(ratings);
        } else {
            // Return 404 Not Found with an empty map if the user does not exist or has no ratings
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
        }
    }
}
