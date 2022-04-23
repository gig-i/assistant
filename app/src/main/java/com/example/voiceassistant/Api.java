package com.example.voiceassistant;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("api/chat")
    Call<String> getResponse(@Query("msg") String msg);
}
