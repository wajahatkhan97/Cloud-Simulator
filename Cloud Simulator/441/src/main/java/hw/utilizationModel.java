package hw;

import com.typesafe.config.Config;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic;

public class utilizationModel extends abstractclassforpolicy<UtilizationModel, Config>{

    @Override
    UtilizationModel assign_policy(Config object) {
        String vmAllocationPolicyType = object.getString("utilizationmodel");

        if (vmAllocationPolicyType.contains("UtilizationModelFull")) {
            final UtilizationModel utilizationModel = new UtilizationModelFull(); //to implement a resource usage
            return utilizationModel;
        }
        else
        {
            if (vmAllocationPolicyType.contains("UtilizationStochasticmodel")) {
                final UtilizationModel utilizationModel = new UtilizationModelStochastic(); //to implement a resource usage
                return utilizationModel;
            }
        }
        return null;
    }
}
