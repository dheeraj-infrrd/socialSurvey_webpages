package com.realtech.socialsurvey.core.services.admin.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.braintreegateway.ResourceCollection;
import com.braintreegateway.StatusEvent;
import com.braintreegateway.Subscription;
import com.braintreegateway.Transaction;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.dao.CompanyDao;
import com.realtech.socialsurvey.core.entities.Company;
import com.realtech.socialsurvey.core.entities.LicenseDetail;
import com.realtech.socialsurvey.core.entities.User;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.services.admin.AdminService;
import com.realtech.socialsurvey.core.services.mail.EmailServices;
import com.realtech.socialsurvey.core.services.mail.UndeliveredEmailException;
import com.realtech.socialsurvey.core.services.organizationmanagement.UserManagementService;
import com.realtech.socialsurvey.core.services.payment.Payment;
import com.realtech.socialsurvey.core.vo.SubscriptionVO;
import com.realtech.socialsurvey.core.vo.TransactionVO;


/**
 * 
 * @author rohit
 *
 */
@Component
public class AdminServiceImpl implements AdminService
{

    private static final Logger LOG = LoggerFactory.getLogger( AdminServiceImpl.class );

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private Payment payment;

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private UserManagementService userManagementService;

    @Value ( "${FILE_DIRECTORY_LOCATION}")
    private String fileDirectoryLocation;

    @Value ( "${APPLICATION_ADMIN_EMAIL}")
    private String adminEmailId;

    @Value ( "${APPLICATION_ADMIN_NAME}")
    private String adminName;


    @SuppressWarnings ( "deprecation")
    @Override
    @Transactional
    public SubscriptionVO getSubscriptionVOBySubscriptionId( String subscriptionId ) throws InvalidInputException
    {

        LOG.info( "methodd getSubscriptionVOBySubscriptionId started" );
        SubscriptionVO subscriptionVO = new SubscriptionVO();
        Subscription subscription = payment.getSubscriptionDetailFromBrainTree( subscriptionId );

        if ( subscription == null ) {
            return subscriptionVO;
        }

        subscriptionVO.setId( subscription.getId() );
        subscriptionVO.setBalance( subscription.getBalance() );
        subscriptionVO.setBillingDayOfMonth( subscription.getBillingDayOfMonth() );
        if ( subscription.getBillingPeriodEndDate() != null )
            subscriptionVO.setBillingPeriodEndDate( subscription.getBillingPeriodEndDate().getTime().toLocaleString() );
        if ( subscription.getBillingPeriodStartDate() != null )
            subscriptionVO.setBillingPeriodStartDate( subscription.getBillingPeriodStartDate().getTime().toLocaleString() );
        subscriptionVO.setCurrentBillingCycle( subscription.getCurrentBillingCycle() );
        if ( subscription.getCreatedAt() != null )
            subscriptionVO.setCreatedAt( subscription.getCreatedAt().getTime().toLocaleString() );
        if ( subscription.getUpdatedAt() != null )
            subscriptionVO.setUpdatedAt( subscription.getUpdatedAt().getTime().toLocaleString() );
        if ( subscription.getFirstBillingDate() != null )
            subscriptionVO.setFirstBillingDate( subscription.getFirstBillingDate().getTime().toLocaleString() );
        subscriptionVO.setNextBillAmount( subscription.getNextBillAmount() );
        if ( subscription.getNextBillingDate() != null )
            subscriptionVO.setNextBillingDate( subscription.getNextBillingDate().getTime().toLocaleString() );
        subscriptionVO.setNextBillingPeriodAmount( subscription.getNextBillingPeriodAmount() );

        Company company = companyDao.getCompanyByBraintreeSubscriptionId( subscription.getId() );
        if ( company != null ) {
            subscriptionVO.setCompanyId( company.getCompanyId() );
            subscriptionVO.setCompanyName( company.getCompany() );
            User user = userManagementService.getCompanyAdmin( company.getCompanyId() );
            if ( user != null ) {
                subscriptionVO.setCompanyAdminId( user.getUserId() );
                subscriptionVO.setCompanyAdminFirstName( user.getFirstName() );
                subscriptionVO.setCompanyAdminLastName( user.getLastName() );
            }

        }

        return subscriptionVO;
    }


    @Override
    @Transactional
    public List<TransactionVO> getTransactionListBySubscriptionIs( String subscriptionId ) throws InvalidInputException
    {
        LOG.info( "Method getTransactionListBySubscriptionIs started for subscriptionId : " + subscriptionId );
        List<TransactionVO> transactionVOs = new ArrayList<TransactionVO>();
        List<Transaction> transactions = payment.getTransactionListFromBrainTree( subscriptionId );
        if ( transactions == null ) {
            return transactionVOs;
        }
        for ( Transaction transaction : transactions ) {
            TransactionVO transactionVO = new TransactionVO();

            transactionVO.setId( transaction.getId() );
            transactionVO.setAmount( transaction.getAmount() );
            transactionVO.setCreatedAt( transaction.getCreatedAt().getTime().toLocaleString() );
            transactionVO.setCreditCard( transaction.getCreditCard() );
            transactionVO.setStatus( transaction.getStatus() );
            transactionVO.setStatusHistory( transaction.getStatusHistory() );
            transactionVO.setSubscriptionId( transaction.getSubscriptionId() );

            Company company = companyDao.getCompanyByBraintreeSubscriptionId( transaction.getSubscriptionId() );
            if ( company != null ) {
                transactionVO.setCompanyId( company.getCompanyId() );
                transactionVO.setCompanyName( company.getCompany() );
                User user = userManagementService.getCompanyAdmin( company.getCompanyId() );
                if ( user != null ) {
                    transactionVO.setCompanyAdminId( user.getUserId() );
                    transactionVO.setCompanyAdminFirstName( user.getFirstName() );
                    transactionVO.setCompanyAdminLastName( user.getLastName() );
                }
            }

            transactionVOs.add( transactionVO );

        }
        LOG.info( "Method getTransactionListBySubscriptionIs ended " );
        return transactionVOs;
    }


    @Override
    public boolean generateTransactionListExcelAndMail( List<TransactionVO> transactionVOs, List<String> recipientMailIds,
        String subscriptionId )
    {
        LOG.info( "method generateTransactionListExcelAndMail started" );
        // Iterate over data and write to sheet

        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet();

        int rownum = 0;
        int cellnum = 0;
        Row row = sheet.createRow( rownum++ );
        Cell cell1 = row.createCell( cellnum++ );
        cell1.setCellValue( "Transaction Id" );
        Cell cell2 = row.createCell( cellnum++ );
        cell2.setCellValue( "Transaction Amount" );
        Cell cell3 = row.createCell( cellnum++ );
        cell3.setCellValue( "Transaction CreatedAt" );
        Cell cell4 = row.createCell( cellnum++ );
        cell4.setCellValue( "Transaction Status" );
        Cell cell5 = row.createCell( cellnum++ );
        cell5.setCellValue( "Transaction Time" );
        Cell cell6 = row.createCell( cellnum++ );
        cell6.setCellValue( "Transaction Subscription Id" );
        Cell cell7 = row.createCell( cellnum++ );
        cell7.setCellValue( "Transaction Company Id" );
        Cell cell8 = row.createCell( cellnum++ );
        cell8.setCellValue( "Transaction Company Name" );
        Cell cell9 = row.createCell( cellnum++ );
        cell9.setCellValue( "Transaction Company Admin Name" );

        for ( TransactionVO transactionVO : transactionVOs ) {

            row = sheet.createRow( rownum++ );
            cellnum = 0;
            Cell cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getId() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getAmount().doubleValue() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getCreatedAt() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getStatus().toString() );

            cell = row.createCell( cellnum++ );
            for ( StatusEvent curStatusEcent : transactionVO.getStatusHistory() ) {
                if ( curStatusEcent.getStatus() == transactionVO.getStatus() )
                    cell.setCellValue( curStatusEcent.getTimestamp().getTime().toLocaleString() );
            }

            cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getSubscriptionId() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( transactionVO.getCompanyId() );

            cell = row.createCell( cellnum++ );
            if ( transactionVO.getCompanyName() != null )
                cell.setCellValue( transactionVO.getCompanyName() );

            cell = row.createCell( cellnum++ );
            if ( transactionVO.getCompanyAdminFirstName() != null )
                cell.setCellValue( transactionVO.getCompanyAdminFirstName()
                    + ( transactionVO.getCompanyAdminLastName() != null ? " " + transactionVO.getCompanyAdminLastName() : "" ) );
        }
        // Create file and write report into it
        boolean excelCreated = false;
        String fileName = "TransactionDetails-" + ( new Timestamp( new Date().getTime() ) );
        FileOutputStream fileOutput = null;
        InputStream inputStream = null;
        File file = null;
        String filePath = null;
        try {
            file = new File( fileDirectoryLocation + File.separator + fileName + ".xls" );
            fileOutput = new FileOutputStream( file );
            file.createNewFile();
            workbook.write( fileOutput );
            filePath = file.getPath();
            excelCreated = true;
        } catch ( FileNotFoundException fe ) {
            LOG.error( "Exception caught while generating billing report " + fe.getMessage() );
            excelCreated = false;
        } catch ( IOException e ) {
            LOG.error( "Exception caught  while generating billing report " + e.getMessage() );
            excelCreated = false;
        } finally {
            try {
                fileOutput.close();
                if ( inputStream != null ) {
                    inputStream.close();
                }
            } catch ( IOException e ) {
                LOG.error( "Exception caught  while generating billing report " + e.getMessage() );
                excelCreated = false;
            }
        }

        // Mail the report to the email
        if ( excelCreated ) {
            Map<String, String> attachmentsDetails = new HashMap<String, String>();
            attachmentsDetails.put( fileName + ".xls", filePath );

            String name = adminName;
            LOG.debug( "sending mail to : " + name + " at : " + recipientMailIds );
            try {
                emailServices.sendCustomReportMail( CommonConstants.ADMIN_RECEPIENT_DISPLAY_NAME, recipientMailIds,
                    CommonConstants.TRANSACTION_LIST_MAIL_SUBJECT + subscriptionId, attachmentsDetails );
            } catch ( InvalidInputException | UndeliveredEmailException e ) {
                LOG.error( "Error while sending mail to ; " + recipientMailIds, e );
            }

        }

        LOG.info( "method generateTransactionListExcelAndMail ended" );
        return excelCreated;
    }


    @SuppressWarnings ( "deprecation")
    @Override
    @Transactional
    public List<SubscriptionVO> getActiveSubscriptionsList() throws InvalidInputException
    {
        LOG.info( "Method getActiveSubscriptionsList started " );
        List<SubscriptionVO> subscriptionVOs = new ArrayList<SubscriptionVO>();
        ResourceCollection<Subscription> collection = payment.getActiveSubscriptionsListFromBrainTree();
        if ( collection == null ) {
            return subscriptionVOs;
        }
        for ( Subscription subscription : collection ) {

            SubscriptionVO subscriptionVO = new SubscriptionVO();

            try {
                subscriptionVO.setId( subscription.getId() );
                subscriptionVO.setBalance( subscription.getBalance() );
                subscriptionVO.setBillingDayOfMonth( subscription.getBillingDayOfMonth() );
                if ( subscription.getBillingPeriodEndDate() != null )
                    subscriptionVO.setBillingPeriodEndDate( subscription.getBillingPeriodEndDate().getTime().toLocaleString() );
                if ( subscription.getBillingPeriodStartDate() != null )
                    subscriptionVO.setBillingPeriodStartDate( subscription.getBillingPeriodStartDate().getTime()
                        .toLocaleString() );
                subscriptionVO.setCurrentBillingCycle( subscription.getCurrentBillingCycle() );
                if ( subscription.getCreatedAt() != null )
                    subscriptionVO.setCreatedAt( subscription.getCreatedAt().getTime().toLocaleString() );
                if ( subscription.getUpdatedAt() != null )
                    subscriptionVO.setUpdatedAt( subscription.getUpdatedAt().getTime().toLocaleString() );
                if ( subscription.getFirstBillingDate() != null )
                    subscriptionVO.setFirstBillingDate( subscription.getFirstBillingDate().getTime().toLocaleString() );
                subscriptionVO.getNextBillAmount();
                subscriptionVO.setNextBillAmount( subscription.getNextBillAmount() );
                if ( subscription.getNextBillingDate() != null )
                    subscriptionVO.setNextBillingDate( subscription.getNextBillingDate().getTime().toLocaleString() );
                subscriptionVO.setNextBillingPeriodAmount( subscription.getNextBillingPeriodAmount() );

                Company company = companyDao.getCompanyByBraintreeSubscriptionId( subscription.getId() );
                if ( company != null ) {
                    subscriptionVO.setCompanyId( company.getCompanyId() );
                    subscriptionVO.setCompanyName( company.getCompany() );
                    User user = userManagementService.getCompanyAdmin( company.getCompanyId() );
                    if ( user != null ) {
                        subscriptionVO.setCompanyAdminId( user.getUserId() );
                        subscriptionVO.setCompanyAdminFirstName( user.getFirstName() );
                        subscriptionVO.setCompanyAdminLastName( user.getLastName() );
                    }

                }

                subscriptionVOs.add( subscriptionVO );
            } catch ( Exception e ) {
                LOG.error( "Error while parsing rescord with Susbcruption Id :" + subscriptionVO.getId(), e );
                LOG.error( "Stack trace is :" + e.getStackTrace().toString() );
            }
        }

        LOG.info( "Method getActiveSubscriptionsList ended " );
        return subscriptionVOs;
    }


    @Override
    public boolean generateSubscriptionListExcelAndMail( List<SubscriptionVO> subscriptionVOs, List<String> recipientMailIds )
    {
        LOG.info( "method generateSubscriptionListExcelAndMail started" );
        // Iterate over data and write to sheet

        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet();

        int rownum = 0;
        int cellnum = 0;
        Row row = sheet.createRow( rownum++ );
        Cell cell1 = row.createCell( cellnum++ );
        cell1.setCellValue( "Subscription Id" );
        Cell cell2 = row.createCell( cellnum++ );
        cell2.setCellValue( "Subscription Balance" );
        Cell cell3 = row.createCell( cellnum++ );
        cell3.setCellValue( "Subscription CreatedAt" );
        Cell cell4 = row.createCell( cellnum++ );
        cell4.setCellValue( "Subscription UpdatedAt" );
        Cell cell5 = row.createCell( cellnum++ );
        cell5.setCellValue( "Subscription First Billing Date" );
        Cell cell6 = row.createCell( cellnum++ );
        cell6.setCellValue( "Subscription Current Billing Cycle" );
        Cell cell7 = row.createCell( cellnum++ );
        cell7.setCellValue( "Subscription Next Bill Amount" );
        Cell cell8 = row.createCell( cellnum++ );
        cell8.setCellValue( "Subscription Next Billing Date" );
        Cell cell9 = row.createCell( cellnum++ );
        cell9.setCellValue( "Subscription Next Billing Period Amount" );
        Cell cell10 = row.createCell( cellnum++ );
        cell10.setCellValue( "Subscription Company Id" );
        Cell cell11 = row.createCell( cellnum++ );
        cell11.setCellValue( "Subscription Company Name" );
        Cell cell12 = row.createCell( cellnum++ );
        cell12.setCellValue( "Subscription Company Admin Name" );

        for ( SubscriptionVO subscriptionVO : subscriptionVOs ) {

            row = sheet.createRow( rownum++ );
            cellnum = 0;
            Cell cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getId() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getBalance().doubleValue() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getCreatedAt() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getUpdatedAt() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getFirstBillingDate() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getCurrentBillingCycle() );
            //
            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getNextBillAmount().doubleValue() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getNextBillingDate() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getNextBillingPeriodAmount().doubleValue() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( subscriptionVO.getCompanyId() );

            cell = row.createCell( cellnum++ );
            if ( subscriptionVO.getCompanyName() != null )
                cell.setCellValue( subscriptionVO.getCompanyName() );

            cell = row.createCell( cellnum++ );
            if ( subscriptionVO.getCompanyAdminFirstName() != null )
                cell.setCellValue( subscriptionVO.getCompanyAdminFirstName()
                    + ( subscriptionVO.getCompanyAdminLastName() != null ? " " + subscriptionVO.getCompanyAdminLastName() : "" ) );
        }
        // Create file and write report into it
        boolean excelCreated = false;
        String fileName = "Subscription_Lists-" + ( new Timestamp( new Date().getTime() ) );
        FileOutputStream fileOutput = null;
        InputStream inputStream = null;
        File file = null;
        String filePath = null;
        try {
            file = new File( fileDirectoryLocation + File.separator + fileName + ".xls" );
            fileOutput = new FileOutputStream( file );
            file.createNewFile();
            workbook.write( fileOutput );
            filePath = file.getPath();
            excelCreated = true;
        } catch ( FileNotFoundException fe ) {
            LOG.error( "Exception caught while generating billing report " + fe.getMessage() );
            excelCreated = false;
        } catch ( IOException e ) {
            LOG.error( "Exception caught  while generating billing report " + e.getMessage() );
            excelCreated = false;
        } finally {
            try {
                fileOutput.close();
                if ( inputStream != null ) {
                    inputStream.close();
                }
            } catch ( IOException e ) {
                LOG.error( "Exception caught  while generating billing report " + e.getMessage() );
                excelCreated = false;
            }
        }

        // Mail the report to the email
        if ( excelCreated ) {
            Map<String, String> attachmentsDetails = new HashMap<String, String>();
            attachmentsDetails.put( fileName + ".xls", filePath );

            String name = adminName;
            LOG.debug( "sending mail to : " + name + " at : " + recipientMailIds );
            try {
                emailServices.sendCustomReportMail( CommonConstants.ADMIN_RECEPIENT_DISPLAY_NAME, recipientMailIds,
                    CommonConstants.ACTIVE_SUBSCRIPTION_MAIL_SUBJECT, attachmentsDetails );
            } catch ( InvalidInputException | UndeliveredEmailException e ) {
                LOG.error( "Error while sending mail to ; " + recipientMailIds, e );
            }

        }

        LOG.info( "method generateTransactionListExcelAndMail ended" );
        return excelCreated;
    }


    @Override
    @Transactional
    public List<Company> getAllAutoBillingModeCompanies()
    {
        LOG.info( "Method getAllAutoBillingModeCompanies started " );
        List<Company> CompanyList = companyDao.getCompaniesByBillingModeAuto();
        LOG.info( "Method getAllAutoBillingModeCompanies ended " );
        return CompanyList;
    }


    @Override
    public boolean generateAutoBillingCompanyListExcelAndMail( List<Company> CompanyList, List<String> recipientMailIds )
    {
        LOG.info( "method generateAutoBillingCompanyListExcelAndMail started" );
        // Iterate over data and write to sheet

        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet();

        int rownum = 0;
        int cellnum = 0;
        Row row = sheet.createRow( rownum++ );
        Cell cell1 = row.createCell( cellnum++ );
        cell1.setCellValue( "Company Id" );
        Cell cell2 = row.createCell( cellnum++ );
        cell2.setCellValue( "Company Name" );
        Cell cell3 = row.createCell( cellnum++ );
        cell3.setCellValue( "Company Created On" );
        Cell cell4 = row.createCell( cellnum++ );
        cell4.setCellValue( "Subscription id" );
        Cell cell5 = row.createCell( cellnum++ );
        cell5.setCellValue( "Subscription Start Date" );
        Cell cell6 = row.createCell( cellnum++ );
        cell6.setCellValue( "Subscription Payment Retries" );

        for ( Company company : CompanyList ) {

            row = sheet.createRow( rownum++ );
            cellnum = 0;
            Cell cell = row.createCell( cellnum++ );
            cell.setCellValue( company.getCompanyId() );

            cell = row.createCell( cellnum++ );
            cell.setCellValue( company.getCompany() );

            cell = row.createCell( cellnum++ );
            if ( company.getCreatedOn() != null )
                cell.setCellValue( company.getCreatedOn().toLocaleString() );

            if ( company.getLicenseDetails() != null && !company.getLicenseDetails().isEmpty() ) {
                LicenseDetail licenseDetail = company.getLicenseDetails().get( 0 );
                if ( licenseDetail != null ) {
                    cell = row.createCell( cellnum++ );
                    cell.setCellValue( licenseDetail.getSubscriptionId() );

                    cell = row.createCell( cellnum++ );
                    if ( licenseDetail.getLicenseStartDate() != null )
                        cell.setCellValue( licenseDetail.getLicenseStartDate().toLocaleString() );

                    cell = row.createCell( cellnum++ );
                    cell.setCellValue( licenseDetail.getPaymentRetries() );
                }
            }

        }
        // Create file and write report into it
        boolean excelCreated = false;
        String fileName = "Auto_Billing_Company_List-" + ( new Timestamp( new Date().getTime() ) );
        FileOutputStream fileOutput = null;
        InputStream inputStream = null;
        File file = null;
        String filePath = null;
        try {
            file = new File( fileDirectoryLocation + File.separator + fileName + ".xls" );
            fileOutput = new FileOutputStream( file );
            file.createNewFile();
            workbook.write( fileOutput );
            filePath = file.getPath();
            excelCreated = true;
        } catch ( FileNotFoundException fe ) {
            LOG.error( "Exception caught while generating Auto Billing Company List Reprot" + fe.getMessage() );
            excelCreated = false;
        } catch ( IOException e ) {
            LOG.error( "Exception caught  while generating Auto Billing Company List report " + e.getMessage() );
            excelCreated = false;
        } finally {
            try {
                fileOutput.close();
                if ( inputStream != null ) {
                    inputStream.close();
                }
            } catch ( IOException e ) {
                LOG.error( "Exception caught  while generating Auto Billing Company List report " + e.getMessage() );
                excelCreated = false;
            }
        }

        // Mail the report to the email
        if ( excelCreated ) {
            Map<String, String> attachmentsDetails = new HashMap<String, String>();
            attachmentsDetails.put( fileName + ".xls", filePath );


            String name = adminName;
            LOG.debug( "sending mail to : " + name + " at : " + recipientMailIds );
            try {
                emailServices.sendCustomReportMail( CommonConstants.ADMIN_RECEPIENT_DISPLAY_NAME, recipientMailIds,
                    CommonConstants.ACTIVE_SUBSCRIPTION_MAIL_SUBJECT, attachmentsDetails );
            } catch ( InvalidInputException | UndeliveredEmailException e ) {
                LOG.error( "Error while sending mail to ; " + recipientMailIds, e );
            }

        }

        LOG.info( "method generateAutoBillingCompanyListExcelAndMail ended" );
        return excelCreated;
    }
}