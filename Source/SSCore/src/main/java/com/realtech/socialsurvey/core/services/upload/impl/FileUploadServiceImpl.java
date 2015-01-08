package com.realtech.socialsurvey.core.services.upload.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.realtech.socialsurvey.core.commons.CommonConstants;
import com.realtech.socialsurvey.core.exception.FatalException;
import com.realtech.socialsurvey.core.exception.InvalidInputException;
import com.realtech.socialsurvey.core.services.upload.FileUploadService;
import com.realtech.socialsurvey.core.utils.DisplayMessageConstants;
import com.realtech.socialsurvey.core.utils.EncryptionHelper;
import com.realtech.socialsurvey.core.utils.PropertyFileReader;

@Component
public class FileUploadServiceImpl implements FileUploadService {

	private static final Logger LOG = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	@Autowired
	private PropertyFileReader propertyFileReader;
	
	@Autowired
	private EncryptionHelper encryptionHelper;

	@Autowired
	private UploadUtils uploadUtils;

	@Override
	public String fileUploadHandler(MultipartFile fileLocal, String logoName) throws InvalidInputException {
		LOG.info("Method imageUploadHandler inside ImageUploadServiceImpl called");

		BufferedOutputStream stream = null;
		if (!fileLocal.isEmpty()) {
			try {
				byte[] bytes = fileLocal.getBytes();

				File convFile = new File(fileLocal.getOriginalFilename());
				fileLocal.transferTo(convFile);

				uploadUtils.validateFile(convFile);

				// Creating the directory to store file
				LOG.debug("Creating the directory to store file");
				String rootPath = propertyFileReader.getProperty(CommonConstants.CONFIG_PROPERTIES_FILE, CommonConstants.LOGO_HOME_DIRECTORY);
				File dir = new File(rootPath);
				if (!dir.exists() && !dir.mkdirs()) {
					throw new FatalException("Directory for Logo upload can not be created. Reason: Permission denied"); 
				}

				// Create the file on server
				LOG.debug("Creating the file on server");
				String logoFormat = logoName.substring(logoName.lastIndexOf("."));
				String logoNameHash = encryptionHelper.encryptSHA512(logoName+(System.currentTimeMillis()));
				File serverFile = new File(dir.getAbsolutePath() + File.separator + logoNameHash + logoFormat);
				stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);

				LOG.debug("Server File Location=" + serverFile.getAbsolutePath());
				LOG.info("Method imageUploadHandler inside ImageUploadServiceImpl completed successfully");
				return logoNameHash + logoFormat;
			}
			catch (IOException e) {
				LOG.error("IOException occured while reading file. Reason : " + e.getMessage(), e);
				throw new FatalException("IOException occured while reading file. Reason : " + e.getMessage(), e);
			}
			finally {
				try {
					if (stream != null) {
						stream.close();
					}
				}
				catch (IOException e) {
					LOG.error("IOException occured while closing the BufferedOutputStream. Reason : " + e.getMessage(), e);
				}
			}
		}
		else {
			LOG.error("Method imageUploadHandler inside ImageUploadServiceImpl failed to upload");
			throw new InvalidInputException("Upload failed: " + logoName + " because the file was empty", DisplayMessageConstants.INVALID_LOGO_FILE);
		}
	}
}