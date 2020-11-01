package hw;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;

public class cloudletscheduler extends abstractclassforpolicy<CloudletScheduler, Config> {
    @Override
    CloudletScheduler assign_policy(Config object) {


        String vmAllocationPolicyType = object.getString("CloudletSchedulerSpaceShared");

        if(vmAllocationPolicyType.contains("CloudletSchedulerSpaceShared"))
        {
            return new CloudletSchedulerSpaceShared();
        }
        else
        {
            return new CloudletSchedulerTimeShared();
        }
    }
}
