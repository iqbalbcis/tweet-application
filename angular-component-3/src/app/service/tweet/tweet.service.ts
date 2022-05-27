import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class TweetService {

  constructor(private http:HttpClient) { }

  addTweetDetails(tweet: object): Observable<any>
  {
    return this.http.post(
      `${API_URL}/api/v1.0/tweets/add`
        , tweet);
  }

  deleteTweetDetails(tweetId: number): Observable<any>
  {
    return this.http.delete(
      `${API_URL}/api/v1.0/tweets/delete/${tweetId}`);
  }

  findAllTweets(username:string): Observable<any>
  {
    return this.http.get(
      `${API_URL}/api/v1.0/tweets/${username}`);
  }

  findAllTweetAndReply(username:string): Observable<any>
  {
    return this.http.get(
      `${API_URL}/api/v1.0/tweets/reply/like/${username}`);
  }

  addReplyDetails(username:string, tweetId: number, reply: object): Observable<any>
  {
    return this.http.post(
      `${API_URL}/api/v1.0/tweets/${username}/reply/${tweetId}`
        , reply);
  }

  updateTweetDetails(tweetId: number, tweet: object): Observable<any>
  {
    return this.http.put(
      `${API_URL}/api/v1.0/tweets/update/${tweetId}`
        , tweet);
  }

  updateTweetLike(username:string, tweetId: number, tweetLike: object): Observable<any>
  {
    return this.http.put(
      `${API_URL}/api/v1.0/tweets/${username}/like/${tweetId}`
        , tweetLike);
  }

  totalLikeCount(tweetId: number): Observable<any> {
    return this.http.get(
      `${API_URL}/api/v1.0/tweets/countTotalLike/${tweetId}`);
  }

  sendMessage(message:string): Observable<any>
  {
    return this.http.post(
      `${API_URL}/api/v1.0/tweets/send/${message}`, null);
  }
}
