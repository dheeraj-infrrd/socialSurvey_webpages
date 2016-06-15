package com.realtech.socialsurvey.web.api;

import com.realtech.socialsurvey.web.api.entities.AccountRegistrationAPIRequest;
import com.realtech.socialsurvey.web.api.entities.CaptchaAPIRequest;
import com.realtech.socialsurvey.web.entities.CompanyProfile;
import com.realtech.socialsurvey.web.entities.Payment;
import com.realtech.socialsurvey.web.entities.PersonalProfile;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface SSApiIntegration
{
    @POST ( "/utils/nocaptcha/validate")
    Response validateCaptcha( @Body CaptchaAPIRequest captchaRequest );


    @POST ( "/account/register/init")
    Response initateRegistration( @Body AccountRegistrationAPIRequest registrationRequest );


    @GET ( "/users/profile/details/{userId}")
    Response getUserProfile( @Path ( "userId") String userId );


    @PUT ( "/users/profile/update/{userId}")
    Response updateUserProfile( @Body PersonalProfile personalProfile, @Path ( "userId") String userId );


    @PUT ( "/users/profile/stage/update/{userId}/{stage}")
    Response updateUserProfileStage( @Path ( "userId") String userId, @Path ( "stage") String stage );


    @GET ( "/account/company/profile/details/{companyId}")
    Response getCompanyProfile( @Path ( "companyId") String companyId );


    @PUT ( "/account/company/profile/update/{companyId}/{userId}")
    Response updateCompanyProfile( @Body CompanyProfile companyProfile, @Path ( "companyId") String companyId,
        @Path ( "userId") String userId );


    @PUT ( "/account/company/profile/stage/update/{companyId}/{stage}")
    Response updateCompanyProfileStage( @Path ( "companyId") String companyId, @Path ( "stage") String stage );


    @GET ( "/account/company/profile/industries")
    Response getVerticals();


    @GET ( "/account/payment/plans")
    Response getPaymentPlans();


    @GET ( "/users/profile/stage/{userId}")
    Response getUserStage( @Path ( "userId") String userId );


    @GET ( "/account/company/profile/stage/{companyId}")
    Response getCompanyStage( @Path ( "companyId") String companyId );


    @PUT ( "/account/company/profile/profileimage/update/{companyId}/{userId}")
    Response updateCompanyLogo( @Path ( "companyId") String companyId, @Path ( "userId") String userId, @Body String logoUrl );


    @PUT ( "/account/company/profile/profileimage/remove/{companyId}/{userId}")
    Response removeCompanyLogo( @Path ( "companyId") String companyId, @Path ( "userId") String userId );


    @PUT ( "/users/profile/profileimage/update/{userId}")
    Response updateUserProfileImage( @Path ( "userId") String userId, @Body String imageUrl );


    @PUT ( "/users/profile/profileimage/remove/{userId}")
    Response removeUserProfileImage( @Path ( "userId") String userId );


    @POST ( "/account/company/generate/hierarchy/{companyId}")
    Response generateDefaultHierarchy( @Path ( "companyId") String companyId );


    @POST ( "/account/payment/company/{companyId}/plan/{planId}")
    Response makePayment( @Path ( "companyId") String companyId, @Path ( "planId") String planId, @Body Payment payment );


    @PUT ( "/users/profile/password/update/{userId}")
    Response savePassword( @Path ( "userId") String userId, @Body String password );


    @GET ( "/account/company/usstates")
    Response getUsStates();


    @POST ( "/users/profile/webaddress/validate")
    Response validateWebAddress( @Body String webAddress );
}