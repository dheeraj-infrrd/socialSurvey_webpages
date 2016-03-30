package com.realtech.socialsurvey.core.services.upload.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realtech.socialsurvey.core.entities.BranchUploadVO;
import com.realtech.socialsurvey.core.entities.HierarchyUpload;
import com.realtech.socialsurvey.core.entities.RegionUploadVO;
import com.realtech.socialsurvey.core.entities.UploadValidation;
import com.realtech.socialsurvey.core.entities.UserUploadVO;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.exception.NoRecordsFetchedException;
import com.realtech.socialsurvey.core.services.organizationmanagement.UserManagementService;


public class UploadValidationServiceImplTest
{
    private static Logger LOG = LoggerFactory.getLogger( UploadValidationServiceImplTest.class );

    @Spy
    @InjectMocks
    private UploadValidationServiceImpl uploadValidationServiceImpl;
    
    @Mock
    private UserManagementService userManagementService;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {}


    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {}


    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }


    @After
    public void tearDown() throws Exception
    {}


    @Test
    public void testRegionValidationsWithNoErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        upload.setRegions( regions );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateRegions( validation, null );
        Assert.assertEquals( 0, validation.getRegionValidationErrors().size() );
        LOG.info( "Errors: " + validation.getRegionValidationErrors() );
    }


    @Test
    public void testRegionValidationsWithErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( null, null, null, null, null, null, null, null, 1 ) );
        regions.add( getRegion( "ABC", null, "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        regions.add( getRegion( null, "acc", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 3 ) );
        upload.setRegions( regions );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateRegions( validation, null );
        Assert.assertEquals( 4, validation.getRegionValidationErrors().size() );
        LOG.info( "Errors: " + validation.getRegionValidationErrors() );
    }


    @Test
    public void testBranchValidationsWithNoErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        upload.setRegions( regions );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateBranches( validation, null );
        LOG.info( "Errors: " + validation.getBranchValidationErrors() );
        LOG.info( "Warnings: " + validation.getBranchValidationWarnings() );
        Assert.assertEquals( 0, validation.getBranchValidationErrors().size() );
        Assert.assertEquals( 0, validation.getBranchValidationWarnings().size() );
    }


    @Test
    public void testBranchValidationsWithErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( null, null, null, null, null, null, null, 1 ) );
        branches.add( getBranch( "ABC", "ABC", "ABC", null, "ABC", "KA", "123456", 2 ) );
        branches.add( getBranch( "ABC", "ABC", "sdf", "assa sdvsd", "ABC", "KA", "123456", 3 ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        upload.setRegions( regions );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateBranches( validation, null );
        LOG.info( "Errors: " + validation.getBranchValidationErrors() );
        LOG.info( "Warnings: " + validation.getBranchValidationWarnings() );
        Assert.assertEquals( 6, validation.getBranchValidationErrors().size() );
        Assert.assertEquals( 1, validation.getBranchValidationWarnings().size() );
    }


    @Test
    public void testUserValidationsWithNoErrors() throws InvalidInputException, NoRecordsFetchedException
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1 ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        /*Mockito.when( userManagementService.getUserByEmailAddress( Mockito.anyString() ) ).thenThrow(
            NoRecordsFetchedException.class );*/
        uploadValidationServiceImpl.validateUsers( validation, null );
        LOG.info( "Errors: " + validation.getUserValidationErrors() );
        LOG.info( "Warnings: " + validation.getUserValidationWarnings() );
        Assert.assertEquals( 0, validation.getUserValidationErrors().size() );
        Assert.assertEquals( 0, validation.getUserValidationWarnings().size() );
    }


    @Test
    public void testUserValidationsWithErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "dcd" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "dcds" );
        users.add( getUser( null, null, null, null, null, null, null, 1 ) );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 2 ) );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "xdc", "cdf", branchAdmins, regionAdmins, 3 ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateUsers( validation, null );
        LOG.info( "Errors: " + validation.getUserValidationErrors() );
        LOG.info( "Warnings: " + validation.getUserValidationWarnings() );
        Assert.assertEquals( 9, validation.getUserValidationErrors().size() );
        Assert.assertEquals( 1, validation.getUserValidationWarnings().size() );
    }


    @SuppressWarnings ( "unchecked")
    @Test
    public void testDeletedRegionValidationsWithActiveBranchAndUsers() throws InvalidInputException, NoRecordsFetchedException
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1 ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 2 ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1, true ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1 ) );

        upload.setUsers( users );
        validation.setUpload( upload );
        Mockito.when( userManagementService.getUserByEmailAddress( Mockito.anyString() ) ).thenThrow(
            NoRecordsFetchedException.class );
        uploadValidationServiceImpl.validateHeirarchyUpload( validation, null, null, null );
        Assert.assertEquals( 1, validation.getRegionValidationErrors().size() );
        Assert.assertEquals( 0, validation.getNumberOfRegionsDeleted() );
        LOG.info( "Errors: " + validation.getRegionValidationErrors() );
    }


    @Test
    public void testDeletedRegionValidationsWithOnlyActiveUsers()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1, true ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 2, true ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1, true ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1 ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateHeirarchyUpload( validation, null, null, null );
        Assert.assertEquals( 1, validation.getRegionValidationErrors().size() );
        Assert.assertEquals( 0, validation.getNumberOfRegionsDeleted() );
        LOG.info( "Errors: " + validation.getRegionValidationErrors() );
    }


    @Test
    public void testDeletedRegionValidationsWithNoErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1, true ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 2, true ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1, true ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        regions.add( getRegion( "DEF1", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 3, true ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1, true ) );
        users.add( getUser( "XYZ1", "cdcdvd", "asncj@dmck.com", null, null, null, null, 2, true ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateHeirarchyUpload( validation, null, null, null );
        Assert.assertEquals( 0, validation.getRegionValidationErrors().size() );
        LOG.info( "Errors: " + validation.getRegionValidationErrors() );
    }


    @Test
    public void testDeletedBranchValidationsWithActiveUsers()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1, true ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 2, true ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1, true ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1 ) );
        users.add( getUser( "XYZ1", "cdcdvd", "asncj@dmck.com", "DEF", "ABC", branchAdmins, regionAdmins, 2 ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateHeirarchyUpload( validation, null, null, null );
        Assert.assertEquals( 2, validation.getBranchValidationErrors().size() );
        Assert.assertEquals( 0, validation.getNumberOfBranchesDeleted() );
        LOG.info( "Errors: " + validation.getBranchValidationErrors() );
    }


    @Test
    public void testDeletedBranchValidationsWithNoErrors()
    {
        UploadValidation validation = new UploadValidation();
        HierarchyUpload upload = new HierarchyUpload();
        List<BranchUploadVO> branches = new ArrayList<BranchUploadVO>();
        branches.add( getBranch( "ABC", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 1, true ) );
        branches.add( getBranch( "DEF", "abcdefh", "ABC", "assa sdvsd", "Bangalore", "KA", "123456", 2, true ) );
        branches.add( getBranch( "DEF1", "abcdefh", "", "assa sdvsd", "Bangalore", "KA", "123456", 3, true ) );
        upload.setBranches( branches );
        List<RegionUploadVO> regions = new ArrayList<RegionUploadVO>();
        regions.add( getRegion( "ABC", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 1, true ) );
        regions.add( getRegion( "DEF", "abcdefh", "12 sdvdv", "12 sdvdv", "Bangalore", "India", "KA", "123456", 2 ) );
        upload.setRegions( regions );
        List<UserUploadVO> users = new ArrayList<UserUploadVO>();
        List<String> branchAdmins = new ArrayList<String>();
        branchAdmins.add( "ABC" );
        branchAdmins.add( "DEF" );
        List<String> regionAdmins = new ArrayList<String>();
        regionAdmins.add( "ABC" );
        regionAdmins.add( "DEF" );
        users.add( getUser( "XYZ", "cdcdvd", "asncj@dmck.com", "ABC", "ABC", branchAdmins, regionAdmins, 1, true ) );
        users.add( getUser( "XYZ1", "cdcdvd", "asncj@dmck.com", null, null, null, null, 2, true ) );
        upload.setUsers( users );
        validation.setUpload( upload );
        uploadValidationServiceImpl.validateHeirarchyUpload( validation, null, null, null );
        Assert.assertEquals( 0, validation.getBranchValidationErrors().size() );
        LOG.info( "Errors: " + validation.getBranchValidationErrors() );
    }


    private UserUploadVO getUser( String userId, String name, String email, String regionId, String branchId,
        List<String> regionAdmin, List<String> branchAdmin, int rowNum )
    {
        UserUploadVO user = new UserUploadVO();
        user.setSourceUserId( userId );
        user.setFirstName( name );
        user.setEmailId( email );
        user.setSourceRegionId( regionId );
        if ( regionId != null && !regionId.isEmpty() ) {
            List<String> assignedRegions = new ArrayList<String>();
            assignedRegions.add( regionId );
            user.setAssignedRegions( assignedRegions );
        }
        user.setSourceBranchId( branchId );
        if ( branchId != null && !branchId.isEmpty() ) {
            List<String> assignedBranches = new ArrayList<String>();
            assignedBranches.add( branchId );
            user.setAssignedBranches( assignedBranches );
        }
        user.setAssignedBranchesAdmin( branchAdmin );
        user.setAssignedRegionsAdmin( regionAdmin );
        user.setRowNum( rowNum );
        return user;
    }


    private UserUploadVO getUser( String userId, String name, String email, String regionId, String branchId,
        List<String> regionAdmin, List<String> branchAdmin, int rowNum, boolean isDeletedRecord )
    {
        UserUploadVO user = getUser( userId, name, email, regionId, branchId, regionAdmin, branchAdmin, rowNum );
        user.setDeletedRecord( isDeletedRecord );
        return user;
    }


    private BranchUploadVO getBranch( String branchId, String name, String regionId, String address, String city, String state,
        String zip, int rowNum )
    {
        BranchUploadVO branch = new BranchUploadVO();
        branch.setSourceBranchId( branchId );
        branch.setBranchName( name );
        branch.setBranchAddress1( address );
        branch.setBranchCity( city );
        branch.setBranchState( state );
        branch.setBranchZipcode( zip );
        branch.setRowNum( rowNum );
        branch.setSourceRegionId( regionId );
        return branch;
    }


    private BranchUploadVO getBranch( String branchId, String name, String regionId, String address, String city, String state,
        String zip, int rowNum, boolean isDeletedRecord )
    {
        BranchUploadVO branch = getBranch( branchId, name, regionId, address, city, state, zip, rowNum );
        branch.setDeletedRecord( isDeletedRecord );
        return branch;
    }


    private RegionUploadVO getRegion( String regionId, String name, String address1, String address2, String city,
        String country, String state, String zip, int rowNum )
    {
        RegionUploadVO region = new RegionUploadVO();
        region.setSourceRegionId( regionId );
        region.setRegionName( name );
        region.setRegionAddress1( address1 );
        region.setRegionAddress2( address2 );
        region.setRegionCity( city );
        region.setRegionCountry( country );
        region.setRegionState( state );
        region.setRegionZipcode( zip );
        region.setRowNum( rowNum );
        return region;
    }


    private RegionUploadVO getRegion( String regionId, String name, String address1, String address2, String city,
        String country, String state, String zip, int rowNum, boolean isDeletedRecord )
    {
        RegionUploadVO region = getRegion( regionId, name, address1, address2, city, country, state, zip, rowNum );
        region.setDeletedRecord( isDeletedRecord );
        return region;
    }
}
