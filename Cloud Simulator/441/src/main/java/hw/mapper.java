package hw;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;

import java.util.ArrayList;
import java.util.List;

public class mapper {
    //what if this mapper(cloudletlist) only uses utilizationmodelstochastic
        //the length of the cloudlet is the number of instruction that the processor is going to execute


    public Cloudlet create_cloudlet(DatacenterBroker broker, int FileSize, int Output, UtilizationModel util, long length, int Pes)
        {
            Cloudlet cloudlet1 =
                    new CloudletSimple(length, 1)
                            .setFileSize(FileSize)
                            .setOutputSize(Output)
                            .setUtilizationModel(util);

            return cloudlet1;

//            Cloudlet cloudlet = new CloudletSimple()

        }
}
