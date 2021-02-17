package com.tts.twitterClone.service;

import com.tts.twitterClone.model.Tweet;
import com.tts.twitterClone.model.User;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    List<Tweet> findAllByUser(User user);

    List<Tweet> findAllByUsers(List<User> users);

    List<Tweet> findAllWithTag(String tag);

    void handleTags(Tweet tweet);

    void save(Tweet tweet);

//    List<Tweet> findAll();
//    List<Tweet> findAllByUser(User user);
//    List<Tweet> findAllByUsers(List<User> users);
//    void save(Tweet tweet);
}
