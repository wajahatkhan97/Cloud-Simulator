package hw;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import hw.Applicationconfig.*;
public class applicationconst {

    public static final String PES = Applicationconfig.complete_appconfig.getString("PES");;
    public static final String RAM = Applicationconfig.complete_appconfig.getString("RAM");;
    public static final String BW = Applicationconfig.complete_appconfig.getString("BW");;
    public static final String NUM_OF_Datacenter = Applicationconfig.complete_appconfig.getString("NUM_OF_Datacenter");
    public static final String MIPS = Applicationconfig.complete_appconfig.getString("MIPS");;
    public static final String Cloud_provider = Applicationconfig.complete_appconfig.getString("Cloud_provider");;
    public static final String DATACENTER = Applicationconfig.complete_appconfig.getString("DATACENTER");;
    public static final String HOSTS = Applicationconfig.complete_appconfig.getString("HOSTS");;
    public static final String VM_ALLOCATION_POLICY = Applicationconfig.complete_appconfig.getString("VM_ALLOCATION_POLICY");;
    public static final String LENGTH = Applicationconfig.complete_appconfig.getString("LENGTH");;
    public static final String FILESIZE = Applicationconfig.complete_appconfig.getString("FILESIZE");;
    public static final String OUTPUTSIZE = Applicationconfig.complete_appconfig.getString("OUTPUTSIZE");;
    public static final String CLOUD_POLICY = Applicationconfig.complete_appconfig.getString("CLOUD_POILCY");;
    public static final String CLOUDLET = Applicationconfig.complete_appconfig.getString("CLOUDLET");;
    public static final String VM = Applicationconfig.complete_appconfig.getString("VM");;
    public static final String CloudletSchedulerSpaceShared = Applicationconfig.complete_appconfig.getString("CloudletSchedulerSpaceShared");;
    public static final String OS = Applicationconfig.complete_appconfig.getString("OS");;
    public static final String ARCHITECTURE = Applicationconfig.complete_appconfig.getString("architecture");;
    public static final String COST = Applicationconfig.complete_appconfig.getString("cost");;
    public static final String COSTPERSEC = Applicationconfig.complete_appconfig.getString("costpersec");
    public static final String COSTPERMEM = Applicationconfig.complete_appconfig.getString("costPerMem");;
    public static final String COSTPERSTORAGE = Applicationconfig.complete_appconfig.getString("costPerStorage");
    public static final String COSTPERBW = Applicationconfig.complete_appconfig.getString("costPerBw");

    public static final String numCloudlet = Applicationconfig.complete_appconfig.getString("numCloudlet");
    public static final String numhosts = Applicationconfig.complete_appconfig.getString("numhosts");






}
