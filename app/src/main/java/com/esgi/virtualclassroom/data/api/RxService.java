package com.esgi.virtualclassroom.data.api;

import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.Message;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RxService {
    @GET("classrooms/") Single<Classroom> getClassrooms();
    @GET("classrooms/{id}/") Single<Message> getClassroom(@Path("id") String id);
}
