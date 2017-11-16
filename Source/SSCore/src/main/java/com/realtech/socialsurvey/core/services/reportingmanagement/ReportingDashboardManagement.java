package com.realtech.socialsurvey.core.services.reportingmanagement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.realtech.socialsurvey.core.entities.Company;
import com.realtech.socialsurvey.core.entities.CompanyDetailsReport;
import com.realtech.socialsurvey.core.entities.CompanyDigestRequestData;
import com.realtech.socialsurvey.core.entities.Digest;
import com.realtech.socialsurvey.core.entities.MonthlyDigestAggregate;
import com.realtech.socialsurvey.core.entities.OrganizationUnitSettings;
import com.realtech.socialsurvey.core.entities.RankingRequirements;
import com.realtech.socialsurvey.core.entities.SurveyResultsReportVO;
import com.realtech.socialsurvey.core.entities.ReportingSurveyPreInititation;
import com.realtech.socialsurvey.core.entities.UserRankingPastMonthMain;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.exception.NoRecordsFetchedException;
import com.realtech.socialsurvey.core.exception.NonFatalException;
import com.realtech.socialsurvey.core.services.mail.UndeliveredEmailException;

public interface ReportingDashboardManagement
{
    public void createEntryInFileUploadForReporting( int reportId, Date startDate, Date endDate, Long entityId, String entityType,Company company,
        Long adminUserid ) throws InvalidInputException, NoRecordsFetchedException, FileNotFoundException, IOException;

    public List<List<Object>> getSurveyStatsReport( Long entityId, String entityType );

    public List<List<Object>> getRecentActivityList( Long entityId, String entityType , int startIndex, int batchSize  ) throws InvalidInputException;

    public Long getRecentActivityCount( Long entityId, String entityType );

    String generateSurveyStatsForReporting( Long entityId, String entityType , Long userId ) throws UnsupportedEncodingException, NonFatalException;

    List<List<Object>> getUserAdoptionReport( Long entityId, String entityType );

    String generateUserAdoptionForReporting( Long entityId, String entityType, Long userId )
        throws UnsupportedEncodingException, NonFatalException;

    void deleteRecentActivity( Long fileUploadId );

    List<List<Object>> getCompanyUserReport( Long entityId, String entityType );

    String generateCompanyUserForReporting( Long entityId, String entityType, Long userId )
        throws UnsupportedEncodingException, NonFatalException;
	
	/**
	 * @param entityId
	 * @param entityType
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NonFatalException
	 * @throws ParseException
	 */
	public String generateSurveyResultsReport( Long entityId, String entityType, Long userId, Timestamp startDate, Timestamp endDate )
		        throws UnsupportedEncodingException, NonFatalException, ParseException;

    List<List<Object>> getSurveyTransactionReport( Long entityId, String entityType, Timestamp startDate, Timestamp endDate );

    String generateSurveyTransactionForReporting( Long entityId, String entityType, Long userId, Timestamp startDate,
        Timestamp endDate ) throws UnsupportedEncodingException, NonFatalException;
    
    List<List<Object>> getUserRankingThisYear(String entityType, Long entityId,int year,int startIndex,int batchSize);
    
    List<List<Object>> getUserRankingThisMonth(String entityType, Long entityId,int month,int year,int startIndex,int batchSize);
    
    List<List<Object>> getUserRankingPastMonth(String entityType, Long entityId,int month,int year,int startIndex,int batchSize);
    
    List<List<Object>> getUserRankingPastYear(String entityType, Long entityId,int year,int startIndex,int batchSize);

    Map<String, Object> fetchRankingCountThisYear(long entityId, String entityType, int year, int BatchSize )
        throws NonFatalException;

    Map<String, Object> fetchRankingCountThisMonth(long entityId, String entityType, int year, int month,
        int BatchSize ) throws NonFatalException;

    Map<String, Object> fetchRankingCountPastYear( long entityId, String entityType, int year, int BatchSize )
        throws NonFatalException;

    Map<String, Object> fetchRankingCountPastMonth( long entityId, String entityType, int year, int month,
        int BatchSize ) throws NonFatalException;

    Map<String, Object> fetchRankingRankCountThisYear( long userId, long entityId, String entityType, int year, int BatchSize )
        throws NonFatalException;

    Map<String, Object> fetchRankingRankCountThisMonth( long userId, long entityId, String entityType, int year, int month,
        int BatchSize ) throws NonFatalException;

    Map<String, Object> fetchRankingRankCountPastYear( long userId, long entityId, String entityType, int year, int BatchSize )
        throws NonFatalException;

    Map<String, Object> fetchRankingRankCountPastMonth( long userId, long entityId, String entityType, int year, int month,
        int BatchSize ) throws NonFatalException;

    Map<String, Object> fetchRankingRankCountPastYears( long userId, long entityId, String entityType, int BatchSize )
        throws NonFatalException;

    Map<String, Object> fetchRankingCountPastYears( long entityId, String entityType, int BatchSize ) throws NonFatalException;

    List<List<Object>> getUserRankingPastYears( String entityType, Long entityId, int startIndex, int batchSize );

    RankingRequirements updateRankingRequirements( int minimumRegistrationDays, float minimumCompletedPercentage,
        int minReviews, int monthOffset, int yearOffset );

    RankingRequirements updateRankingRequirementsMongo( String collection, OrganizationUnitSettings unitSettings,
        RankingRequirements rankingRequirements ) throws InvalidInputException;

    Long getRegionIdFromBranchId(long branchId);

    List<List<Object>> getUserRankingReportForYear( Long entityId, String entityType, int year );

    List<List<Object>> getUserRankingReportForMonth( Long entityId, String entityType, int year, int month );

    String generateUserRankingForReporting( Long entityId, String entityType, Long userId, Timestamp startDate, int type )
        throws UnsupportedEncodingException, NonFatalException;
    
    List<List<Object>> getScoreStatsForOverall(Long entityId, String entityType, int currentMonth, int currentYear);
    
    List<List<Object>> getScoreStatsForQuestion(Long entityId, String entityType, int currentMonth, int currentYear);
    
    public Map<Integer, Digest> getDigestDataForLastFourMonths(long companyId, int monthUnderConcern, int year) throws InvalidInputException, NoRecordsFetchedException;

    public MonthlyDigestAggregate prepareMonthlyDigestMailData( long companyId, String companyName, int monthUnderConcern, int year, String recipientMail ) throws InvalidInputException, NoRecordsFetchedException, UndeliveredEmailException;

    public List<UserRankingPastMonthMain> getTopTenUserRankingsThisMonthForACompany( long companyId, int monthUnderConcern, int year ) throws InvalidInputException;

    public void startMonthlyDigestProcess();

    public boolean updateSendDigestMailToggle( long companyId, boolean sendMonthlyDigestMail ) throws InvalidInputException;

    public List<CompanyDigestRequestData> getCompaniesOptedForDigestMail( int startIndex, int batchSize );

    /**
     * method to get maximum question number for company
     * based on time frame
     * @param entityId
     * @param startDate
     * @param endDate
     * @return
     */
    public int getMaxQuestionForSurveyResultsReport(String entityType, Long entityId, Timestamp startDate, Timestamp endDate );

    /**
     * This is the service class method for SurveyResultsReport.
     * This method calls the respective DAO for each report and returns a generic class.
     * @param entityType
     * @param entityId
     * @param startDate
     * @param endDate
     * @param startIndex
     * @param batchSize
     * @return
     */
    public Map<String, SurveyResultsReportVO> getSurveyResultsReport( String entityType, Long entityId, Timestamp startDate,
        Timestamp endDate, int startIndex, int batchSize );

	/**
	 * This method validates the entityType and entityId for social survey admin
	 * and then generates the Company Details Report.
	 * 
	 * @param entityId
	 * @param startIndex
	 * @param batchSize
	 * @return List of CompanyDetailsReport.
	 */
	public List<CompanyDetailsReport> getCompanyDetailsReport(Long entityId, int startIndex, int batchSize);

	/**
	 * @param profileValue
	 * @param profileLevel
	 * @return
	 */
	public String generateCompanyDetailsReport(long profileValue, String profileLevel)throws UnsupportedEncodingException, NonFatalException;
	
    /**
     * 
     * @param entityId
     * @param entityType
     * @param startDate
     * @param endDate
     * @param startIndex
     * @param batchSize
     * @return
     * @throws InvalidInputException
     */
    public List<ReportingSurveyPreInititation> getIncompleteSurvey( long entityId, String entityType, Date startDate, Date endDate,
        int startIndex, int batchSize ) throws InvalidInputException;

   /**
     * Method to generate incomplete survey results report.
     * @param profileValue
     * @param profileLevel
     * @param adminUserId
     * @param startDate
     * @param endDate
     * @return
     */
	public String generateIncompleteSurveyResultsReport(Long entityId, String entityType, Long userId,
			Timestamp startDate, Timestamp endDate)
			throws UnsupportedEncodingException, NonFatalException, ParseException;

	/**
	 * This method returns the latest record for account statistics report in file upload.
	 * @param reportId
	 * @return
	 */
	public Object getAccountStatisticsRecentActivity(Long reportId);
}
