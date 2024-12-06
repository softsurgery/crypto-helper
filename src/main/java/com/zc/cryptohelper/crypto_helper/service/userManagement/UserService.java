package com.zc.cryptohelper.crypto_helper.service.userManagement;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.models.User;
import com.zc.cryptohelper.crypto_helper.repository.CoinRepository;
import com.zc.cryptohelper.crypto_helper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoinRepository coinRepository;

    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Find a user by ID
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // Find a user by username
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Update user details
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Add a coin to user's favorite list
    public void addFavouriteCoin(Long userId, Long coinId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Coin> coinOpt = coinRepository.findById(coinId);

        if (userOpt.isPresent() && coinOpt.isPresent()) {
            User user = userOpt.get();
            Coin coin = coinOpt.get();
            user.getFavouriteCoins().add(coin);  // Add the coin to the user's favorite list
            userRepository.save(user);           // Save the updated user
        } else {
            throw new RuntimeException("User or Coin not found.");
        }
    }

    // Remove a coin from user's favorite list
    public void removeFavouriteCoin(Long userId, Long coinId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Coin> coinOpt = coinRepository.findById(coinId);

        if (userOpt.isPresent() && coinOpt.isPresent()) {
            User user = userOpt.get();
            Coin coin = coinOpt.get();
            user.getFavouriteCoins().remove(coin);  // Remove the coin from the user's favorite list
            userRepository.save(user);              // Save the updated user
        } else {
            throw new RuntimeException("User or Coin not found.");
        }
    }

    // Get the list of a user's favorite coins
    public Set<Coin> getFavouriteCoins(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get().getFavouriteCoins();
        } else {
            throw new RuntimeException("User not found.");
        }
    }
}
