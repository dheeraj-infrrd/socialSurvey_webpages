package com.realtech.socialsurvey.web.api;

import com.realtech.socialsurvey.web.api.entities.AccountRegistrationAPIRequest;
import com.realtech.socialsurvey.web.api.entities.CaptchaAPIRequest;
import com.realtech.socialsurvey.web.entities.CompanyProfile;
import com.realtech.socialsurvey.web.entities.Payment;
import com.realtech.socialsurvey.web.entities.PersonalProfile;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;


public interface SSApiIntegration
{
    @POST ( "/v1/nocaptcha")
    Response validateCaptcha( @Body CaptchaAPIRequest captchaRequest );


    @POST ( "/v1/companies/register")
    Response initateRegistration( @Body AccountRegistrationAPIRequest registrationRequest );


    @GET ( "/v1/users/{userId}")
    Response getUserProfile( @Path ( "userId") String userId );


    @PUT ( "/v1/users/{userId}")
    Response updateUserProfile( @Body PersonalProfile personalProfile, @Path ( "userId") String userId );


    @PUT ( "/v1/users/{userId}/stage/{stage}")
    Response updateUserProfileStage( @Path ( "userId") String userId, @Path ( "stage") String stage );


    @GET ( "/v1/companies/{companyId}")
    Response getCompanyProfile( @Path ( "companyId") String companyId );


    @PUT ( "/v1/companies/{companyId}")
    Response updateCompanyProfile( @Body CompanyProfile companyProfile, @Path ( "companyId") String companyId,
        @Query ( "userId") String userId );


    @PUT ( "/v1/companies/{companyId}/stage/{stage}")
    Response updateCompanyProfileStage( @Path ( "companyId") String companyId, @Path ( "stage") String stage );


    @GET ( "/v1/industries")
    Response getVerticals();


    @GET ( "/v1/payment/plans")
    Response getPaymentPlans();


    @GET ( "/v1/users/{userId}/stage")
    Response getUserStage( @Path ( "userId") String userId );


    @GET ( "/v1/companies/{companyId}/stage")
    Response getCompanyStage( @Path ( "companyId") String companyId );


    @PUT ( "/v1/companies/{companyId}/profileimage")
    Response updateCompanyLogo( @Path ( "companyId") String companyId, @Query ( "userId") String userId, @Body String logoUrl );


    @DELETE ( "/v1/companies/{companyId}/profileimage")
    Response removeCompanyLogo( @Path ( "companyId") String companyId, @Query ( "userId") String userId );


    @PUT ( "/v1/users/{userId}/profileimage")
    Response updateUserProfileImage( @Path ( "userId") String userId, @Body String imageUrl );


    @DELETE ( "/v1/users/{userId}/profileimage")
    Response removeUserProfileImage( @Path ( "userId") String userId );


    @POST ( "/v1/companies/{companyId}/hierarchy")
    Response generateDefaultHierarchy( @Path ( "companyId") String companyId );


    @POST ( "/v1/companies/{companyId}/plan/{planId}/payment")
    Response makePayment( @Path ( "companyId") String companyId, @Path ( "planId") String planId, @Body Payment payment );


    @PUT ( "/v1/users/{userId}/password")
    Response savePassword( @Path ( "userId") String userId, @Body String password );


    @GET ( "/v1/usstates")
    Response getUsStates();


    @POST ( "/v1/webaddress")
    Response validateWebAddress( @Body String webAddress );
}