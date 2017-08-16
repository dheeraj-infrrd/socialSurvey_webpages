package com.realtech.socialsurvey.core.dao;

import java.util.List;

import com.realtech.socialsurvey.core.entities.UserRankingThisMonthRegion;

public interface UserRankingThisMonthRegionDao extends GenericReportingDao<UserRankingThisMonthRegion, String>{
	List<UserRankingThisMonthRegion> fetchUserRankingForThisMonthRegion(Long regionId, int month, int year , int startIndex , int batchSize);
	
	int fetchUserRankingRankForThisMonthRegion( Long userId, Long regionId, int year );

    long fetchUserRankingCountForThisMonthRegion( Long regionId, int year, int month );
}