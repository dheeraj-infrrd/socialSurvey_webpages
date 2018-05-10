/**
 * 
 */
package com.realtech.socialsurvey.compute;

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realtech.socialsurvey.compute.common.EnvConstants;
import com.realtech.socialsurvey.compute.topology.bolts.emailreports.AggregateSolrQueryBolt;
import com.realtech.socialsurvey.compute.topology.spouts.KafkaTopicSpoutBuilder;
import com.realtech.socialsurvey.compute.utils.ChararcterUtils;

/**
 * @author Subhrajit
 *
 */
public class EmailReportsTopologyStarterHelper extends TopologyStarterHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger( EmailReportsTopologyStarterHelper.class );
	public static final String EMAIL_REPORTS_TOPOLOGY = "EmailReportsTopology";
	
	@Override protected void displayBanner()
    {
        LOG.info( "          ███████╗███╗   ███╗ █████╗ ██╗██╗         ██████╗ ███████╗██████╗  ██████╗ ██████╗ ████████╗           " );
        LOG.info( "          ██╔════╝████╗ ████║██╔══██╗██║██║         ██╔══██╗██╔════╝██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝           " );
        LOG.info( "          █████╗  ██╔████╔██║███████║██║██║         ██████╔╝█████╗  ██████╔╝██║   ██║██████╔╝   ██║              " );
        LOG.info( "          ██╔══╝  ██║╚██╔╝██║██╔══██║██║██║         ██╔══██╗██╔══╝  ██╔═══╝ ██║   ██║██╔══██╗   ██║              " );
        LOG.info( "          ███████╗██║ ╚═╝ ██║██║  ██║██║███████╗    ██║  ██║███████╗██║     ╚██████╔╝██║  ██║   ██║              " );
        LOG.info( "          ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝╚══════╝    ╚═╝  ╚═╝╚══════╝╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝              " );
        LOG.info( "                                                                                                                 " );
        LOG.info( "                  ████████╗ ██████╗ ██████╗  ██████╗ ██╗      ██████╗  ██████╗██╗   ██╗                          " );
        LOG.info( "                  ╚══██╔══╝██╔═══██╗██╔══██╗██╔═══██╗██║     ██╔═══██╗██╔════╝╚██╗ ██╔╝                          " );
        LOG.info( "                     ██║   ██║   ██║██████╔╝██║   ██║██║     ██║   ██║██║  ███╗╚████╔╝                           " );
        LOG.info( "                     ██║   ██║   ██║██╔═══╝ ██║   ██║██║     ██║   ██║██║   ██║ ╚██╔╝                            " );
        LOG.info( "                     ██║   ╚██████╔╝██║     ╚██████╔╝███████╗╚██████╔╝╚██████╔╝  ██║                             " );
        LOG.info( "                     ╚═╝    ╚═════╝ ╚═╝      ╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝   ╚═╝                             " );
    }

	/* (non-Javadoc)
	 * @see com.realtech.socialsurvey.compute.TopologyStarterHelper#createConfig(boolean)
	 */
	@Override
	public Config createConfig(boolean isLocalMode) {
		if ( isLocalMode ) {
            Config config = new Config();
            config.put( Config.TOPOLOGY_DEBUG, true );
            return config;
        } else {
            Config config = new Config();
            config.put( Config.TOPOLOGY_MAX_SPOUT_PENDING, 5000 );
            config.put( Config.STORM_NIMBUS_RETRY_TIMES, 3 );
            /* The maximum amount of time given to the topology to 
               fully process a message emitted by a spout. If the 
               message is not acked within this time frame, 
               Storm will fail the message on the spout. 
               Some spouts implementations will then replay the 
               message at a later time. */ 
            config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 300);
            return config;
        }
	}

	/* (non-Javadoc)
	 * @see com.realtech.socialsurvey.compute.TopologyStarterHelper#topology()
	 */
	@Override
	protected StormTopology topology() {
		LOG.info( "Creating mail reports topology" );
        TopologyBuilder builder = new TopologyBuilder();
        //add the spout
        builder.setSpout("BatchProcessingSpout", KafkaTopicSpoutBuilder.getInstance().batchProcessingSpout(), 1);
        //add the bolts
        builder.setBolt("AggregateSolrQueryBolt", new AggregateSolrQueryBolt(), 1)
        .shuffleGrouping("BatchProcessingSpout");
        
        return builder.createTopology();
	}
	
	@Override 
	protected boolean validateTopologyEnvironment()
    {
	   LOG.info("No env params required to validate.");
       return true;
    }
	
	public static void main( String[] args )
    {
        LOG.info( "Starting up email reports topology..." );
        // Run time params should be the first step
        // DO NOT ADD ANY CODE BEFORE THIS LINE
        EnvConstants.runtimeParams( args );
        new EmailReportsTopologyStarterHelper().submitTopology( EnvConstants.getCluster().equals( EnvConstants.LOCAL_TOPOLOGY ),
                ( EnvConstants.getProfile().equals( EnvConstants.PROFILE_PROD ) ) ?
                		EMAIL_REPORTS_TOPOLOGY :
                        ChararcterUtils.appendWithHypen(EMAIL_REPORTS_TOPOLOGY, EnvConstants.getProfile() ) );
    }

}