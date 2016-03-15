package com.realtech.socialsurvey.core.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.HierarchyUploadDao;
import com.realtech.socialsurvey.core.entities.HierarchyUpload;
import com.realtech.socialsurvey.core.exception.InvalidInputException;


@Repository
public class MongoHierarchyUploadDaoImpl implements HierarchyUploadDao
{

    private static final Logger LOG = LoggerFactory.getLogger( MongoHierarchyUploadDaoImpl.class );

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * Method to save hierarchy upload in mongo
     * @param hierarchyUpload
     * @throws InvalidInputException 
     */
    @Override
    public void saveHierarchyUploadObject( HierarchyUpload hierarchyUpload ) throws InvalidInputException
    {
        LOG.info( "Method to save hierarchy upload object started" );
        saveHierarchyUpload( hierarchyUpload, CommonConstants.HIERARCHY_UPLOAD_COLLECTION );
        LOG.info( "Method to save hierarchy upload object finished" );
    }


    /**
     * Method to fetch hierarchy upload for company
     * @param companyId
     * @return
     * @throws InvalidInputException
     */
    @Override
    public HierarchyUpload getHierarchyUploadByCompany( long companyId ) throws InvalidInputException
    {
        LOG.info( "Method to get hierarchy upload for companyId : " + companyId + " started" );
        //Fetch from mongo
        HierarchyUpload hierarchyUpload = fetchHierarchyUploadFromCollection( companyId,
            CommonConstants.HIERARCHY_UPLOAD_COLLECTION );
        LOG.info( "Method to get hierarchy upload for companyId : " + companyId + " finished" );
        return hierarchyUpload;
    }


    /**
     * Method to save hierarchy upload in UPLOAD_HIERARCHY_DETAILS collection
     * @param hierarchyUpload
     * @throws InvalidInputException
     */
    @Override
    public void saveUploadHierarchyDetails( HierarchyUpload hierarchyUpload ) throws InvalidInputException
    {
        LOG.info( "Method to save hierarchy upload object into UPLOAD_HIERARCHY_DETAILS started" );
        saveHierarchyUpload( hierarchyUpload, CommonConstants.UPLOAD_HIERARCHY_DETAILS_COLLECTION );
        LOG.info( "Method to save hierarchy upload object into UPLOAD_HIERARCHY_DETAILS finished" );
    }


    /**
     * Method to get hieararchy upload object from UPLOAD_HIERARCHY_DETAILS collection
     * @param companyId
     * @return
     * @throws InvalidInputException
     */
    @Override
    public HierarchyUpload getUploadHierarchyDetailsByCompany( long companyId ) throws InvalidInputException
    {
        LOG.info( "Method to get hierarchy upload for companyId : " + companyId + " from UPLOAD_HIERARCHY_DETAILS started" );
        //Fetch from mongo
        HierarchyUpload hierarchyUpload = fetchHierarchyUploadFromCollection( companyId,
            CommonConstants.UPLOAD_HIERARCHY_DETAILS_COLLECTION );
        LOG.info( "Method to get hierarchy upload for companyId : " + companyId + " from UPLOAD_HIERARCHY_DETAILS finished" );
        return hierarchyUpload;
    }


    /**
     * Method to save hierarchyupload object into a specific collection
     * @param hierarchyUpload
     * @param collectionName
     * @throws InvalidInputException
     */
    void saveHierarchyUpload( HierarchyUpload hierarchyUpload, String collectionName ) throws InvalidInputException
    {
        if ( hierarchyUpload == null ) {
            LOG.error( "The hierarchy upload object is null" );
            throw new InvalidInputException( "The hierarchy upload object is null" );
        }

        //Delete previous instance
        Query query = new Query();
        query.addCriteria( Criteria.where( CommonConstants.COMPANY_ID_COLUMN ).is( hierarchyUpload.getCompanyId() ) );
        mongoTemplate.remove( query, HierarchyUpload.class, collectionName );
        mongoTemplate.insert( hierarchyUpload, collectionName );
    }


    /**
     * Method to fetch hierarchy upload object from a specific collection
     * @param companyId
     * @param collectionName
     * @return
     * @throws InvalidInputException
     */
    HierarchyUpload fetchHierarchyUploadFromCollection( long companyId, String collectionName ) throws InvalidInputException
    {
        //Invalid check
        if ( companyId <= 0l ) {
            throw new InvalidInputException( "Invalid CompanyId : " + companyId );
        }
        Query query = new Query();
        query.addCriteria( Criteria.where( CommonConstants.COMPANY_ID_COLUMN ).is( companyId ) );
        //Fetch from mongo
        HierarchyUpload hierarchyUpload = mongoTemplate.findOne( query, HierarchyUpload.class, collectionName );
        return hierarchyUpload;
    }
}