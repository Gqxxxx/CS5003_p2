package stacs.GameServiceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import stacs.GameService.GameService;
import stacs.GameRepo.GameRepo;
import stacs.Games.Games;
import stacs.Users.Users;

import java.io.Serializable;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    // Create a mock instance of GameRepo to simulate repository behavior
    @Mock
    private GameRepo gameRepo;

    // Inject the mocks into the GameService instance
    @InjectMocks
    private GameService gameService;

    // Sample game and user objects for testing purposes
    private Games game;
    private Users user;

    @BeforeEach
    public void setUp() {
        // Initialize a sample game object:
        // ID: 1, Title: "Elden Ring", Genres: ["RPG"], release date: null, Platform: "PC"
        game = new Games(1, "Elden Ring", new ArrayList<>(Arrays.asList("RPG")), null, "PC");
        // Initialize a sample user object with ID 1 and name "JohnDoe"
        user = new Users(1, "JohnDoe");
    }

    /**
     * Test 1: Verify that adding a game calls addGame() on the repository and returns HTTP 200 OK.
     */
    @Test
    public void testAddGame() {
        ResponseEntity<Void> response = gameService.addGame(game);
        verify(gameRepo, times(1)).addGame(game);  // Verify that addGame() is called once
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test 2: Verify that adding a user calls addUser() on the repository and returns HTTP 200 OK.
     */
    @Test
    public void testAddUser() {
        ResponseEntity<Void> response = gameService.addUser(user);
        verify(gameRepo, times(1)).addUser(user); // Verify that addUser() is called once
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test 3: Update an existing user successfully.
     * Expect HTTP 200 OK and verify that updateUserName() is called with correct parameters.
     */
    @Test
    public void testUpdateUserSuccess() {
        when(gameRepo.findUserById(1)).thenReturn(user); // Mock repository to return the user
        ResponseEntity<Map<String, Serializable>> response = gameService.updateUser("1", "NewName");
        verify(gameRepo, times(1)).updateUserName(1, "NewName"); // Verify updateUserName is called
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test 4: Attempt to update a user that doesn't exist.
     * Expect HTTP 404 NOT_FOUND with an error message in the response body.
     */
    @Test
    public void testUpdateUserFailure() {
        when(gameRepo.findUserById(1)).thenReturn(null); // Simulate that user is not found
        ResponseEntity<Map<String, Serializable>> response = gameService.updateUser("1", "NewName");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Serializable> body = response.getBody();
        assertNotNull(body);
        assertEquals("User not found", body.get("error"));
        assertEquals(1, body.get("userId"));
    }

    /**
     * Test 5: Successfully rate a game when both user and game exist.
     * Verify that addRating() is invoked and HTTP 200 OK is returned.
     */
    @Test
    public void testRateGameSuccess() {
        when(gameRepo.findUserById(1)).thenReturn(user); // Mock valid user
        when(gameRepo.findGameById(1)).thenReturn(game);   // Mock valid game
        ResponseEntity<Map<String, Serializable>> response = gameService.rateGame("1", "1", 5);
        verify(gameRepo, times(1)).addRating(user, game, 5); // Verify addRating() is called with correct values
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test 6: Attempt to rate a game when the user is not found.
     * Expect HTTP 404 NOT_FOUND with appropriate error information.
     */
    @Test
    public void testRateGameFailureUserNotFound() {
        when(gameRepo.findUserById(1)).thenReturn(null);  // Simulate user not found
        when(gameRepo.findGameById(1)).thenReturn(game);    // Valid game returned
        ResponseEntity<Map<String, Serializable>> response = gameService.rateGame("1", "1", 5);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Serializable> body = response.getBody();
        assertNotNull(body);
        assertEquals("Game or user not found", body.get("error"));
        assertEquals(1, body.get("userId"));
    }

    /**
     * Test 7: Attempt to rate a game when the game is not found.
     * Expect HTTP 404 NOT_FOUND with appropriate error information.
     */
    @Test
    public void testRateGameFailureGameNotFound() {
        when(gameRepo.findUserById(1)).thenReturn(user);   // Valid user returned
        when(gameRepo.findGameById(1)).thenReturn(null);     // Simulate game not found
        ResponseEntity<Map<String, Serializable>> response = gameService.rateGame("1", "1", 5);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Serializable> body = response.getBody();
        assertNotNull(body);
        assertEquals("Game or user not found", body.get("error"));
        assertEquals(1, body.get("userId"));
    }

    /**
     * Test 8: Retrieve a list of games by genre when matching games exist.
     * Expect HTTP 200 OK and a list with at least one game.
     */
    @Test
    public void testListGamesByGenreSuccess() {
        List<Games> gamesList = Arrays.asList(game);
        // When any genre list is passed, return the predefined list containing the game
        when(gameRepo.listGamesByGenre(any())).thenReturn(new ArrayList<>(gamesList));
        ResponseEntity<List<Games>> response = gameService.listGamesByGenre("RPG");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Test 9: Retrieve games by genre when no games match.
     * Expect HTTP 200 OK and an empty list.
     */
    @Test
    public void testListGamesByGenreEmpty() {
        when(gameRepo.listGamesByGenre(any())).thenReturn(new ArrayList<>()); // Return empty list
        ResponseEntity<List<Games>> response = gameService.listGamesByGenre("Action");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    /**
     * Test 10: Retrieve a list of all users.
     * Expect HTTP 200 OK and a list containing the users.
     */
    @Test
    public void testListUsers() {
        List<Users> usersList = Arrays.asList(user);
        when(gameRepo.getUsers()).thenReturn(new ArrayList<>(usersList)); // Return list of users
        ResponseEntity<List<Users>> response = gameService.listUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Test 11: Retrieve ratings for a user who has ratings.
     * Expect HTTP 200 OK and a mapping of game titles to their ratings.
     */
    @Test
    public void testListRatingsByUserSuccess() {
        when(gameRepo.findUserById(1)).thenReturn(user); // Valid user
        // Create a ratings map for the user
        Map<Games, Integer> ratingsMap = new HashMap<>();
        ratingsMap.put(game, 5);
        Map<Users, Map<Games, Integer>> userRatings = new HashMap<>();
        userRatings.put(user, ratingsMap);
        when(gameRepo.getUserGameRatings()).thenReturn(userRatings); // Return the ratings map

        ResponseEntity<Map<String, Integer>> response = gameService.listRatingsByUser("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Integer> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(5, body.get("Elden Ring"));
    }

    /**
     * Test 12: Retrieve ratings for a user that does not exist.
     * Expect HTTP 404 NOT_FOUND.
     */
    @Test
    public void testListRatingsByUserFailureUserNotFound() {
        when(gameRepo.findUserById(2)).thenReturn(null); // Simulate user not found
        ResponseEntity<Map<String, Integer>> response = gameService.listRatingsByUser("2");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test 13: Retrieve ratings for a user who exists but has no ratings.
     * Expect HTTP 404 NOT_FOUND.
     */
    @Test
    public void testListRatingsByUserEmptyRatings() {
        when(gameRepo.findUserById(1)).thenReturn(user); // Valid user returned
        // Simulate empty ratings map (no rating record for the user)
        when(gameRepo.getUserGameRatings()).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, Integer>> response = gameService.listRatingsByUser("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test 14: Attempt to update a user with a non-numeric userId.
     * Expect NumberFormatException.
     */
    @Test
    public void testUpdateUserInvalidUserId() {
        assertThrows(NumberFormatException.class, () -> {
            gameService.updateUser("abc", "NewName");
        });
    }

    /**
     * Test 15: Attempt to rate a game with a non-numeric userId.
     * Expect NumberFormatException.
     */
    @Test
    public void testRateGameInvalidUserId() {
        assertThrows(NumberFormatException.class, () -> {
            gameService.rateGame("abc", "1", 5);
        });
    }
}
