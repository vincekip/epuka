package com.kipruto.epuka.Fragments;



import com.kipruto.epuka.Notifications.MyResponse;
import com.kipruto.epuka.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAN6uRvlM:APA91bFJRJWSSnQhXbDfmIP-DtpUDxfpNkxRhmDYStogZNR8uSJhF1UFOmvOpTmBpFbPLTAxwR_r0o40u-vOT623BT5bcIGkBYl4qr5MMvFrnbM-BFml7JVep8caGV3pIb_qQ4g1S9GI"

            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
