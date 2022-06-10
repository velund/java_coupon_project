package definitions;

public final class ConnectionDefinitions
{

    public static final int MAX_COMPANY_CONNECTIONS  = 5;
    public static final int MAX_CLIENT_CONNECTIONS  = 20;
    public static final int MAX_ADMIN_CONNECTIONS  = 5;
    public static final int MAX_CLIENT_POOL_INSTANCES = 2;
    public static final int MAX_COMPANY_POOL_INSTANCES = 1;
    public static final int MAX_ADMIN_POOL_INSTANCES = 1;

    public static final String COMPANY_USER  = "company";
    public static final String CLIENT_USER  = "client";
    public static final String ADMIN_USER  = "krill_admin";
    public static final String PASSWORD  = "15kvz61982!";
    public static final String PORT  = "3306";
    public static final String DB  = "coupon_system";
    public static final String URL = "jdbc:mysql://localhost:" + PORT + "/" + DB;
    public static final String CONNECTION_FAILED_FORMAT_MSG = "failed to connect to db as %s";

    public static final String NUM_OF_ACTIVE_CONNECTIONS_FORMAT_MSG = "some connections failed, num of active: %d";
    public static final String MAX_NEW_INSTANCES_REACHED_FORMAT_MSG = "maximum %d instance of company pool allowed";

    private ConnectionDefinitions()
    {

    }
}
