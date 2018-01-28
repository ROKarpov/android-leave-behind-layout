package com.romankarpov.leavebehindlayout.demo.services;

import java.util.List;

import com.romankarpov.leavebehindlayout.demo.model.Contact;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UiNamesService {
    @GET("/api/")
    Call<List<Contact>> getContacts(
            @Query("amount") Integer count
    );
}
