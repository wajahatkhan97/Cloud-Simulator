package hw;
import com.typesafe.config.Config;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyFirstFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import hw.abstractclassforpolicy.*;
//VmAllocationPolicy is the interface from cloudsim
public class vmallocationpolicy extends abstractclassforpolicy<VmAllocationPolicy,Config>{
    @Override
    VmAllocationPolicy assign_policy(Config object) {

            String vmAllocationPolicyType = object.getString("vmallocationpolicy");

            if(vmAllocationPolicyType.contains("simple"))
            {
                return new VmAllocationPolicySimple();
            }
            else
            {
                if(vmAllocationPolicyType.contains("BestFit"))
                {
                    return new VmAllocationPolicyBestFit();
                }
            }
            return null;

    }
}
