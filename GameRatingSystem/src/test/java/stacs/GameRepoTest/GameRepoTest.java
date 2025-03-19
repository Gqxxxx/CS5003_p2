package stacs.GameRepoTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import stacs.GameRepo.GameRepo;
import stacs.Games.Games;
import stacs.Users.Users;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameRepoTest {
    private GameRepo gameRepo;
    private Games game1, game2, game3;
    private Users user1, user2;

    // Setting up initial classes to be used in each test
    @BeforeEach
    public void initialSetUpForTests () {
        gameRepo = new GameRepo();
        ArrayList<String> game1GenreList = new ArrayList<>(List.of("Relaxing"));
        game1 = new Games(1, "Candy Crush", game1GenreList, LocalDate.of(2012, 04, 12), "Mobile Phone");
        ArrayList<String> game2GenreList = new ArrayList<>(List.of("Relaxing", "Thrilling"));
        game2 = new Games(2, "Subway Surfers", game2GenreList, LocalDate.of(2012, 05, 24), "Mobile Phone");
        ArrayList<String> game3GenreList = new ArrayList<>(List.of( "Learning", "Wordy"));
        game3 = new Games(3, "Wordscapes", game3GenreList, LocalDate.of(2017, 06, 14), "Mobile Phone");
        user1 = new Users(1, "John Doe");
        user2 = new Users(2, "Jane Doe");
    }

    // A test to check that a game has been added to the games list
    @Test
    public void shouldAddGameToGamesList() {
        gameRepo.addGame(game1);
        ArrayList<Games> listOfGames = gameRepo.getGames();
        assertEquals(1, listOfGames.size());
        assertTrue(listOfGames.contains(game1));
    }

    // A test to check that Game ID is not 0
    // This test is to make sure that a Game with gameID of 0..
    // ..should not be added to the listOfGames
    @Test
    public void testGameIdShouldNotBeZero() {
        gameRepo.addGame(game1); // Adding game with ID above 0
        ArrayList<String> testGameGenreList = new ArrayList<>(List.of( "Thrilling", "Relaxing"));
        Games testGame = new Games(0, "Temple Run", testGameGenreList, LocalDate.of(2011, 8, 04), "Mobile Phone");
        gameRepo.addGame(testGame); // Adding game with ID of 0
        ArrayList<Games> listOfGames = gameRepo.getGames(); // get list of games
        assertEquals(1, listOfGames.size());
        assertTrue(listOfGames.contains(game1));
        assertFalse(listOfGames.contains(testGame));
    }

    // A test to check that Game ID is not a negative number
    // This test ensures a game with an ID of a negative number..
    // ..is not added to the listOfGames ArrayList
    @Test
    public void testGameIdShouldNotBeNegative() {
        ArrayList<String> testGameGenreList = new ArrayList<>(List.of( "Feel-good", "Relaxing"));
        Games testGame = new Games(-8, "Fruit Ninja", testGameGenreList, LocalDate.of(2010, 5, 21), "Mobile Phone");
        gameRepo.addGame(testGame);
        ArrayList<Games> listOfGames = gameRepo.getGames(); // Get list of games
        assertEquals(0, listOfGames.size());
        assertFalse(listOfGames.contains(testGame));
    }

    // A test to check that you can list Games with a specific genre..
    // .. in their genre ArrayList
    @Test
    public void testListGamesByGenre() {
        gameRepo.addGame(game1);
        gameRepo.addGame(game2);
        ArrayList<String> gameGenresList1 = new ArrayList<>(List.of("Relaxing"));
        ArrayList<Games> gamesListByGenre = gameRepo.listGamesByGenre(gameGenresList1);
        assertEquals(2, gamesListByGenre.size());
        // Testing the listGamesByGenre() with multiple genres
        ArrayList<String> gameGenresList2 = new ArrayList<>(List.of("Relaxing", "Thrilling"));
        gamesListByGenre = gameRepo.listGamesByGenre(gameGenresList2);
        assertEquals(1, gamesListByGenre.size());
    }

    // A test to check that the listOfGames ArrayList should..
    // ..initiallty be empty
    @Test
    public void gameListShouldBeInitiallyEmpty() {
        ArrayList<Games> listOfGames = gameRepo.getGames();
        assertEquals(0, listOfGames.size());
    }

    // A test to check that the listGamesByGenre() is case insensitive
    @Test
    public void testgamesByGenreIsCaseInsensitive() {
        gameRepo.addGame(game3);
        ArrayList<String> testGenreList = new ArrayList<>(List.of("LeArNinG"));
        ArrayList<Games> gamesListByGenre = gameRepo.listGamesByGenre(testGenreList);
        assertEquals(1, gamesListByGenre.size());
    }

    // A test that ensures the same game is not added to the listOfGames twice..
    @Test
    public void shouldNotAddSameGameTwice() {
        gameRepo.addGame(game1);
        gameRepo.addGame(game2);
        gameRepo.addGame(game2);
        ArrayList<Games> listOfGames = gameRepo.getGames();
        assertEquals(2, listOfGames.size());
    }

    // A test to check that an empty list is returned..
    // ..if the genre is not in any of the games genre ArrayList
    @Test
    public void shouldReturnEmptyListIfNoGenre() {
        gameRepo.addGame(game1);
        gameRepo.addGame(game2);
        gameRepo.addGame(game3);
        ArrayList<String> testGenreList = new ArrayList<>(List.of("Non-Exist"));
        ArrayList<Games> gamesListByGenre = gameRepo.listGamesByGenre(testGenreList);
        assertEquals(0, gamesListByGenre.size());
    }

    // A test to check that the exception is thrown properly handled..
    // .. if argument for listGamesByGenre method is null
    @Test
    public void shouldThrowExceptionIfNoArgumentInGenre() {
        gameRepo.addGame(game1);
        gameRepo.addGame(game2);
        // Ensuring an exception is thrown.
        Exception anException = assertThrows(IllegalArgumentException.class, () -> {
            gameRepo.listGamesByGenre(null);
        });

        assertEquals("A String ArrayList is required", anException.getMessage());
    }

    // A test to check that the IllegalArgumentException is..
    // ..thrown and handled, if the addGame() method is called with..
    // ..null as the argument
    @Test 
    public void shouldThrowExceptionIfNullArgumentInAddGame() {
        Exception anException = assertThrows(IllegalArgumentException.class, () -> {
            gameRepo.addGame(null);
        });

        ArrayList<Games> listOfGames = gameRepo.getGames();
        assertEquals(0, listOfGames.size());
        assertEquals("A Game object is required", anException.getMessage());
    }

    // A test that checks that the listOfUsers ArrayList..
    // ..is initially empty
    @Test
    public void userListShouldBeInitiallyEmpty() {
        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(0, listOfUsers.size());
    }

    // A test to check that a user can be added to the listOfUsers ArrayList
    @Test
    public void shouldAddUserToUsersList() {
        gameRepo.addUser(user1);
        gameRepo.addUser(user2);
        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(2, listOfUsers.size());
        assertTrue(listOfUsers.contains(user1));
        assertTrue(listOfUsers.contains(user2));
    }

    // A test to check that User Id should not be 0
    // It does this by not adding the User to the listOfUsers ArrayList
    @Test
    public void testUserIdSHouldNotBeZero() {
        Users testUser = new Users(0, "The Star");
        gameRepo.addUser(user1);
        gameRepo.addUser(testUser);
        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(1, listOfUsers.size());
        assertTrue(listOfUsers.contains(user1));
        assertFalse(listOfUsers.contains(testUser));
    }

    // A test to ensure the UserId id not a nagative number..
    // ..by ensuring a user with id of a negative number..
    // ..is not added to the list
    @Test
    public void testUserIdShouldNotBeNegative() {
        Users testUser = new Users(-15, "The Star");
        gameRepo.addUser(testUser);
        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(0, listOfUsers.size());
        assertFalse(listOfUsers.contains(testUser));
    }

    // A test to check that a user is not added to the..
    // ..listOfUsers ArrayList twice
    @Test
    public void shouldNotAddSameUserTwice() {
        gameRepo.addUser(user1);
        gameRepo.addUser(user2);
        gameRepo.addUser(user2);
        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(2, listOfUsers.size());
    }

    // A test to check that the addUser() method does not..
    // ..add null values to the listOfUsers ArrayList..
    // ..but an exception is thrown.
    @Test
    public void shouldThrowExceptionIfNullArgumentInAddUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gameRepo.addUser(null);
        });

        ArrayList<Users> listOfUsers = gameRepo.getUsers();
        assertEquals(0, listOfUsers.size());
        assertEquals("A User object is required", exception.getMessage());
    }

    // 1. Test: Finding an existing user by ID
    @Test
    public void testFindUserByIdExists() {
        gameRepo.addUser(user1); // Ensure user1 is added
        Users foundUser = gameRepo.findUserById(1);
        assertNotNull(foundUser, "Should find user with ID 1");
        assertEquals("John Doe", foundUser.getName(), "User name should be John Doe");
    }

    // 2. Test: Finding a non-existing user by ID
    @Test
    public void testFindUserByIdNotFound() {
        gameRepo.addUser(user1);
        gameRepo.addUser(user2);
        Users foundUser = gameRepo.findUserById(999); // Non-existing user ID
        assertNull(foundUser, "Should return null for non-existing user ID");
    }

    // 3. Test: Updating a user's name
    @Test
    public void testUpdateUserName() {
        gameRepo.addUser(user1);
        gameRepo.updateUserName(1, "John Smith");
        Users updatedUser = gameRepo.findUserById(1);
        assertNotNull(updatedUser, "User should exist after update");
        assertEquals("John Smith", updatedUser.getName(), "User name should be updated to John Smith");
    }

    // 4. Test: Finding an existing game by ID
    @Test
    public void testFindGameByIdExists() {
        gameRepo.addGame(game2);
        Games foundGame = gameRepo.findGameById(2);
        assertNotNull(foundGame, "Should find game with ID 2");
        assertEquals("Subway Surfers", foundGame.getTitle(), "Game title should be Subway Surfers");
    }

    // 5. Test: Finding a non-existing game by ID
    @Test
    public void testFindGameByIdNotFound() {
        gameRepo.addGame(game1);
        gameRepo.addGame(game2);
        gameRepo.addGame(game3);
        Games foundGame = gameRepo.findGameById(999); // Non-existing game ID
        assertNull(foundGame, "Should return null for non-existing game ID");
    }

    // 6. Test: Adding a rating for a game by a user
    @Test
    public void testAddRating() {
        gameRepo.addRating(user1, game1, 4);
        Map<Users, Map<Games, Integer>> ratings = gameRepo.getUserGameRatings();
        assertEquals(4, ratings.get(user1).get(game1), "Rating should be 4");
    }

    // 7. Test: Multiple users rating the same game
    @Test
    public void testMultipleUsersRateSameGame() {
        gameRepo.addRating(user1, game1, 5);
        gameRepo.addRating(user2, game1, 3);
        Map<Users, Map<Games, Integer>> ratings = gameRepo.getUserGameRatings();
        assertEquals(5, ratings.get(user1).get(game1), "User 1's rating should be 5");
        assertEquals(3, ratings.get(user2).get(game1), "User 2's rating should be 3");
    }

    // 8. Test: Retrieving all ratings given by a user
    @Test
    public void testGetUserRatings() {
        gameRepo.addRating(user1, game1, 5);
        gameRepo.addRating(user1, game2, 2);
        Map<Users, Map<Games, Integer>> ratings = gameRepo.getUserGameRatings();
        assertEquals(2, ratings.get(user1).size(), "User 1 should have 2 ratings");
    }

    // 9. Test: Checking if a user without ratings is correctly handled
    @Test
    public void testUserWithoutRatings() {
        Map<Users, Map<Games, Integer>> ratings = gameRepo.getUserGameRatings();
        assertFalse(ratings.containsKey(user2), "User 2 should not have any ratings");
    }

    // 10. Test: Finding a user with a negative ID
    @Test
    public void testFindUserByNegativeId() {
        gameRepo.addUser(user1);
        gameRepo.addUser(user2);
        Users foundUser = gameRepo.findUserById(-1); // Invalid ID
        assertNull(foundUser, "Should return null for negative user ID");
    }

}
