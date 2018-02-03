package com.romankarpov.leavebehindlayout.demo;

import android.animation.Animator;
import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import com.romankarpov.leavebehindlayout.demo.model.Contact;
import com.romankarpov.leavebehindlayout.demo.services.ContactDeserializer;
import com.romankarpov.leavebehindlayout.demo.services.UiNamesService;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NamesApplication extends Application {
    private UiNamesService mUiNamesService;

    Animator anim;

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Contact.class, new ContactDeserializer())
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("ext", null)
                                .build();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://uinames.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        mUiNamesService = retrofit.create(UiNamesService.class);
    }

    public UiNamesService getUiNamesService() {
        return mUiNamesService;
    }
}
