package com.gpslab.kaun.retrofit;

import com.gpslab.kaun.model.GetLogoResponseJson;
import com.gpslab.kaun.model.GetSpamCallRequestJson;
import com.gpslab.kaun.model.GetSpamcallResponseJson;
import com.gpslab.kaun.model.UpdateUserRequestJson;
import com.gpslab.kaun.model.UpdateUserResponseJson;
import com.gpslab.kaun.retrofit_model.AddUserRequestJson;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.retrofit_model.AppContactsContactResponseJson;
import com.gpslab.kaun.retrofit_model.AppContactsRequestJsn;
import com.gpslab.kaun.retrofit_model.RefreshDataRequestJson;
import com.gpslab.kaun.retrofit_model.RefreshDataResponseJson;
import com.gpslab.kaun.retrofit_model.SearchContactRequestJson;
import com.gpslab.kaun.model.SearchContactMainResponseJson;
import com.gpslab.kaun.retrofit_model.SearchContactResponseJson;
import com.gpslab.kaun.retrofit_model.VerifyUserRequestJson;
import com.gpslab.kaun.retrofit_model.VerifyUserResponseJson;
import com.gpslab.kaun.model.ContactSyncResponseJson;
import com.gpslab.kaun.model.ContactSyncRequestJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserService {
    @POST("get_app_contacts")
    Call<VersionResponseJson> get_app_contacts(@Body VersionRequestJson param);




    @POST("adduser")
    Call<AddUserResponseJson> adduser(@Body AddUserRequestJson param);



    @GET("getlogo")
    Call<GetLogoResponseJson> getlogo();


    @POST("verify_user")
    Call<VerifyUserResponseJson> verify_user(@Body VerifyUserRequestJson param);



    @PUT("updateuser")
    Call<UpdateUserResponseJson> updateuser(@Body UpdateUserRequestJson param);












    @POST("contact_sync")
    Call<List<ContactSyncResponseJson>>contact_sync(@Body ContactSyncRequestJson param);








    @POST("refresh_data")
    Call<RefreshDataResponseJson> refresh_data(@Body RefreshDataRequestJson param);




    @POST("spamcall")
    Call<List<GetSpamcallResponseJson>> spamcall(@Body GetSpamCallRequestJson param);







    @POST("search_contact")
    Call<SearchContactResponseJson> search_contact(@Body SearchContactRequestJson param);



















    @POST("get_app_contacts")
    Call<AppContactsContactResponseJson> get_app_contacts(@Body AppContactsRequestJsn param);
}
