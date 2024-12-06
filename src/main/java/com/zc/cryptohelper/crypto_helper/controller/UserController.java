package com.zc.cryptohelper.crypto_helper.controller;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.models.User;
import com.zc.cryptohelper.crypto_helper.service.userManagement.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    //Admin Function
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Endpoint to get user details by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.findUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get user details by username
    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to update user information
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            User existingUser = user.get();
            // Update fields from userDetails
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            User updatedUser = userService.updateUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to add a coin to user's favorite list
    @PostMapping("/{userId}/favourite-coins/{coinId}")
    public ResponseEntity<String> addFavouriteCoin(@PathVariable Long userId, @PathVariable Long coinId) {
        try {
            userService.addFavouriteCoin(userId, coinId);
            return ResponseEntity.ok("Coin added to favourites.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to remove a coin from user's favorite list
    @DeleteMapping("/{userId}/favourite-coins/{coinId}")
    public ResponseEntity<String> removeFavouriteCoin(@PathVariable Long userId, @PathVariable Long coinId) {
        try {
            userService.removeFavouriteCoin(userId, coinId);
            return ResponseEntity.ok("Coin removed from favourites.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to get the list of user's favorite coins
    @GetMapping("/{username}/favourite-coins")
    public ResponseEntity<Set<Coin>> getFavouriteCoins(@PathVariable Long userId) {
        try {
            Set<Coin> favouriteCoins = userService.getFavouriteCoins(userId);
            return ResponseEntity.ok(favouriteCoins);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
