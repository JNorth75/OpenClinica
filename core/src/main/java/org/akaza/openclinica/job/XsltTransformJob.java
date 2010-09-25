package org.akaza.openclinica.job;

import org.akaza.openclinica.bean.extract.ExtractPropertyBean;
import org.akaza.openclinica.bean.service.ProcessingFunction;
import org.akaza.openclinica.dao.core.CoreResources;
import org.akaza.openclinica.i18n.util.ResourceBundleProvider;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Locale;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Xalan Transform Job, an XSLT transform job using the Xalan classes
 * @author thickerson
 *
 */
public class XsltTransformJob extends QuartzJobBean {
    
    public static final String DATASET_ID = "dsId";
    public static final String EMAIL = "contactEmail";
    public static final String USER_ID = "user_id";
    public static final String XSL_FILE_PATH = "xslFilePath";
    public static final String XML_FILE_PATH = "xmlFilePath";
    public static final String POST_FILE_PATH = "postFilePath";
    public static final String POST_FILE_NAME = "postFileName";
    public static final String EXTRACT_PROPERTY = "extractProperty";
    
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // need to generate a Locale so that user beans and other things will
        // generate normally
        // TODO make dynamic?
        Locale locale = new Locale("en-US");
        ResourceBundleProvider.updateLocale(locale);
        ResourceBundle pageMessages = ResourceBundleProvider.getPageMessagesBundle();
        JobDataMap dataMap = context.getMergedJobDataMap();
        // get the file information from the job
        String alertEmail = dataMap.getString(EMAIL);
        try {
            // create dirs 
            String outputPath = dataMap.getString(POST_FILE_PATH);
            File output = new File(outputPath);
            if (!output.isDirectory()) {
                output.mkdirs();
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            
            // Use the TransformerFactory to instantiate a Transformer that will work with  
            // the stylesheet you specify. This method call also processes the stylesheet
            // into a compiled Templates object.
            java.io.InputStream in = new java.io.FileInputStream(dataMap.getString(XSL_FILE_PATH));
            // tFactory.setAttribute("use-classpath", Boolean.TRUE);
            // tFactory.setErrorListener(new ListingErrorHandler());
            Transformer transformer = tFactory.newTransformer(new StreamSource(in));

            // Use the Transformer to apply the associated Templates object to an XML document
            // (foo.xml) and write the output to a file (foo.out).
            System.out.println("--> job starting: ");
            final long start = System.currentTimeMillis();
            transformer.transform(new StreamSource(dataMap.getString(XML_FILE_PATH)), 
                    new StreamResult(new FileOutputStream(outputPath + File.separator + dataMap.getString(POST_FILE_NAME))));
            final long done = System.currentTimeMillis() - start;
            System.out.println("--> job completed in " + done + " ms");
            // run post processing
            int epBeanId = dataMap.getInt(EXTRACT_PROPERTY);
            ExtractPropertyBean epBean = CoreResources.findExtractPropertyBeanById(epBeanId);
            ProcessingFunction function = epBean.getPostProcessing();
            function.setTransformFileName(outputPath + File.separator + dataMap.getString(POST_FILE_NAME));
            function.setXslFileName(dataMap.getString(XSL_FILE_PATH));
            String message = function.run();
            final long done2 = System.currentTimeMillis() - start;
            System.out.println("--> postprocessing completed in " + done2 + " ms");
            // email the message to the user
            
            // ping the users' session
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
