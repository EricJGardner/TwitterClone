package com.tts.twitterClone.service;

import com.tts.twitterClone.model.Tag;
import com.tts.twitterClone.model.Tweet;
import com.tts.twitterClone.model.User;
import com.tts.twitterClone.repository.TagRepository;
import com.tts.twitterClone.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TweetServiceImpl implements TweetService{


    private TweetRepository tweetRepository;


    private TagRepository tagRepository;

   @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, TagRepository tagRepository){
       this.tweetRepository = tweetRepository;
       this.tagRepository = tagRepository;
   }

    @Override
    public List<Tweet> findAll() {
//        return formatTweets(tweetRepository.findAllByOrderByCreatedAtDesc());
       List<Tweet> tweets =tweetRepository.findAllByOrderByCreatedAtDesc();
       return formatTweets(tweets);
//        return tweetRepository.findAllByOrderByCreatedAtDesc();
    }

    private List<Tweet> formatTweets(List<Tweet> tweets) {
        addTagLinks(tweets);
        shortenLinks(tweets);
        return tweets;
    }

    @Override
    public List<Tweet> findAllByUser(User user) {
        List<Tweet> tweets = tweetRepository.findAllByUserOrderByCreatedAtDesc(user);
        return tweets;
    }

    @Override
    public List<Tweet> findAllByUsers(List<User> users) {
        List<Tweet> tweets = tweetRepository.findAllByUserInOrderByCreatedAtDesc(users);
        return tweets;
    }

    @Override
    public List<Tweet> findAllWithTag(String tag) {
//        return formatTweets(tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag));
        List<Tweet> tweets = tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatTweets(tweets);
   }

    @Override
    public void handleTags(Tweet tweet) {
        List<Tag> tags = new ArrayList<Tag>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(tweet.getMessage());
        while (matcher.find()) {
            String phrase = matcher.group().substring(1).toLowerCase();
            Tag tag = tagRepository.findByPhrase(phrase);
            if (tag == null) {
                tag = new Tag();
                tag.setPhrase(phrase);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        tweet.setTags(tags);
    }

    @Override
    public void save(Tweet tweet) {
        handleTags(tweet);
        tweetRepository.save(tweet);
    }

    private void addTagLinks(List<Tweet> tweets) {
        Pattern pattern = Pattern.compile("#\\w+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            Set<String> tags = new HashSet<String>();
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            for (String tag : tags) {
                message = message.replaceAll(tag,
                        "<a class=\"tag\" href=\"/tweets/" + tag.substring(1).toLowerCase() + "\">" + tag + "</a>");
            }
            tweet.setMessage(message);
        }
    }

    private void shortenLinks(List<Tweet> tweets) {
        Pattern pattern = Pattern.compile("https?[^ ]+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String link = matcher.group();
                String shortenedLink = link;
                if (link.length() > 23) {
                    shortenedLink = link.substring(0, 20) + "...";
                    message = message.replace(link,
                            "<a class=\"tag\" href=\"" + link + "\" target=\"_blank\">" + shortenedLink + "</a>");
                }
                tweet.setMessage(message);
            }

        }
    }
}
