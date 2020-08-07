package com.example.food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteService {
    public static final String BASE_URL = "http://192.168.0.15:8088/food/";

    @GET("list.jsp")
    Call<List<FoodVO>> listFood();

    @POST("insert.jsp")
    Call<Void> insertFood(@Query("name") String name,
                          @Query("address") String address,
                          @Query("tel") String tel,
                          @Query("latitude") Double latitude,
                          @Query("longitude") Double longitude);
}
