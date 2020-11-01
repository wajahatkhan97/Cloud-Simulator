package hw;/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.resources.SanStorage;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmCost;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import  hw.Applicationconfig.*;
import org.cloudsimplus.listeners.CloudletVmEventInfo;
import org.cloudsimplus.listeners.EventListener;

import static hw.applicationconst.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class  hw1441 {
    private static final int VM_PES = 1;
    private final ArrayList<Datacenter> datacenter0;
    private  DatacenterBroker broker;
    private static final Logger LOGGER = LoggerFactory.getLogger(hw1441.class);

    public ArrayList<Cloudlet> mappers;
    public ArrayList<Cloudlet> reducers;
    public List<Vm> vmlist;
    private CloudSim simulation;
    private List<Datacenter> Datacenterlist;
   public  Map<Cloudlet,Cloudlet> mapper_Reducer = new HashMap<>();
    private  Map<Cloudlet,ArrayList<Cloudlet>> mapper_Reducer1 = new HashMap<>();
     String NETWORK_TOPOLOGY_FILE ;
    int count = 0;

    /**
     * Starts the example.
     *
     * @param args
     */
    public static void main(String[] args) {
        new hw1441();
    }

    public hw1441() {
        //Enables just some level of log messages.
        //Make sure to import org.cloudsimplus.util.Log;
        //Log.setLevel(ch.qos.logback.classic.Level.WARN);
            LOGGER.info("STARTING THE DATACENTER");
             //this cloud_provider is coming from applicationconst and redirecting to simulationdata
        List<? extends Config> datacentersConfig = Applicationconfig.simulation_config.getConfigList(Cloud_provider);
        vmlist = new ArrayList<>();
        mappers = new ArrayList<>();
        reducers = new ArrayList<>();
        datacenter0 = new ArrayList<>();
        simulation = new CloudSim();
        LOGGER.info("Creating DataCenters");
        for(int i=0;i<datacentersConfig.get(0).getInt(NUM_OF_Datacenter);i++){
        datacenter0.add(createDatacenter(datacentersConfig.get(i).getConfig(DATACENTER))); //creating two data centers with same config
        }
        LOGGER.info("Creating Brokers");
            broker = createBroker();
        NETWORK_TOPOLOGY_FILE = datacentersConfig.get(0).getConfig(DATACENTER).getString("NETWORK_TOPOLOGY_FILE");
        LOGGER.info("Configuring the network on which broker and datacenter will communicate");
        configureNetwork();
        LOGGER.info("CREATING AND SUBMITTING VMS");
        for(int i =0;i<=1;i++) { //using this loop to create VMs
            createAndSubmitVms(broker, datacentersConfig.get(i).getConfig(DATACENTER));
        }
        for(int i =0;i<2;i++) { //using this loop to generate more cloudlets
            createAndSubmitCloudlets(broker, datacentersConfig.get(i).getConfig(DATACENTER));
            reducer(broker, datacentersConfig.get(i).getConfig(DATACENTER));
        }
        LOGGER.info("ASSIGNING REDUCER TO MAPPERS 1:1 RATION");
        mapper_reducer_allocation(mappers,reducers);

        simulation.start();

        List<Cloudlet> newList = broker.getCloudletFinishedList();
        for(int i=0;i<newList.size();i++) { //printing the cost of each cloudlet
            LOGGER.info("Total Cost of " + i + "  cloudlets:  " + Double.toString(newList.get(i).getTotalCost()) + "  ACTUAL RAM UTILIZATION: " + newList.get(i).getUtilizationOfRam() + " ");

        }
        new CloudletsTableBuilder(newList).build();
        System.out.println(getClass().getSimpleName() + " finished!");
    }

    //same as cloudsim example
    private void configureNetwork() {
        //load the network topology file
        NetworkTopology networkTopology = BriteNetworkTopology.getInstance(NETWORK_TOPOLOGY_FILE);
        simulation.setNetworkTopology(networkTopology);

        //maps CloudSim entities to BRITE entities
        //Datacenter will correspond to BRITE node 0
        int briteNode = 0;
        networkTopology.mapNode(datacenter0.get(0).getId(), briteNode); //datacenter 0 communicating over britenode 0

        briteNode = 2;
        networkTopology.mapNode(datacenter0.get(1).getId(), briteNode); //datacenter 1 communicating over britenode==2

        //Broker will correspond to BRITE node 3
        briteNode = 3;
        networkTopology.mapNode(broker.getId(), briteNode);
    }

    public void createAndSubmitCloudlets(DatacenterBroker broker,Config config) {
//        final long length = 40000;

        //The RAM, CPU and Bandwidth UtilizationModel.
        final UtilizationModel utilizationModel = new UtilizationModelFull(); //to implement a resource usage
        List<? extends Config> hostsConfig = config.getConfigList(CLOUDLET);

        //mappers.add(map.create_cloudlet(broker,hostsConfig.get(0).getInt("length"));
        for(int i=0;i<config.getInt("numCloudlet");i++) {

            Cloudlet mapper =
                    new CloudletSimple(hostsConfig.get(0).getLong("length"), 1)
                            .setFileSize(hostsConfig.get(0).getLong("filesize"))
                            .setOutputSize(hostsConfig.get(0).getLong("outputsize"))
                            .setUtilizationModel(utilizationModel);

            mappers.add(mapper);

            mapper.addOnFinishListener(new EventListener<CloudletVmEventInfo>() {
                @Override
                public void update(CloudletVmEventInfo info) {
                    LOGGER.info("REDUCER WILL BE EXECUTED RIGHT AFTER MAPPER");
                    broker.submitCloudlet(mapper_Reducer.get(mapper)); //corresponding reducer will run

                }
            });        //add the cloudlet to the list
        }//submit cloudlet list to the broker
        broker.submitCloudletList(mappers);
    }
    public void reducer(DatacenterBroker broker,Config config) {
//        final long length = 40000;

        //The RAM, CPU and Bandwidth UtilizationModel.
//        final UtilizationModel utilizationModel = new UtilizationModelFull(); //to implement a resource usage
        List<? extends Config> hostsConfig = config.getConfigList(CLOUDLET);

        utilizationModel util = new utilizationModel();
        for(int i=0;i<config.getInt("numCloudlet");i++) {

            Cloudlet reducer1 =
                  new CloudletSimple(hostsConfig.get(0).getLong("length"), 1)
                          .setFileSize(hostsConfig.get(0).getLong("filesize"))
                          .setOutputSize(hostsConfig.get(0).getLong("outputsize"))
                          .setUtilizationModel(util.assign_policy(config)); //using full utilization model

          //add the cloudlet to the list
          reducers.add(reducer1);
          //submit cloudlet list to the broker
          broker.submitCloudletList(reducers);

      }
      }
    public void mapper_reducer_allocation(ArrayList<Cloudlet> mappers,ArrayList<Cloudlet> reducers)
    {

        for(int i=0; i<mappers.size();i++) {
                mapper_Reducer.put(mappers.get(i), reducers.get(i)); //assigning mapper to reducer 1:1
        }
    }

    public void createAndSubmitVms(DatacenterBroker broker,Config config) {
        List<? extends Config> hostsConfig = config.getConfigList(VM);

        cloudletscheduler cloudlet = new cloudletscheduler();
        final long size = 10000; //image size (Megabyte)
    for(int i=0;i<=1;i++) {
        Vm vm1 = new VmSimple(hostsConfig.get(i).getLong("mips"), hostsConfig.get(i).getLong("pes"))
                .setRam(hostsConfig.get(i).getLong("ram")).setBw(hostsConfig.get(i).getLong("bw")).setSize(size)
                .setCloudletScheduler(cloudlet.assign_policy(config)); //cloudlet policy spaceshared

        vmlist.add(vm1); //adding vms to vm lst
        broker.submitVmList(vmlist); //submitting vms

//        LOGGER.info(Double.toString(vmc.getTotalCost()));
    }
    }

    public Datacenter createDatacenter(Config config) {
        NetworkDatacenter new_datacenter;
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
          List<? extends Config> hosts_configuration = config.getConfigList("hosts");

        for(int i=0;i<1;i++) { //creating two hosts
            for(int j =0;j<2;j++) {
                peList.add(new PeSimple(hosts_configuration.get(j).getConfigList("pes").get(j).getLong("mips"), new PeProvisionerSimple())); //creating PE's
                Host host = new HostSimple(hosts_configuration.get(j).getLong("ram"), hosts_configuration.get(j).getLong("bw"), hosts_configuration.get(j).getLong("storage"), peList);
                hostList.add(host);


            }
        }
        vmallocationpolicy vm = new  vmallocationpolicy();
        new_datacenter = new NetworkDatacenter(simulation, hostList,vm.assign_policy(config));
        new_datacenter.getCharacteristics().setVmm("King " + count);
        new_datacenter.getCharacteristics().setOs(hosts_configuration.get(count).getString("os"));
        new_datacenter.getCharacteristics().setArchitecture(hosts_configuration.get(count).getString("architecture"));
        new_datacenter.getCharacteristics().setCostPerBw(hosts_configuration.get(count).getDouble("costPerBw"));
        new_datacenter.getCharacteristics().setCostPerMem(hosts_configuration.get(count).getDouble("costPerMem"));
        new_datacenter.getCharacteristics().setCostPerSecond(hosts_configuration.get(count).getDouble(("costPerSec")));
        new_datacenter.getCharacteristics().setCostPerStorage(hosts_configuration.get(count).getDouble(("costPerStorage")));

        LOGGER.info("Active HOSTS"  + new_datacenter.getActiveHostsNumber());
        LOGGER.info(new_datacenter.getCharacteristics().getVmm());
        LOGGER.info(new_datacenter.getCharacteristics().getOs());
        count++;
        return (new_datacenter);
    }

    public DatacenterBroker createBroker() {
        return new DatacenterBrokerSimple(simulation); //creating datacenter using simple
    }
}
