package hw;

import com.typesafe.config.Config;
import hw.Applicationconfig;
import hw.hw1441;
import junit.framework.TestCase;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static hw.applicationconst.Cloud_provider;
import static hw.applicationconst.DATACENTER;

public class hw1441Test extends TestCase {
    /*Total 6 test cases */

    hw1441 testing;
    public void setUp() throws Exception {
        super.setUp();
    }

    @BeforeEach
    void initialize()
    {
        testing = new hw1441();
    }

    @Test
    public void testCreateAndSubmitCloudlets() {
         testing = new hw1441();
        DatacenterBroker broker;
        broker = testing.createBroker();
        List<? extends Config> datacentersConfig = Applicationconfig.simulation_config.getConfigList(Cloud_provider);

        testing.createAndSubmitCloudlets(broker,datacentersConfig.get(0).getConfig(DATACENTER));
        //initialize and assign vm and cloudlet
        //12 because of less number of hosts 4 cloudlets won't create
        Assert.assertEquals(12,testing.mappers.size());
    }

    @Test
    public void testReducer() {
//creating 12 reducer because we are using 1:1 ratio we can create less reducer as well if we decide 2:1 ratio or something
         testing = new hw1441();
        DatacenterBroker broker;
        broker = testing.createBroker();
        List<? extends Config> datacentersConfig = Applicationconfig.simulation_config.getConfigList(Cloud_provider);

        testing.reducer(broker,datacentersConfig.get(0).getConfig(DATACENTER));
        //initialize and assign vm and cloudlet
        //12 because of less number of hosts 4 cloudlets won't create
        Assert.assertEquals(12,testing.reducers.size());
    }

    @Test
    public void testCreateAndSubmitVms() {
         testing = new hw1441();
        DatacenterBroker broker;
        broker = testing.createBroker();
        List<? extends Config> datacentersConfig = Applicationconfig.simulation_config.getConfigList(Cloud_provider);

        testing.createAndSubmitVms(broker,datacentersConfig.get(0).getConfig(DATACENTER));
        //initialize and assign vm and cloudlet
        //12 because of less number of hosts 4 cloudlets won't create
        Assert.assertEquals(6,testing.vmlist.size());
    }

    @Test
    public void test_mapper_reducer() {
//
        testing = new hw1441();
        DatacenterBroker broker;
        broker = testing.createBroker();
        List<? extends Config> datacentersConfig = Applicationconfig.simulation_config.getConfigList(Cloud_provider);
        testing.createAndSubmitCloudlets(broker,datacentersConfig.get(0).getConfig(DATACENTER));
        testing.reducer(broker,datacentersConfig.get(0).getConfig(DATACENTER));
        testing.mapper_reducer_allocation(testing.mappers,testing.reducers);
        Assert.assertEquals(testing.reducers.get(0), testing.mapper_Reducer.get(testing.mappers.get(0))); //testing that mapper and its respective reducer
    }
    @Test
    public void testCreateBroker() {
        //checking if broker is creating successfully
         testing = new hw1441();
        DatacenterBroker broker;
        broker = testing.createBroker();
        Assert.assertNotNull(broker);
    }
}