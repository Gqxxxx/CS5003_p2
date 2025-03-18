package stacs.GameRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import stacs.Games.Games;
import stacs.Users.Users;

@Repository
public class GameRepo {
    private ArrayList<Games> listOfGames = new ArrayList<>();
    private ArrayList<Users> listOfUsers = new ArrayList<>();
    private Map<Users, Map<Games, Integer>> userGameRatings = new HashMap<>();


    // A getter for the listOfGames arrayList
    public ArrayList<Games> getGames() {
        return listOfGames;
    }

    // A getter for the listOfUsers arrayList
    public ArrayList<Users> getUsers() {
        return listOfUsers;
    }

    // A method to add a game to the games ArrayList
    public void addGame(Games newGame) {
        if (newGame == null) {
            throw new IllegalArgumentException("A Game object is required");
        }

        if (newGame.getGameId() > 0 && !isGameInList(newGame)) {
            listOfGames.add(newGame);
        }
    }

    // A method to check if a game is already in..
    // ..the listOfGames ArrayList
    public boolean isGameInList(Games game) {
        boolean isInGameList = false;
        String gameTitle = game.getTitle();
        for (Games gameInList : listOfGames) {
            String existingGameTitle = gameInList.getTitle();
            // Check if ID or game title are similar
            if (gameInList.getGameId() == game.getGameId() || 
                existingGameTitle.equalsIgnoreCase(gameTitle)) {
                isInGameList = true;
            }
        }
        return isInGameList;
    }

    // A method to list all the games in the game list whose genre..
    // .. matches a genre list
    public ArrayList<Games> listGamesByGenre(ArrayList<String> genresList) {
        if(genresList == null) {
            throw new IllegalArgumentException("A String ArrayList is required");
        }

        ArrayList<Games> gamesMatchingGenres = new ArrayList<>();
        ArrayList<String> lowerCaseGenreList = convertListToLowerCase(genresList);
        for (Games game : listOfGames) {
            ArrayList<String> gameGenreList = game.getGenres();
            ArrayList<String> lowerCaseGameGenres = convertListToLowerCase(gameGenreList);
            if (lowerCaseGameGenres.containsAll(lowerCaseGenreList)) {
                gamesMatchingGenres.add(game);
            }
        }
        return gamesMatchingGenres;
    }

    // A method that converts all the elements in a string ArrayList..
    // ..to lower case strings.
    public ArrayList<String> convertListToLowerCase(ArrayList<String> stringList) {
        ArrayList<String> lowerCaseStringList = new ArrayList<>();
        for (String item : stringList) {
            lowerCaseStringList.add(item.toLowerCase());
        }
        return lowerCaseStringList;
    }

    // A method that adds a user to the Users ArrayList
    public void addUser(Users newUser) {
        if (newUser == null) {
            throw new IllegalArgumentException("A User object is required");
        }

        if (newUser.getUserId() > 0 && !isUserInList(newUser)) {
            listOfUsers.add(newUser);
        }
    }

    // A method to check if a User is in the listOfUsers..
    // ..ArrayList
    public boolean isUserInList(Users user) {
        boolean isInUserList = false;
        for (Users userInList : listOfUsers) {
            // Check if ID are similar
            if (userInList.getUserId() == user.getUserId()) {
                    isInUserList = true;
            }
        }
        return isInUserList;
    }

    /**
     * Finds a user by its unique ID.
     *
     * @param userId the unique identifier of the user
     * @return the Users object if found; otherwise, returns null
     */
    public Users findUserById(int userId) {
        // Retrieve the list of users from the repository
        ArrayList<Users> listOfUsers = getUsers();
        // Iterate over each user in the list
        for (Users user : listOfUsers) {
            // Check if the current user's ID matches the provided userId
            if (user.getUserId() == userId) {
                // Return the user if found
                return user;
            }
        }
        // Return null if no matching user is found
        return null;
    }

    /**
     * Updates the name of a user with the given user ID.
     *
     * @param userId  the unique identifier of the user
     * @param newName the new name to set for the user
     */
    public void updateUserName(int userId, String newName) {
        // Iterate over each user in the listOfUsers
        // (Assuming listOfUsers is a class field accessible within this context)
        for (Users user : listOfUsers) {
            // Check if the user's ID matches the given userId
            if (user.getUserId() == userId) {
                // Set the new name for the user
                user.setName(newName);
                // Exit the method after updating
                return;
            }
        }
    }

    /**
     * Finds a game by its unique ID.
     *
     * @param gameId the unique identifier of the game
     * @return the Games object if found; otherwise, returns null
     */
    public Games findGameById(int gameId) {
        // Retrieve the list of games from the repository
        ArrayList<Games> listOfGames = getGames();
        // Iterate over each game in the list
        for (Games game : listOfGames) {
            // Check if the current game's ID matches the provided gameId
            if (game.getGameId() == gameId) {
                // Return the game if found
                return game;
            }
        }
        // Return null if no matching game is found
        return null;
    }

    /**
     * Adds a rating for a specific game by a specific user.
     *
     * @param user   the user who is rating the game
     * @param game   the game being rated
     * @param rating the rating value
     */
    public void addRating(Users user, Games game, int rating) {
        // Ensure that the user has an existing ratings map; if not, create one
        userGameRatings.putIfAbsent(user, new HashMap<>());
        // Store the rating for the game in the user's ratings map
        userGameRatings.get(user).put(game, rating);
    }

    /**
     * Returns the complete mapping of users to their game ratings.
     *
     * @return a map where the key is a user and the value is another map of games and their corresponding ratings
     */
    public Map<Users, Map<Games, Integer>> getUserGameRatings() {
        return userGameRatings;
    }



}
