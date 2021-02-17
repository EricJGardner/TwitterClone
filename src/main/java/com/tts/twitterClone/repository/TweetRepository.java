package com.tts.twitterClone.repository;

import com.tts.twitterClone.model.Tweet;
import com.tts.twitterClone.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {

    List<Tweet> findAllByOrderByCreatedAtDesc();
    List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);
    List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);
    List<Tweet> findByTags_PhraseOrderByCreatedAtDesc(String phrase);
//    List<Tweet> findAllByOrderByCreatedAtDesc();
//    List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);
//    List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);

}
